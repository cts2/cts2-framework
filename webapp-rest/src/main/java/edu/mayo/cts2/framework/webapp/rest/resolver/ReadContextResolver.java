package edu.mayo.cts2.framework.webapp.rest.resolver;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;

@Component
public class ReadContextResolver {

	public ResolvedReadContext resolveRestReadContext(RestReadContext context){
		if(context == null){
			return null;
		}
		
		ResolvedReadContext resolvedContext = new ResolvedReadContext();
		resolvedContext.setChangeSetContextUri(context.getChangesetcontext());
		//TODO: Finish this method
		return resolvedContext;
	}
}
