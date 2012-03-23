package edu.mayo.cts2.framework.webapp.rest.view;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

public class Inspector {

	public static boolean shouldRecurse(Object bean) {
		return !BeanUtils.isSimpleProperty(bean.getClass()) && !bean.getClass().isEnum();
	}

	public static List<Map.Entry<String, Object>> inspect(Object bean) {
		Map<String, Object> props = new LinkedHashMap<String, Object>();

		Class<?> clazz = bean.getClass();
		
		while(clazz != null){
			for(Field field : clazz.getDeclaredFields()){
				field.setAccessible(true);
				String name = field.getName();
				
				Object value;
				try {
					value = field.get(bean);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
	
				if(value != null){
					props.put(name, value);
				}
			}
			clazz = clazz.getSuperclass();
		}

		return new ArrayList<Map.Entry<String, Object>>(props.entrySet());
	}
}
