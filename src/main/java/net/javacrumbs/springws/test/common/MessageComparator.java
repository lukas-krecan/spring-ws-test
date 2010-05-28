package net.javacrumbs.springws.test.common;

import java.io.IOException;

import net.javacrumbs.springws.test.util.XmlUtil;

import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;

public interface MessageComparator {

	public abstract void compareMessage(WebServiceMessage message, Resource controlResource) throws IOException;

	public abstract void setXmlUtil(XmlUtil xmlUtil);

	public abstract void setIgnoreWhitespace(boolean ignoreWhitespace);

	public abstract boolean isIgnoreWhitespace();
	
}