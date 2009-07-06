package net.krecan.springws.test.context;

import java.util.Map;

public interface WsTestContext {
	
	public void setAttribute(String name, Object value);
	
	public Object getAttribute(String attributeName);
	
	public Map<String, Object> getAttributeMap();

	public void clear();
}
