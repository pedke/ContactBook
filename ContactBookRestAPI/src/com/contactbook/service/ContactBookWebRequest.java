package com.contactbook.service;

import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContactBookWebRequest {
	
	@JsonbProperty("email")
	public String email;
	
	@JsonbProperty("name")
	public String name;

}
