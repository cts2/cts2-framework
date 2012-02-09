package edu.mayo.cts2.framework.webapp.soap.endpoint;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.service.core.QueryControl;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.webapp.service.AbstractServiceAwareBean;

public class AbstractEndpoint extends AbstractServiceAwareBean {
	
	private ExecutorService executorService = Executors.newCachedThreadPool();
	
	protected ResolvedReadContext resolveReadContext(ReadContext context){
		if(context == null){
			return null;
		}
		
		ResolvedReadContext resolvedContext = new ResolvedReadContext();
		resolvedContext.setChangeSetContextUri(context.getChangeSetContext());
		//TODO: Finish the language part
		return resolvedContext;
	}

	protected <T,I> T doTimedCall(Callable<T> callable, QueryControl queryControl){
		
		if(queryControl == null || queryControl.getTimeLimit() < 0){
			try {
				return callable.call();
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
		} else {
		
			Future<T> future = this.executorService.submit(callable);

			long time = queryControl.getTimeLimit();

			try {
				return future.get(time, TimeUnit.SECONDS);
			} catch (TimeoutException e) {
				throw ExceptionFactory.createTimeoutException();
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			} catch (ExecutionException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}
