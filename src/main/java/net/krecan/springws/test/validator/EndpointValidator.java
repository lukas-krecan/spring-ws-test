package net.krecan.springws.test.validator;

import java.net.URI;
import java.util.Arrays;

import net.krecan.springws.test.RequestValidator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.server.EndpointMapping;
import org.springframework.ws.server.MessageDispatcher;
import org.springframework.ws.support.DefaultStrategiesHelper;

/**
 * Uses XML schema to validate a message.
 * @author Lukas Krecan
 *
 */
public class EndpointValidator implements RequestValidator, InitializingBean{
	private EndpointMapping[] endpointMappings;
	
	private WebServiceMessageFactory messageFactory;
	
	private MessageDispatcher messageDispatcher;
	
	public void validate(URI uri, WebServiceMessage message) {
		try {
			messageDispatcher.receive(new DefaultMessageContext(message, messageFactory));
		} catch (Exception e) {
			//TODO fixme
			throw new RuntimeException(e);
		}
	}

	public EndpointMapping[] getEndpointMappings() {
		return endpointMappings;
	}


	public void afterPropertiesSet() throws Exception {
        Resource resource = new ClassPathResource(ClassUtils.getShortName(WebServiceTemplate.class) + ".properties",  WebServiceTemplate.class);
		DefaultStrategiesHelper helper = new DefaultStrategiesHelper(resource);
		if (getMessageFactory()==null)
		{
			WebServiceMessageFactory messageFactory = (WebServiceMessageFactory) helper.getDefaultStrategy(WebServiceMessageFactory.class);
	        setMessageFactory(messageFactory);
		}
		if (messageDispatcher==null)
		{
			messageDispatcher = new MessageDispatcher();
			messageDispatcher.setEndpointMappings(Arrays.asList(endpointMappings));
		}
	}

	public void setEndpointMappings(EndpointMapping[] endpointMappings) {
		this.endpointMappings = endpointMappings;
	}
	
	public void setEndpointMapping(EndpointMapping endpointMapping) {
		this.endpointMappings = new EndpointMapping[]{endpointMapping};
	}

	public WebServiceMessageFactory getMessageFactory() {
		return messageFactory;
	}

	public void setMessageFactory(WebServiceMessageFactory messageFactory) {
		this.messageFactory = messageFactory;
	}

	public MessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}

	public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
		this.messageDispatcher = messageDispatcher;
	}

}
