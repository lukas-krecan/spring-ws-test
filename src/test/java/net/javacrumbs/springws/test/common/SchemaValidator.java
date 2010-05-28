package net.javacrumbs.springws.test.common;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.transform.Source;

import net.javacrumbs.springws.test.WsTestException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.validation.XmlValidator;
import org.springframework.xml.validation.XmlValidatorFactory;
import org.xml.sax.SAXParseException;

/**
 * Validates message using XML schema.
 * @author Lukas Krecan
 *
 */
public class SchemaValidator {
    protected final Log logger = LogFactory.getLog(getClass());
	
	public void validate(WebServiceMessage message, XmlValidator validator) throws IOException{
		Source requestSource = message.getPayloadSource();
        if (requestSource != null) {
            SAXParseException[] errors = validator.validate(requestSource);
            if (!ObjectUtils.isEmpty(errors)) {
                handleRequestValidationErrors(message, errors);
            }
            logger.debug("Request message validated");
        }
        else
        {
       	 logger.warn("Request source is null");
        }
	}
	
	/**
	 * Creates {@link XmlValidator} from schemas.
	 * @param schemas
	 * @param schemaLanguage
	 * @return
	 * @throws IOException
	 */
	public XmlValidator createValidatorFromSchemas(Resource[] schemas, String schemaLanguage) throws IOException
	{
		 Assert.hasLength(schemaLanguage, "schemaLanguage is required");
         for (int i = 0; i < schemas.length; i++) {
             Assert.isTrue(schemas[i].exists(), "schema [" + schemas[i] + "] does not exist");
         }
         if (logger.isInfoEnabled()) {
             logger.info("Validating using \"" + StringUtils.arrayToCommaDelimitedString(schemas)+"\"");
         }
         return XmlValidatorFactory.createValidator(schemas, schemaLanguage);
	}
	
	
    protected void handleRequestValidationErrors(WebServiceMessage message, SAXParseException[] errors) {
		throw new WsTestException("Request not valid. "+Arrays.toString(errors));
	}
}
