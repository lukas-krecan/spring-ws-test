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
package net.javacrumbs.springws.test.validator;

import net.javacrumbs.springws.test.lookup.AbstractPayloadRootResourceLookupFactoryBean;

/**
 * Simplifies creation of {@link XmlCompareRequestValidator}.
 * @author Lukas Krecan
 *
 */
public class PayloadRootBasedXmlCompareRequestValidatorFactoryBean extends AbstractPayloadRootResourceLookupFactoryBean {

	
	private int order = XmlCompareRequestValidator.DEFAULT_ORDER;

	public PayloadRootBasedXmlCompareRequestValidatorFactoryBean() {
		setPathSuffix("request.xml");
	}

	@Override
	protected Object createInstance() throws Exception {
		XmlCompareRequestValidator responseGenerator = new XmlCompareRequestValidator();
		responseGenerator.setControlResourceLookup(getResourceLookup());
		return responseGenerator;
	}

	@Override
	public Class<?> getObjectType() {
		return XmlCompareRequestValidator.class;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}


}
