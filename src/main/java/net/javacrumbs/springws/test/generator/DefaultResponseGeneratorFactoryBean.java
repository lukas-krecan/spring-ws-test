/**
 * Copyright 2006 the original author or authors.
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
package net.javacrumbs.springws.test.generator;

import net.javacrumbs.springws.test.lookup.AbstractResourceLookupFactoryBean;




/**
 * Simplifies creation of {@link DefaultResponseGenerator}. Could be used like this
 *  <code><pre>
 * &lt;bean class="net.javacrumbs.springws.test.generator.DefaultResponseGeneratorFactoryBean"&gt;
 *	&lt;property name="namespaceMap"&gt;
 *		&lt;map&gt;
 *			&lt;entry key="soapenv" value="http://schemas.xmlsoap.org/soap/envelope/"/&gt;
 *			&lt;entry key="ns" value="http://www.springframework.org/spring-ws/samples/airline/schemas/messages"/&gt;
 *		&lt;/map&gt;
 *	&lt;/property&gt;
 *	&lt;property name="XPathExpressions"&gt;
 *		&lt;list&gt;
 *			&lt;value&gt;concat('mock-responses/',$uri.host, '/', local-name(//soapenv:Body/*[1]),'/',//ns:from,'-',//ns:to,'-response.xml')&lt;/value&gt;
 *			&lt;value&gt;concat('mock-responses/',$uri.host, '/', local-name(//soapenv:Body/*[1]),'/default-response.xml')&lt;/value&gt;
 *		&lt;/list&gt;
 *	&lt;/property&gt;		
 *&lt;/bean&gt;
 *	</pre>	
 * </code>
 * 	
 * @author Lukas Krecan
 *
 */
public class DefaultResponseGeneratorFactoryBean extends AbstractResourceLookupFactoryBean {

	private int order = DefaultResponseGenerator.DEFAULT_ORDER;

	@Override
	protected Object createInstance() throws Exception {
				
		DefaultResponseGenerator responseGenerator = new DefaultResponseGenerator();
		responseGenerator.setOrder(order);
		responseGenerator.setResourceLookup(getResourceLookup());
		
		return responseGenerator;
	}

	@Override
	public Class<?> getObjectType() {
		return DefaultResponseGenerator.class;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}


}
