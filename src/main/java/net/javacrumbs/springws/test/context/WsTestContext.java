/**
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.springws.test.context;

import java.util.Map;
import java.util.Properties;

/**
 * Context that can be used for test specific data. Its attributes could be set in the test method and the 
 * values can be used in the
 * <ul>
 * <li> XSLT transformation using {@link TemplateProcessingXPathResourceLookup}.</li>
 * <li> XPath resolution using {@link XPathResourceLookup} </li>
 * <li> Programatically</li>
 * </ul> 
 * @author Lukas Krecan
 *
 */
public interface WsTestContext {
	
	/**
	 * Sets the attribute
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name, Object value);
	
	/**
	 * Gets attribute value
	 * @param attributeName
	 * @return
	 */
	public Object getAttribute(String attributeName);
	
	/**
	 * Returns map of the attributes
	 * @return
	 */
	public Map<String, Object> getAttributeMap();
	
	/**
	 * Sets attribute values.
	 * @param attributes
	 */
	public void setAttributes(Map<String, Object> attributes);

	/**
	 * Sets attribute values.
	 * @param attributes
	 */
	public void setAttributes(Properties attributes);
	

	/**
	 * Clear all attributes.
	 */
	public void clear();
}
