package net.javacrumbs.springws.test.validator;



import net.javacrumbs.springws.test.WsTestException;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.w3c.dom.Document;

/**
 * Compares message with the control message.
 * 
 * @author Lukas Krecan
 * 
 */
public class XmlCompareRequestValidator extends AbstractCompareRequestValidator implements RequestValidator {

	/**
	 * Diff that ignores "${IGNORE}" placeholder
	 * @author Lukas Krecan
	 *
	 */
	private static final class IgnoringDiff extends Diff {
		private IgnoringDiff(Document controlDoc, Document testDoc) {
			super(controlDoc, testDoc);
		}

		public int differenceFound(Difference difference) {
			if ("${IGNORE}".equals(difference.getControlNodeDetail().getValue())) {
				return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
			} else {
				return super.differenceFound(difference);
			}
		}
	}

	/**
	 * Compares documents. If they are different throws {@link WsTestException}.
	 * @param controlDocument
	 * @param messageDocument
	 */
	@Override
	protected void compareDocuments(Document controlDocument, Document messageDocument) {
		Diff diff = createDiff(controlDocument, messageDocument);
		if (!diff.similar()) {
			throw new WsTestException("Message is different " + diff.toString());
		}
	}

	protected Diff createDiff(Document controlDocument, Document messageDocument) {
		return new IgnoringDiff(controlDocument, messageDocument);
	}

}
