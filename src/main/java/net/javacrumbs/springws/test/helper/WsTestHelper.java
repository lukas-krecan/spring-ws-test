package net.javacrumbs.springws.test.helper;

import java.io.IOException;

import net.javacrumbs.springws.test.util.XmlUtil;

import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.WebServiceMessageReceiver;

/**
 * Helps with implementing WS server side tests. The most common usage is 
 * <pre><code>
 * &#064;RunWith(SpringJUnit4ClassRunner.class)
 * // your endpoint configuration + Default helper config
 * &#064;ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring-ws-servlet.xml",
 * 		WsTestHelper.DEFAULT_CONFIG_PATH })
 * public class EndpointTest {
 * 
 * 	&#064;Autowired
 * 	private WsTestHelper helper;
 * 
 * 	&#064;Test
 * 	public void testSimple() throws Exception {
 * 		// simulates request coming to MessageDispatcherServlet
 * 		MessageContext message = helper.receiveMessage("request1.xml");
 * 		// assert that response is not fault
 * 		helper.createMessageValidator(message.getResponse()).assertNotSoapFault();
 *  }
 * }
 * </code></pre>
 * @author Lukas Krecan
 *
 */
public interface WsTestHelper {
	public static final String DEFAULT_CONFIG_PATH = "classpath:net.javacrumbs.springws.test.helper/default-helper-config.xml";
	/**
	 * Creates a {@link MessageContext} from the message and calls  {@link WebServiceMessageReceiver#receive(MessageContext)}
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public abstract MessageContext receiveMessage(WebServiceMessage message) throws Exception;

	/**
	 * Creates a {@link MessageContext} from the resource and calls  {@link WebServiceMessageReceiver#receive(MessageContext)}
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//TODO rename
	public abstract MessageContext receiveMessage(Resource request) throws Exception;

	/**
	 * Creates a {@link MessageContext} from the resource on the requestPath and calls  {@link WebServiceMessageReceiver#receive(MessageContext)}
	 * @param requestPath
	 * @return
	 * @throws Exception
	 */
	//TODO rename
	public abstract MessageContext receiveMessage(String requestPath) throws Exception;

	/**
	 * Loads message from given resource. Does template processing.
	 * @param resource
	 * @return
	 * @throws IOException
	 */
	public abstract WebServiceMessage loadMessage(Resource resource) throws IOException;

	/**
	 * Loads message from given resource. Does template processing.
	 * @param resource
	 * @return
	 * @throws IOException
	 */
	public abstract WebServiceMessage loadMessage(String resourcePath) throws IOException;

	/**
	 * Creates a message validator. Applies configuration taken from {@link DefaultWsTestHelper} configuration.
	 * @param message
	 * @return
	 */
	public abstract MessageValidator createMessageValidator(WebServiceMessage message);

	public abstract WebServiceMessageReceiver getWebServiceMessageReceiver();

	public abstract WebServiceMessageFactory getMessageFactory();

	public abstract XmlUtil getXmlUtil();
}