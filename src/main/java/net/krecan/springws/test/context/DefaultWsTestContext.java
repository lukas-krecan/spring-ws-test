package net.krecan.springws.test.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Default WS test context implementation.
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

}
