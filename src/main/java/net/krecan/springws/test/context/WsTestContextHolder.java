package net.krecan.springws.test.context;

public final class WsTestContextHolder {
	private WsTestContextHolder()
	{
		//nothing
	}
	
	private static final ThreadLocal<WsTestContext> testContext = new ThreadLocal<WsTestContext>()
	{
		@Override
		protected WsTestContext initialValue() {
			return new DefaultWsTestContext();
		}
	};
	
	public static WsTestContext getTestContext() {
		return testContext.get();
	}

}
