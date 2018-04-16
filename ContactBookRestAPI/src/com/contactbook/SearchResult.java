package com.contactbook;

import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchResult {
	
	@JsonbProperty("cont")
	private List<Contact> contacts = new ArrayList<Contact>();
	
	@JsonbProperty("tc")
	private int totalCount;

	
	public List<Contact> getContacts() {
		return contacts;
	}
	
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalHits) {
		totalCount = totalHits;	
	}
	
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public void addContact(String  email, String name) {
		Contact contact = new Contact(email, name);
		contacts.add(contact);
	}

	public void addContact(Contact contact) {
		contacts.add(contact);		
	}
	
	
}
