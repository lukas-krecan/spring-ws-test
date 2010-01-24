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
