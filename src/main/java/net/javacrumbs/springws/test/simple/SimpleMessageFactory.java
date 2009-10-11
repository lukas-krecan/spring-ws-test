package net.javacrumbs.springws.test.simple;

public class SimpleMessageFactory {
	
	private SimpleMessageFactory()
	{
		//nothing here
	}
	
	public static final SimpleValidatingFactory expectRequest(String resourceName) {
		return new SimpleValidatingFactory(resourceName, null);
	}

}
