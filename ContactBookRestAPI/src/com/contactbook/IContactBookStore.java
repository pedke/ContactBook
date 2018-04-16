package com.contactbook;

/**
 * Storage for contact book data
 *
 */
public interface IContactBookStore {
	
	ContactBookRespose addNewContact(Contact contact);
	
	Contact getContact(String email);
	
	ContactBookRespose updateContact(Contact contact);
	
	ContactBookRespose deleteContact(String email);

}
