package edu.mayo.cts2.framework.webapp.soap.endpoint;

import edu.mayo.cts2.framework.model.core.Directory;
import edu.mayo.cts2.framework.model.core.RESTResource;
import edu.mayo.cts2.framework.model.core.types.CompleteDirectory;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.QueryControl;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.model.wsdl.basequeryservice.*;
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.*;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.service.profile.QueryService;
import edu.mayo.cts2.framework.service.profile.ReadService;
import edu.mayo.cts2.framework.webapp.soap.directoryuri.SoapDirectoryUriRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;


public abstract class AbstractQueryServiceEndpoint extends AbstractEndpoint {

  protected <T,I> T doCount(
      final QueryService queryService,
      final I identifier,
      final QueryControl queryControl,
      ReadContext readContext){
   return null;
  }
  
  protected <T extends Directory> T populateDirectory(
      DirectoryResult<?> result,
      SoapDirectoryUriRequest<?> request,
      Class<T> directoryClazz) {

    boolean atEnd = result.isAtEnd();

    boolean isComplete = atEnd && (request.getPage() == 0);

    T directory;
    try {
      directory = directoryClazz.newInstance();

      final Field field = ReflectionUtils.findField(directoryClazz,
          "_entryList");

      AccessController.doPrivileged(new PrivilegedAction<Void>() {
        public Void run() {
          field.setAccessible(true);

          return null;
        }
      });

      ReflectionUtils.setField(field, directory, result.getEntries());
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    if (isComplete) {
      directory.setComplete(CompleteDirectory.COMPLETE);
    }
    else {
      directory.setComplete(CompleteDirectory.PARTIAL);

      //TODO:
    }

    directory.setNumEntries((long) result.getEntries().size());

    directory.setHeading(new RESTResource());
    directory.getHeading().setAccessDate(new Date());
    directory.getHeading().setResourceRoot("soap");
    directory.getHeading().setResourceURI("soap");

    return directory;
  }
}
