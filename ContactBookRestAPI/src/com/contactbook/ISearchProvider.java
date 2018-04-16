package com.contactbook;

/**
 * Search capability for stored contact data.
 */
public interface ISearchProvider {
	
	boolean addNewContact(Contact contact);
	
	boolean updateContact(Contact contact);
	
	boolean deleteContact(String email);
	
	SearchResult searchByEmail(String emailQueryString, int startIndex);
	
	SearchResult searchByName(String nameQueryString, int startIndex);
}
