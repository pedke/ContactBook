package test;

import com.contactbook.Contact;
import com.contactbook.ContactBookRespose;
import com.contactbook.ContactBookStoreHashTable;

import junit.framework.TestCase;

public class ContactBookStoreHashTableTest extends TestCase {

	public void testAddNewContact() {

		ContactBookStoreHashTable contactBookStore = new ContactBookStoreHashTable();

		ContactBookRespose response = contactBookStore.addNewContact(new Contact("john_snow@gmail.com", "John Snow"));
		assertEquals(ContactBookRespose.SUCCESS, response);

		Contact contact = contactBookStore.getContact("john_snow@gmail.com");
		assertNotNull(contact);
		assertEquals("john_snow@gmail.com", contact.getEmail());
		assertEquals("John Snow", contact.getName());
	}

	public void testUpdateContact() {
		ContactBookStoreHashTable contactBookStore = new ContactBookStoreHashTable();

		contactBookStore.addNewContact(new Contact("john_snow@gmail.com", "John Snow"));
		
		ContactBookRespose response = contactBookStore.updateContact
										(new Contact("john_snow@gmail.com", "Kit Harington"));
		assertEquals(ContactBookRespose.SUCCESS, response);

		Contact contact = contactBookStore.getContact("john_snow@gmail.com");
		assertNotNull(contact);
		assertEquals("john_snow@gmail.com", contact.getEmail());
		assertEquals("Kit Harington", contact.getName());
	}

	public void testDeleteContact() {
		ContactBookStoreHashTable contactBookStore = new ContactBookStoreHashTable();

		contactBookStore.addNewContact(new Contact("john_snow@gmail.com", "John Snow"));
		
		ContactBookRespose response = contactBookStore.deleteContact("john_snow@gmail.com");
		assertEquals(ContactBookRespose.SUCCESS, response);

		Contact contact = contactBookStore.getContact("john_snow@gmail.com");
		assertNull(contact);
	}	
}
