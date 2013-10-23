/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mayo.cts2.framework.webapp.rest.controller;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import edu.mayo.cts2.framework.core.timeout.Timeout;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.webapp.rest.command.QueryControl;

/**
 * The Class MethodTimingAspect.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE)
public class MethodTimingAspect {
	
	private ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactory()
	{
		@Override
		public Thread newThread(Runnable r)
		{
			Thread t = new Thread(r);
			t.setName("ProcessingThread-" + t.getId());
			return t;
		}
	});
	private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, new ThreadFactory()
	{
		@Override
		public Thread newThread(Runnable r)
		{
			Thread t = new Thread(r);
			t.setName("ExecutionTimer-" + t.getId());
			return t;
		}
	});
	
	/**
	 * Execute.
	 *
	 * @param pjp the pjp
	 * @return the object
	 * @throws Throwable the throwable
	 */
	@Around("execution(public *"
	        + " edu.mayo.cts2.framework.webapp.rest.controller.*.*(..,edu.mayo.cts2.framework.webapp.rest.command.QueryControl,..))")
	    public Object execute(final ProceedingJoinPoint pjp) throws Throwable {

		QueryControl queryControl = null;
		
		//this should never happen
		if(ArrayUtils.isEmpty(pjp.getArgs())){
			throw new IllegalStateException("Pointcut failure!");
		}
		
		for(Object arg : pjp.getArgs()){
			if(arg.getClass() == QueryControl.class){
				queryControl = (QueryControl) arg;
				break;
			}
		}
		
		//this also should never happen
		if(queryControl == null){
			throw new IllegalStateException("Pointcut failure!");
		}
		
		final AtomicLong threadId = new AtomicLong(-1); 
		
		Future<Object> future = this.executorService.submit(new Callable<Object>(){

			@Override
			public Object call() {
				try {
					threadId.set(Thread.currentThread().getId());
					
					/*
					 * The model here is that we clear any previous timeout before we launch the job. A design flaw is that we
					 * can't tell if we are clearing a previous timeout that simply hadn't been cleaned up yet, or if we are
					 * clearing a timeout meant for this thread that happened before this thread even launched. The second scenario 
					 * seems unlikely as the minimum timeout is 1 second - hard to believe it would take more than 1 second to 
					 * launch this thread. Plus, this thread would have to launch in the exact window in between the timeout and 
					 * the future.cancel()
					 * 
					 * If the above scenario did defy all odds and happen , it shouldn't cause much harm, as the end result would
					 * be that this thread wouldn't see the cancelled flag - and would churn away for no reason, wasting some cpu
					 * cycles, but doing no other harm.
					 */
					
					Timeout.clearThreadFlag(threadId.get());
					return pjp.proceed();
				} catch (Throwable e) {
					
					if(e instanceof Error){
						throw (Error)e;
					}
					
					if(e instanceof RuntimeException){
						throw (RuntimeException)e;
					}
					
					throw new RuntimeException(e);
				}
			}
		});
		
		long time = queryControl.getTimelimit();

		try {
			if(time < 0){
				return future.get();
			} else {
				return future.get(time, TimeUnit.SECONDS);
			}
		} catch (ExecutionException e) {
			throw e.getCause();
		} catch (TimeoutException e) {
			try
			{
				//Set the flag for the processing thread to read
				Timeout.setTimeLimitExceeded(threadId.get());
				
				//Schedule another future to make sure we don't cause a memory leak if the thread IDs aren't being reused (though, they should be)
				//and therefore don't get cleared up by the next run.  Give the running thread 30 seconds to see the cancelled flag before this 
				//cleanup takes place.
				this.scheduledExecutorService.schedule(new Runnable()
				{
					@Override
					public void run()
					{
						Timeout.clearThreadFlag(threadId.get());
					}
				}, 30, TimeUnit.SECONDS);
				
				//Interrupt the processing thread so it has an opportunity to check the flag and stop.
				future.cancel(true);
			}
			catch (Exception e1)
			{
				// don't think this is possible, but just in case...
			}
			throw ExceptionFactory.createTimeoutException(e.getMessage());
		}
	}
}
