package edu.mayo.cts2.framework.webapp.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.mayo.cts2.framework.model.service.core.BaseReadService;
import edu.mayo.cts2.framework.model.service.core.BaseService;
import edu.mayo.cts2.framework.service.profile.BaseMaintenanceService;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.service.profile.Cts2Profile;
import edu.mayo.cts2.framework.service.profile.HistoryService;
import edu.mayo.cts2.framework.service.profile.ReadService;
import edu.mayo.cts2.framework.service.provider.ServiceProvider;
import edu.mayo.cts2.framework.webapp.service.ServiceBuilder;

@Controller
public class ServiceController extends AbstractMessageWrappingController
		implements InitializingBean {

	@Resource
	private ServiceBuilder serviceBuilder;

	private class ServiceType {
		private static final String READ_SUFFIX = "read";
		private static final String QUERY_SUFFIX = "query";
		private static final String MAINTENANCE_SUFFIX = "maintenance";
		private static final String HISTORY_SUFFIX = "history";

		private static final String SERVICE_SUFFIX = "Service";

		private static final String MODEL_PACKAGE = "edu.mayo.cts2.framework.model.service";
		private static final String SERVICE_PACKAGE = "edu.mayo.cts2.framework.service.profile";
		
		private final Map<String,String> structureCaseMap = new HashMap<String,String>();
		{
			structureCaseMap.put("codesystem", "CodeSystem");
			structureCaseMap.put("codesystemversion", "CodeSystemVersion");
			structureCaseMap.put("valueset", "valueSet");
			structureCaseMap.put("valuesetdefinition", "ValueSetDefinition");
			structureCaseMap.put("conceptdomain", "ConceptDomain");
			structureCaseMap.put("conceptdomainbinding", "ConceptDomainBinding");
			structureCaseMap.put("mapentry", "MapEntry");
			structureCaseMap.put("mapversion", "MapVersion");
			structureCaseMap.put("entitydescription", "EntityDescription");
		}

		private BaseService service;

		private ServiceType(String path) {
			this.service = this.getService(path);
		}

		private BaseService getBaseService() {
			return this.service;
		}

		private Class<?> classForName(String name) {
			try {
				return Class.forName(name);
			} catch (ClassNotFoundException e) {
				throw new InvalidServiceRequest();
			}
		}

		@SuppressWarnings("unchecked")
		private BaseService getService(String path) {
			ServiceProvider serviceProvider = ServiceController.this
					.getServiceProviderFactory().getServiceProvider();

			String suffix = this.getSuffix(path);

			String serviceClassName = this.getClassNameGivenSuffix(path,
					SERVICE_PACKAGE, suffix);

			Class<Cts2Profile> serviceClass = (Class<Cts2Profile>) this
					.classForName(serviceClassName);

			String metadataClassName = this.getClassNameGivenSuffix(path,
					MODEL_PACKAGE, suffix);

			if (StringUtils.equals(suffix, READ_SUFFIX)) {
				ReadService<?, ?> service = (ReadService<?, ?>) serviceProvider
						.getService(serviceClass);
				return serviceBuilder.buildServiceMetadata(service,
						(Class<BaseReadService>) this.classForName(metadataClassName));
			} else if (StringUtils.equals(suffix, QUERY_SUFFIX)) {
				BaseQueryService service = (BaseQueryService) serviceProvider
						.getService(serviceClass);
				return serviceBuilder
						.buildServiceMetadata(
								service,
								(Class<edu.mayo.cts2.framework.model.service.core.BaseQueryService>) 
									this.classForName(metadataClassName));
			} else if (StringUtils.equals(suffix, HISTORY_SUFFIX)) {
				HistoryService<?, ?> service = (HistoryService<?, ?>) serviceProvider
						.getService(serviceClass);
				return serviceBuilder
						.buildServiceMetadata(
								service,
								(Class<edu.mayo.cts2.framework.model.service.core.HistoryService>) 
									this.classForName(metadataClassName));
			} else if (StringUtils.equals(suffix, MAINTENANCE_SUFFIX)) {
				BaseMaintenanceService<?, ?, ?> service = (BaseMaintenanceService<?, ?, ?>) serviceProvider
						.getService(serviceClass);
				return serviceBuilder
						.buildServiceMetadata(
								service,
								(Class<edu.mayo.cts2.framework.model.service.core.BaseMaintenanceService>) 
									this.classForName(metadataClassName));
			} else {
				throw new IllegalStateException();
			}
		}

		private String getSuffix(String path) {
			String suffix;

			if (StringUtils.endsWith(path, READ_SUFFIX)) {
				suffix = READ_SUFFIX;
			} else if (StringUtils.endsWith(path, QUERY_SUFFIX)) {
				suffix = QUERY_SUFFIX;
			} else if (StringUtils.endsWith(path, HISTORY_SUFFIX)) {
				suffix = HISTORY_SUFFIX;
			} else if (StringUtils.endsWith(path, MAINTENANCE_SUFFIX)) {
				suffix = MAINTENANCE_SUFFIX;
			} else {
				throw new InvalidServiceRequest();
			}

			return suffix;
		}

		private String getClassNameGivenSuffix(String path, String packageName,
				String suffix) {
			path = path.toLowerCase();

			String structure = StringUtils.substringBefore(path, suffix);

			String structureName;
			if(structureCaseMap.containsKey(structure)){
				structureName = structureCaseMap.get(structure);
			} else {
				structureName = StringUtils.capitalize(structure);
			}
			
			return packageName + "." + 
				structure + "."+ 
				structureName + 
				StringUtils.capitalize(suffix) + SERVICE_SUFFIX;
		}
	}

	@RequestMapping(value = PATH_SERVICE, method = RequestMethod.GET)
	public Object getService(HttpServletRequest request,
			@PathVariable(VAR_SERVICEID) String serviceId) {
		ServiceType serviceType = new ServiceType(serviceId);

		BaseService result = serviceType.getBaseService();

		return this.buildResponse(request, result);
	}

	private class InvalidServiceRequest extends RuntimeException {

		private static final long serialVersionUID = 7588100241967323612L;

		private InvalidServiceRequest() {
			super();
		}

	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ InvalidServiceRequest.class })
	public void handle() {
		// ...
	}
}
