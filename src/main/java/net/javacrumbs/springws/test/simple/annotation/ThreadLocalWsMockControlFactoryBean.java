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
package net.javacrumbs.springws.test.simple.annotation;

import net.javacrumbs.springws.test.simple.WsMockControl;

import org.springframework.beans.factory.FactoryBean;


/**
 * Factory bean that keep WsMockControl in {@link ThreadLocal}. This way we make sure that parallel execution of tests
 * works. 
 * @author Lukas Krecan
 *
 */
class ThreadLocalWsMockControlFactoryBean implements FactoryBean {

	private static final ThreadLocal<WsMockControl> wsMockControl = new ThreadLocal<WsMockControl>()
	{
		protected WsMockControl initialValue() {
			return createNew();			
		};
	};
		
	public Object getObject() throws Exception {
		return getWsMockControl();
	}

	public Class<?> getObjectType() {
		return WsMockControl.class;
	}

	public boolean isSingleton() {
		return false;
	}
	
	public static WsMockControl getWsMockControl() {
		return wsMockControl.get();
	}
	public static void clean() {
		 wsMockControl.set(createNew());
	}

	private static WsMockControl createNew() {
		return new WsMockControl();
	}
}
