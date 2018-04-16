package com.contactbook.service;

import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContactBookWebSearchRequest {
			
		@JsonbProperty("sstr")
		public String searchString;
		
		@JsonbProperty("sin")
		public int startIndex;
}
