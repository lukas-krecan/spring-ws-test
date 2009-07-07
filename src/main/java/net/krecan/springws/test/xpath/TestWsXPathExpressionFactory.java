package net.krecan.springws.test.xpath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathVariableResolver;

import org.springframework.xml.namespace.SimpleNamespaceContext;
import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathException;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.springframework.xml.xpath.XPathParseException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Creates XPath expressions that are capable to do variable resolving. (It would be better to add this ability to the {@link XPathExpressionFactory}.
 * @author Lukas Krecan
 *
 */
public abstract class TestWsXPathExpressionFactory {
   private static XPathFactory xpathFactory = XPathFactory.newInstance();
	 /**
     * Creates a JAXP 1.3 <code>XPathExpression</code> from the given string expression and namespaces.
     *
     * @param expression the XPath expression
     * @param namespaces the namespaces
     * @return the compiled <code>XPathExpression</code>
     * @throws XPathParseException when the given expression cannot be parsed
     */
    public static XPathExpression createXPathExpression(String expression, Map namespaces, XPathVariableResolver resolver) {
        try {
            XPath xpath = xpathFactory.newXPath();
            SimpleNamespaceContext namespaceContext = new SimpleNamespaceContext();
            namespaceContext.setBindings(namespaces);
            xpath.setNamespaceContext(namespaceContext);
            if (resolver!=null)
            {
            	xpath.setXPathVariableResolver(resolver);
            }
            javax.xml.xpath.XPathExpression xpathExpression = xpath.compile(expression);
            return new Jaxp13XPathExpression(xpathExpression);
        }
        catch (XPathExpressionException ex) {
            throw new org.springframework.xml.xpath.XPathParseException(
                    "Could not compile [" + expression + "] to a XPathExpression: " + ex.getMessage(), ex);
        }
    }
    
    /** JAXP 1.3 implementation of the <code>XPathExpression</code> interface. */
    private static class Jaxp13XPathExpression implements XPathExpression {

        private final javax.xml.xpath.XPathExpression xpathExpression;

        private Jaxp13XPathExpression(javax.xml.xpath.XPathExpression xpathExpression) {
            this.xpathExpression = xpathExpression;
        }

        public String evaluateAsString(Node node) {
            return (String) evaluate(node, XPathConstants.STRING);
        }

        public List evaluateAsNodeList(Node node) {
            NodeList nodeList = (NodeList) evaluate(node, XPathConstants.NODESET);
            return toNodeList(nodeList);
        }

        private Object evaluate(Node node, QName returnType) {
            try {
                // XPathExpression is not thread-safe
                synchronized (xpathExpression) {
                    return xpathExpression.evaluate(node, returnType);
                }
            }
            catch (XPathExpressionException ex) {
                throw new XPathException("Could not evaluate XPath expression:" + ex.getMessage(), ex);
            }
        }

        private List toNodeList(NodeList nodeList) {
            List result = new ArrayList(nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {
                result.add(nodeList.item(i));
            }
            return result;
        }

        public double evaluateAsNumber(Node node) {
            Double result = (Double) evaluate(node, XPathConstants.NUMBER);
            return result.doubleValue();
        }

        public boolean evaluateAsBoolean(Node node) {
            Boolean result = (Boolean) evaluate(node, XPathConstants.BOOLEAN);
            return result.booleanValue();
        }

        public Node evaluateAsNode(Node node) {
            return (Node) evaluate(node, XPathConstants.NODE);
        }

        public Object evaluateAsObject(Node node, NodeMapper nodeMapper) throws XPathException {
            Node result = (Node) evaluate(node, XPathConstants.NODE);
            if (result != null) {
                try {
                    return nodeMapper.mapNode(result, 0);
                }
                catch (DOMException ex) {
                    throw new XPathException("Mapping resulted in DOMException", ex);
                }
            }
            else {
                return null;
            }
        }

        public List evaluate(Node node, NodeMapper nodeMapper) throws XPathException {
            NodeList nodes = (NodeList) evaluate(node, XPathConstants.NODESET);
            List results = new ArrayList(nodes.getLength());
            for (int i = 0; i < nodes.getLength(); i++) {
                try {
                    results.add(nodeMapper.mapNode(nodes.item(i), i));
                }
                catch (DOMException ex) {
                    throw new XPathException("Mapping resulted in DOMException", ex);
                }
            }
            return results;
        }
    }
}
