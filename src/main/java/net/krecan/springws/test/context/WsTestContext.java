package net.krecan.springws.test.context;

import java.util.Map;

/**
 * Context that can be used for test specific data. Its attributes could be set in the test method and the 
 * values can be used in
 * <ul>
 * <li> XSLT transformation using {@link TemplateProcessingXPathResourceLookup}.</li>
 * <li> XPath resolution using {@link XPathResourceLookup} </li>
 * <li> Programatically</li>
 * </ul> 
 * @author Lukas Krecan
 *
 */
public interface WsTestContext {
	
	public void setAttribute(String name, Object value);
	
	public Object getAttribute(String attributeName);
	
	public Map<String, Object> getAttributeMap();

	public void clear();
}
