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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Default {@link WsTestContext} implementation.
 * @author Lukas Krecan
 *
 */
class DefaultWsTestContext implements WsTestContext {
	private Map<String, Object> attributeMap = new HashMap<String, Object>();
	
	public Object getAttribute(String attributeName) {
		return attributeMap.get(attributeName);
	}

	public void setAttribute(String name, Object value) {
		attributeMap.put(name, value);

	}
	
	public Map<String, Object> getAttributeMap() {
		return new HashMap<String, Object>(attributeMap);
	}

	public void clear() {
		attributeMap.clear();
	}

	public void setAttributes(Map<String, Object> attributes)  {
		attributeMap.putAll(attributes);
	}

	public void setAttributes(Properties attributes) {
		for(String key : attributes.stringPropertyNames()) {
			attributeMap.put(key, attributes.getProperty(key));
		}
	}

}
