package com.contactbook.service;

import javax.json.bind.annotation.JsonbProperty;

import com.contactbook.ContactBookRespose;
import com.contactbook.SearchResult;

public class ContactBookWebSearchResponse {
	
	public ContactBookWebSearchResponse() {
		searchResult = new SearchResult();
	}
	
	@JsonbProperty("rsp")
	public ContactBookRespose response;
	
	@JsonbProperty("msg")
	public String message;
	
	@JsonbProperty("sr")
	public SearchResult searchResult;

}
