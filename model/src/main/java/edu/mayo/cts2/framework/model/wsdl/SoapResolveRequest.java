package edu.mayo.cts2.framework.model.wsdl;

import edu.mayo.cts2.framework.model.service.core.QueryControl;
import edu.mayo.cts2.framework.model.service.core.ReadContext;

/**
 * Created with IntelliJ IDEA.
 * User: m005256
 * Date: 1/31/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SoapResolveRequest {

    public ReadContext getContext();

    public String getDirectory();

    public QueryControl getQueryControl();
}
