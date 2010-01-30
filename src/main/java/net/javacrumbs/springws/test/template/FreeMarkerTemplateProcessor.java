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
package net.javacrumbs.springws.test.template;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.javacrumbs.springws.test.context.WsTestContextHolder;
import net.javacrumbs.springws.test.util.DefaultXmlUtil;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.ws.WebServiceMessage;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Processes a freemarker template.
 * @author Lukas Krecan
 *
 */
public class FreeMarkerTemplateProcessor implements TemplateProcessor, InitializingBean, ResourceLoaderAware {
	private FreeMarkerConfigurationFactory configurationFactory;	
	
	private ResourceLoader resourceLoader;
	
	private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();
	
	/**
	 * Simple template loader able to process absolute resource URIs. 
	 * @author Lukas Krecan
	 *
	 */
	private static final class SimpleTemplateLoader implements TemplateLoader
	{
		private ResourceLoader resourceLoader;

		public SimpleTemplateLoader(ResourceLoader resourceLoader) {
			this.resourceLoader = resourceLoader;
		}

		public Object findTemplateSource(String name) throws IOException {
			Resource resource = this.resourceLoader.getResource(name);
			return (resource.exists() ? resource : null);
		}

		public long getLastModified(Object templateSource) {
			return -1;
		}

		public Reader getReader(Object templateSource, String encoding) throws IOException {
			Resource resource = (Resource) templateSource;
			return new InputStreamReader(resource.getInputStream(), encoding);
		}
		
		public void closeTemplateSource(Object templateSource) throws IOException {
			
		}
	}

	public Resource processTemplate(Resource resource, URI uri, WebServiceMessage message) throws IOException {
		try {
			Configuration configuration = configurationFactory.createConfiguration();
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.putAll(WsTestContextHolder.getTestContext().getAttributeMap());
			properties.put("request", getXmlUtil().loadDocument(message));
			properties.put("IGNORE", "${IGNORE}");
			
			StringWriter out = new StringWriter();
			configuration.getTemplate(resource.getURL().toString()).process(properties, out);
			return new ByteArrayResource(out.toString().getBytes("UTF-8"));
		} catch (TemplateException e) {
			throw new IllegalStateException("FreeMarker exception",e);
		}
	}

	public void afterPropertiesSet(){
		if (configurationFactory==null)
		{
			configurationFactory = new FreeMarkerConfigurationFactory();
			Properties settings = new Properties();
			settings.put(Configuration.LOCALIZED_LOOKUP_KEY, "false");
			configurationFactory.setFreemarkerSettings(settings);
			configurationFactory.setPreTemplateLoaders(new TemplateLoader[]{new SimpleTemplateLoader(resourceLoader)});
		}
		
	}

	public FreeMarkerConfigurationFactory getConfigurationFactory() {
		return configurationFactory;
	}

	public void setConfigurationFactory(FreeMarkerConfigurationFactory configurationFactory) {
		this.configurationFactory = configurationFactory;
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public XmlUtil getXmlUtil() {
		return xmlUtil;
	}

	public void setXmlUtil(XmlUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
	}

}
