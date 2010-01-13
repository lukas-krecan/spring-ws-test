package net.javacrumbs.springws.test.simple;

import java.util.ArrayList;
import java.util.List;

import net.javacrumbs.springws.test.AbstractMockWebServiceMessageSender;
import net.javacrumbs.springws.test.RequestProcessor;

public class WsMockControlMockWebServiceMessageSender extends AbstractMockWebServiceMessageSender {

	private final WsMockControl wsMockControl;
	
	public WsMockControlMockWebServiceMessageSender(WsMockControl wsMockControl) {
		super();
		this.wsMockControl = wsMockControl;
	}

	@Override
	protected List<RequestProcessor> getRequestProcessors() {
		return new ArrayList<RequestProcessor>(wsMockControl.getRequestProcessors());
	}

}
