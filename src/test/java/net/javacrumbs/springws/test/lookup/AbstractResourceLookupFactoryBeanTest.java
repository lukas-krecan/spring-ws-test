package net.javacrumbs.springws.test.lookup;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;


public class AbstractResourceLookupFactoryBeanTest {
	@Test
	public void testSplit()
	{
		AbstractResourceLookupFactoryBean factoryBean = new AbstractResourceLookupFactoryBean(){
			@Override
			protected Object createInstance() throws Exception {
				return null;
			}

			@Override
			public Class<?> getObjectType() {
				return null;
			}
		};
		factoryBean.setDiscriminators(Collections.singletonMap("test", "//ns:text	; //ns:number"));
		assertArrayEquals(new String[]{"//ns:text","//ns:number"}, factoryBean.getDiscriminatorsMap().get("test"));
		
		factoryBean.setDiscriminators(Collections.singletonMap("test", "//ns:text;//ns:number"));
		assertArrayEquals(new String[]{"//ns:text","//ns:number"}, factoryBean.getDiscriminatorsMap().get("test"));
	}
}
