package edu.mayo.cts2.framework.webapp.soap.directoryuri;

import java.io.Serializable;
import java.util.Set;

import edu.mayo.cts2.framework.model.core.types.SetOperator;
import edu.mayo.cts2.framework.model.service.core.FilterComponent;

public interface SoapDirectoryUri<T> extends Serializable {
	
	public Set<FilterComponent> getFilterComponents();
	
	public T getRestrictions();
	
	public SetOperation<T> getSetOperation();
	
	public static class SetOperation<T> implements Serializable {

		private static final long serialVersionUID = -6445696738700141690L;
		
		private SetOperator setOperator;
		private SoapDirectoryUri<T> soapDirectoryUri1;
		private SoapDirectoryUri<T> soapDirectoryUri2;
		public SetOperator getSetOperator() {
			return setOperator;
		}
		public void setSetOperator(SetOperator setOperator) {
			this.setOperator = setOperator;
		}
		public SoapDirectoryUri<T> getSoapDirectoryUri1() {
			return soapDirectoryUri1;
		}
		public void setSoapDirectoryUri1(SoapDirectoryUri<T> soapDirectoryUri1) {
			this.soapDirectoryUri1 = soapDirectoryUri1;
		}
		public SoapDirectoryUri<T> getSoapDirectoryUri2() {
			return soapDirectoryUri2;
		}
		public void setSoapDirectoryUri2(SoapDirectoryUri<T> soapDirectoryUri2) {
			this.soapDirectoryUri2 = soapDirectoryUri2;
		}
	}
}
