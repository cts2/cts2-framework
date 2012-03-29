package edu.mayo.cts2.framework.webapp.soap.endpoint;

import java.util.concurrent.Callable;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.QueryControl;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.service.profile.ReadService;
import edu.mayo.cts2.framework.service.profile.TagAwareReadService;

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
	
	
	protected VersionTagReference resolveTag(NameOrURI tag, TagAwareReadService<?,?> readService ){
		if(CollectionUtils.isNotEmpty(readService.getSupportedTags())){
			for(VersionTagReference foundTag : readService.getSupportedTags()){
				if(StringUtils.equals(tag.getName(), foundTag.getContent())
						||
						StringUtils.equals(tag.getUri(), foundTag.getUri())){
					return foundTag;
				}		
			}
		}
		
		throw ExceptionFactory.createUnsupportedVersionTag(tag, readService.getSupportedTags());
	}
	

}
