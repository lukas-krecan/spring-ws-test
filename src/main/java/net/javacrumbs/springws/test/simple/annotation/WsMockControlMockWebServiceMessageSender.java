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
package net.javacrumbs.springws.test.simple.annotation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ws.transport.WebServiceConnection;

import net.javacrumbs.springws.test.AbstractMockWebServiceMessageSender;
import net.javacrumbs.springws.test.RequestProcessor;

/**
 * Message sender that creates {@link WebServiceConnection} from {@link ThreadLocalWsMockControlFactoryBean}s request processors.
 * @author Lukas Krecan
 *
 */
class WsMockControlMockWebServiceMessageSender extends AbstractMockWebServiceMessageSender {

	@Override
	protected List<RequestProcessor> getRequestProcessors() {
		return new ArrayList<RequestProcessor>(ThreadLocalWsMockControlFactoryBean.getWsMockControl().getRequestProcessors());
	}
}
