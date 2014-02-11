package com.hellosign.sdk.test;

import static org.junit.Assert.*;

import java.net.HttpURLConnection;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hellosign.sdk.AbstractHelloSignTest;
import com.hellosign.sdk.HelloSignClient;
import com.hellosign.sdk.resource.EmbeddedRequest;
import com.hellosign.sdk.resource.SignatureRequest;
import com.hellosign.sdk.resource.Template;
import com.hellosign.sdk.resource.TemplateSignatureRequest;

/**
 * Exercise the EmbeddedRequest wrapper.
 * 
 * @author "Chris Paul (chris@hellosign.com)"
 */
public class EmbeddedTemplateSignatureRequestTest extends AbstractHelloSignTest {
	
	private static final Logger logger = LoggerFactory.getLogger(EmbeddedTemplateSignatureRequestTest.class);
	
	@Test
	public void testEmbeddedTemplateRequestCreate() throws Exception {
		if (!isHelloSignAvailable()) {
			logger.debug("No API access, skipping tests...");
			return;
		}
		if (templateId == null || templateId.equals("")) {
			logger.debug("No template ID provided, skipping tests...");
			return;
		}
    	// Retrieve user's templates
    	HelloSignClient client = new HelloSignClient(auth);
    	Template template = client.getTemplate(templateId);
    	// assertNotNull(template);
    	// Instead of failing, we'll just skip. The user may not want to set up the template for testing.
    	if (template == null || template.getId() == null) {
    		logger.debug("\tNo template found, skipping test.");
    		return;
    	}
    	
    	// Create the signature request based on template
    	logger.debug("Creating signature request with template ID: " + template.getId());
    	TemplateSignatureRequest request = new TemplateSignatureRequest();
    	request.setTemplateId(template.getId());
    	request.setTestMode(true);
    	request.setTitle("Embedded Purchase Order");
    	request.setSubject("Congratulations on your purchase");
    	request.setMessage("Glad we could come to an agreement. Please sign when you have a chance.");
    	request.setSigner("Client", "katie@test.com", "Katie");
    	request.setCC("Accounting", "accounting@hellosign.com");
    	request.setCustomFieldValue("Cost", "$50,000");
    	logger.debug("\tSuccess!");

    	// Create the embedded request
    	logger.debug("Testing creation of Embedded Template Signature Request...");
    	EmbeddedRequest embeddedReq = new EmbeddedRequest(clientId, request);
    	assertNotNull(embeddedReq);
    	assertTrue(areFieldsEqual(getExpectedFields(), embeddedReq.getPostFields()));
    	logger.debug("\tSuccess!");

    	// Just pass the request through the HelloSignClient since we are already
		// testing the request output for SignatureRequest/TemplateSignatureRequest
		// in their respective test classes.
    	logger.debug("Creating Embedded Tempalte Signature Request...");
    	SignatureRequest response = client.createEmbeddedRequest(embeddedReq);
    	assertNotNull(response);
    	assertTrue(response.hasId());
    	logger.debug("\tSuccess!");
    	
    	// Cancel the signature request
    	logger.debug("Cancelling signature request...");
    	assertTrue(HttpURLConnection.HTTP_OK == client.cancelSignatureRequest(response.getId()));
    	logger.debug("\tSuccess!");
	}
}
