package com.contactbook;

import javax.inject.Singleton;

/**
 * ContactBook, responsible for storing and searching contacts.
 * This in for single user only, defined as singleton
 * however we can modify to create multiple ContactBooks for multiple users  
 * For that we need to define another wrapper to store multiple ContactBooks
 * 
 * Uses two interfaces 
 * 	IContactBookStore : to store contact book data
 *  ISearchProvider : Provides search capability, particularly fuzzy search  
 * 
 */

@Singleton
public class ContactBook {

	IContactBookStore contactBookStore;
	ISearchProvider searchProvider;
	
	private static ContactBook contactBook = new ContactBook();
	
	public static ContactBook getInstance() {
		return contactBook;
	}
	
	/**
	 * This is used for testing purpose only, used in unit test only.
	 */
	public static void resetInstance() {
		contactBook = new ContactBook();
	}

	private ContactBook() {
		
		contactBookStore = new ContactBookStoreHashTable();
		searchProvider = new LuceneSearchProvider();
		
		}

	public ContactBookRespose addNewContact(Contact contact) {

		ContactBookRespose respose;

		if (!Util.isValidContact(contact)) {
			respose = ContactBookRespose.INVALID_INPUT;
		} else {
			respose = contactBookStore.addNewContact(contact);
			
			if(!searchProvider.addNewContact(contact)) {
				respose =  ContactBookRespose.SEARCH_UPDATE_ERROR;
			}
		}

		return respose;
	}

	public ContactBookRespose updateContact(Contact contact) {
		
		ContactBookRespose respose;

		if (!Util.isValidContact(contact)) {
			return ContactBookRespose.INVALID_INPUT;
		} else {
			respose = contactBookStore.updateContact(contact);
			if(!searchProvider.updateContact(contact)) {
				respose =  ContactBookRespose.SEARCH_UPDATE_ERROR;
			}
		}
		
		return respose;
	}
	
	public Contact getContact(String email) {

		if (!Util.isValidEmail(email)) {
			return null;
		}
		
		return contactBookStore.getContact(email);
	}
	
	public ContactBookRespose deleteContact(String email) {
		
		ContactBookRespose respose;

		if (!Util.isValidEmail(email)) {
			return ContactBookRespose.INVALID_INPUT;
		} else {
			respose = contactBookStore.deleteContact(email);
			if(!searchProvider.deleteContact(email)) {
				respose =  ContactBookRespose.SEARCH_UPDATE_ERROR;
			}
		}
		
		return respose;
	}
	
	public SearchResult searchByEmail(String email, int startIndex) {
		return searchProvider.searchByEmail(email, startIndex);
	}
	
	public SearchResult searchByName(String name, int startIndex) {
		return searchProvider.searchByName(name, startIndex);
	}
}