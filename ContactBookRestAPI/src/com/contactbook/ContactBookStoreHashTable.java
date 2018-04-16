package com.contactbook;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ContactBookStoreHashTable implements IContactBookStore {

	Map<String, Contact> contactBookMap = new HashMap<String, Contact>();

	@Override
	public ContactBookRespose addNewContact(Contact contact) {

		if (contactBookMap.containsKey(contact.getEmail())) {
			Util.log(this, Level.INFO, "Duplicate request for add contact email :" + contact.getEmail());
			return ContactBookRespose.DUPLICATE;
		}

		contactBookMap.put(contact.getEmail(), contact);

		return ContactBookRespose.SUCCESS;
	}

	@Override
	public Contact getContact(String email) {
		return contactBookMap.get(email);
	}

	@Override
	public ContactBookRespose updateContact(Contact contact) {
		if (!contactBookMap.containsKey(contact.getEmail())) {
			Util.log(this, Level.INFO, "Contact to update not found email :" + contact.getEmail());
			return ContactBookRespose.NOT_FOUND;
		}

		contactBookMap.put(contact.getEmail(), contact);

		return ContactBookRespose.SUCCESS;
	}

	@Override
	public ContactBookRespose deleteContact(String email) {

		if (contactBookMap.remove(email) != null) {
			return ContactBookRespose.SUCCESS;
		}
		
		Util.log(this, Level.INFO, "Contact to delet not found email :" + email);
		return ContactBookRespose.NOT_FOUND;
	}

}
