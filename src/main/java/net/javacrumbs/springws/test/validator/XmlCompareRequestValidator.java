/**
 * Copyright 2009-2010 the original author or authors.
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



import net.javacrumbs.springws.test.WsTestException;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.springframework.core.Ordered;
import org.w3c.dom.Document;

/**
 * Compares message with the control message.
 * 
 * @author Lukas Krecan
 * 
 */
public class XmlCompareRequestValidator extends AbstractCompareRequestValidator implements Ordered
{
	
	static final int DEFAULT_ORDER = 10;
	
	private int order = DEFAULT_ORDER;
	
	private boolean ignoreWhitespace = true; 
	
	/**
	 * Compares documents. If they are different throws {@link WsTestException}.
	 * @param controlDocument
	 * @param messageDocument
	 */
	@Override
	protected void compareDocuments(Document controlDocument, Document messageDocument) {
		if (logger.isTraceEnabled())
		{
			logger.trace("Comparing \""+serializeDocument(controlDocument)+" \"\n with \n\""+serializeDocument(messageDocument)+"\"");
		}
		Diff diff = createDiff(controlDocument, messageDocument);
		if (!diff.similar()) {
			logger.debug("Messages different, throwing exception");
			throw new WsTestException("Message is different " + diff.toString());
		}
	}


	protected Diff createDiff(Document controlDocument, Document messageDocument) {
		if (ignoreWhitespace != XMLUnit.getIgnoreWhitespace())
		{
			XMLUnit.setIgnoreWhitespace(ignoreWhitespace);
		}
		return new EnhancedDiff(controlDocument, messageDocument);
	}


	public int getOrder() {
		return order;
	}


	public void setOrder(int order) {
		this.order = order;
	}


	public boolean isIgnoreWhitespace() {
		return ignoreWhitespace;
	}


	public void setIgnoreWhitespace(boolean ignoreWhitespace) {
		this.ignoreWhitespace = ignoreWhitespace;
	}

}
