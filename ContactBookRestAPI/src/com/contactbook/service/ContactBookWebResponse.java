package com.contactbook.service;

import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlRootElement;

import com.contactbook.ContactBookRespose;

@XmlRootElement
public class ContactBookWebResponse {
			
		@JsonbProperty("rsp")
		public ContactBookRespose response;
		
		@JsonbProperty("msg")
		public String message;
}
