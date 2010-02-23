package net.javacrumbs.springws.test.helper;

import javax.xml.transform.Source;

import org.springframework.ws.server.endpoint.PayloadEndpoint;
import org.springframework.xml.transform.StringSource;

public class DummyEndpoint implements PayloadEndpoint {

	public Source invoke(Source request) throws Exception {
		return new StringSource("<test xmlns=\"http://www.example.org/schema\"><number>0</number><text>text</text></test>");
	}

}
