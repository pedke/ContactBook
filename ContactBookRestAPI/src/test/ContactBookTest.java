package test;

import com.contactbook.Contact;
import com.contactbook.ContactBook;
import com.contactbook.ContactBookRespose;
import com.contactbook.SearchResult;

import junit.framework.TestCase;

public class ContactBookTest extends TestCase {
	
	public void setUp() {
		ContactBook.resetInstance();
	}
	
	public void testAddNewContact() {

		ContactBook contactBook = ContactBook.getInstance();

		ContactBookRespose response = contactBook.addNewContact(new Contact("john_snow@gmail.com", "John Snow"));
		assertEquals(ContactBookRespose.SUCCESS, response);

		Contact contact = contactBook.getContact("john_snow@gmail.com");
		assertNotNull(contact);
		assertEquals("john_snow@gmail.com", contact.getEmail());
		assertEquals("John Snow", contact.getName());
	}

	public void testUpdateContact() {
		ContactBook contactBook = ContactBook.getInstance();

		contactBook.addNewContact(new Contact("john_snow@gmail.com", "John Snow"));
		
		ContactBookRespose response = contactBook.updateContact
										(new Contact("john_snow@gmail.com", "Kit Harington"));
		assertEquals(ContactBookRespose.SUCCESS, response);

		Contact contact = contactBook.getContact("john_snow@gmail.com");
		assertNotNull(contact);
		assertEquals("john_snow@gmail.com", contact.getEmail());
		assertEquals("Kit Harington", contact.getName());
	}

	public void testDeleteContact() {
		
		ContactBook contactBook = ContactBook.getInstance();

		contactBook.addNewContact(new Contact("john_snow@gmail.com", "John Snow"));
		
		ContactBookRespose response = contactBook.deleteContact("john_snow@gmail.com");
		assertEquals(ContactBookRespose.SUCCESS, response);

		Contact contact = contactBook.getContact("john_snow@gmail.com");
		assertNull(contact);
	}
	
	public void testSearchContactByEmail() {
		
		ContactBook contactBook = ContactBook.getInstance();

		contactBook.addNewContact(new Contact("john_snow@gmail.com", "John Snow"));
		
		SearchResult searchResult = contactBook.searchByEmail("john_snow@gmail.com", 0);
		assertEquals(1, searchResult.getTotalCount());
		assertEquals(1, searchResult.getContacts().size());
		assertEquals("john_snow@gmail.com", searchResult.getContacts().get(0).getEmail());
		assertEquals("John Snow", searchResult.getContacts().get(0).getName());
	}
	
	public void testSearchContactByName() {
		
		ContactBook contactBook = ContactBook.getInstance();

		contactBook.addNewContact(new Contact("john_snow@gmail.com", "John Snow"));
		
		SearchResult searchResult = contactBook.searchByName("John Snow", 0);
		assertEquals(1, searchResult.getTotalCount());
		assertEquals(1, searchResult.getContacts().size());
		assertEquals("john_snow@gmail.com", searchResult.getContacts().get(0).getEmail());
		assertEquals("John Snow", searchResult.getContacts().get(0).getName());
	}

}
