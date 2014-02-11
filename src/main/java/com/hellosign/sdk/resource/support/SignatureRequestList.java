package com.hellosign.sdk.resource.support;

import org.json.JSONObject;

import com.hellosign.sdk.HelloSignException;
import com.hellosign.sdk.resource.AbstractResourceList;
import com.hellosign.sdk.resource.SignatureRequest;

/**
 * Represents a paged list of SignatureRequests returned from HelloSign.
 * 
 * @author "Chris Paul (chris@hellosign.com)"
 */
public class SignatureRequestList extends AbstractResourceList<SignatureRequest> {

	public SignatureRequestList(JSONObject json) throws HelloSignException {
		super(json, "signature_requests");
	}
}
