package com.contactbook;

import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Contact {
	
	@JsonbProperty("email")
	private String email;
	
	@JsonbProperty("name")
	private String name;
	
	public Contact() {		
	}
		
	public Contact(String email, String name) {
		this.email = email;
		this.name = name;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement
	public String getEmail() {
		return email;
	}
	
	@XmlElement
	public String getName() {
		return name;
	}	
}
