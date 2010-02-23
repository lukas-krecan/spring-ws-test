package net.javacrumbs.springws.test.server;

import javax.xml.transform.Source;

import org.springframework.ws.server.endpoint.PayloadEndpoint;

public class DummyEndpoint implements PayloadEndpoint {

	public Source invoke(Source request) throws Exception {
		return null;
	}

}
