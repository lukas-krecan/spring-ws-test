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
package net.javacrumbs.springws.test.util;

import java.io.IOException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;

/**
 * Common XML manipulating code. 
 * @author Lukas Krecan
 *
 */
public interface XmlUtil {

	public abstract Document loadDocument(Source source);

	public abstract Document loadDocument(Resource resource) throws IOException;

	public abstract void transform(Source source, Result result);

	public abstract void transform(Source template, Source source, Result result);

	public abstract Document loadDocument(WebServiceMessage message);

	/**
	 * Returns envelope source.
	 * @param message
	 * @return
	 * @throws UnsupportedOperationException if message is not SoapMessage
	 */
	public abstract Source getEnvelopeSource(WebServiceMessage message);

	public abstract String serializeDocument(Source source);

	public abstract String serializeDocument(WebServiceMessage message);

	public abstract String serializeDocument(Document document);

	public abstract boolean isSoap(Document document);
}