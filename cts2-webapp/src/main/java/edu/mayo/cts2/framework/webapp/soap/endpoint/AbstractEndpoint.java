package edu.mayo.cts2.framework.webapp.soap.endpoint;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.service.core.QueryControl;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.webapp.service.AbstractServiceAwareBean;

import java.util.concurrent.*;

public class AbstractEndpoint extends AbstractServiceAwareBean {

    protected static final String CTS2_NAMESPACE_ROOT = "http://www.omg.org/spec/CTS2/1.1/wsdl/";
	
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
