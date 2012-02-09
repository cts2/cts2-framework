package edu.mayo.cts2.framework.webapp.soap.endpoint;

import java.util.concurrent.Callable;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.QueryControl;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.service.profile.ReadService;

public abstract class AbstractReadServiceEndpoint extends AbstractEndpoint {
		
	protected <T,I> T doRead(
			final ReadService<T,I> readService, 
			final I identifier, 
			final QueryControl queryControl, 
			ReadContext readContext){
		final ResolvedReadContext resolvedReadContext = this.resolveReadContext(readContext);
		
		return this.doTimedCall(new Callable<T>(){

			@Override
			public T call() throws Exception {
				return readService.read(identifier, resolvedReadContext);
			}
			
		}, queryControl);	
	}

}
