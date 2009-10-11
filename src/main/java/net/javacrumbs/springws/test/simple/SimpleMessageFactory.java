package net.javacrumbs.springws.test.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.ResponseGenerator;

import org.springframework.ws.transport.WebServiceMessageSender;

public class SimpleMessageFactory {
	private final SimpleMessageFactory previousFactory;
	
	public SimpleMessageFactory()
	{
		this(null);
	}

	protected SimpleMessageFactory(SimpleMessageFactory previousFactory)
	{
		this.previousFactory = previousFactory;
	}
	
	
	private WebServiceMessageSender create() {
		MockWebServiceMessageSender messageSender = new MockWebServiceMessageSender();
		messageSender.setResponseGenerators(getResponseGenerators());
		return messageSender;
	}

	private List<ResponseGenerator> getResponseGenerators() {
		ResponseGenerator generator = createResponseGenerator();
		if (previousFactory!=null)
		{
			List<ResponseGenerator> result = new ArrayList<ResponseGenerator>(previousFactory.getResponseGenerators());
			if (generator!=null)
			{
				result.add(generator);
			}
			return result;
		}
		else
		{
			return generator!=null?Collections.singletonList(generator):Collections.<ResponseGenerator>emptyList();
		}
	}

	/**
	 * To be overerriden.
	 * @return
	 */
	protected ResponseGenerator createResponseGenerator() {
		return null;
	}

	public SimpleMessageFactory expectRequest(String resourceName) {
		return new SimpleCompareRequestValidatorFactory(resourceName, this);
	}

	public WebServiceMessageSender andReturnResponse(String resourceName) {
		SimpleMessageFactory lastFactory = new SimpleResponseGeneratorFactory(resourceName, this);
		return lastFactory.create();
	}
	
	

}
