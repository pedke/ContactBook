package test;

import com.contactbook.Constants;
import com.contactbook.Contact;
import com.contactbook.LuceneSearchProvider;
import com.contactbook.SearchResult;

import junit.framework.TestCase;

public class LuceneSearchProviderTest extends TestCase {

	public void testAddNewContact() {

		LuceneSearchProvider searchProvider = new LuceneSearchProvider();
		SearchResult searchResult;

		searchProvider.addNewContact(new Contact("john_snow@gmail.com", "John Snow"));

		searchResult = searchProvider.searchByEmail("john_snow@gmail.com", 0);
		assertNotNull(searchResult);
		assertEquals(1, searchResult.getTotalCount());
		assertNotNull(searchResult.getContacts());
		assertEquals("john_snow@gmail.com", searchResult.getContacts().get(0).getEmail());
		assertEquals("John Snow", searchResult.getContacts().get(0).getName());

		searchResult = searchProvider.searchByName("John Snow", 0);
		assertNotNull(searchResult);
		assertEquals(1, searchResult.getTotalCount());
		assertNotNull(searchResult.getContacts());
		assertEquals("john_snow@gmail.com", searchResult.getContacts().get(0).getEmail());
		assertEquals("John Snow", searchResult.getContacts().get(0).getName());
	}

	public void testUpdateContact() {

		LuceneSearchProvider searchProvider = new LuceneSearchProvider();

		searchProvider.addNewContact(new Contact("john_snow@gmail.com", "John Snow"));
		searchProvider.updateContact(new Contact("john_snow@gmail.com", "Arya Stark"));

		SearchResult searchResult = searchProvider.searchByName("Arya Stark", 0);
		assertNotNull(searchResult);
		assertEquals(1, searchResult.getTotalCount());
		assertNotNull(searchResult.getContacts());
		assertEquals("john_snow@gmail.com", searchResult.getContacts().get(0).getEmail());
		assertEquals("Arya Stark", searchResult.getContacts().get(0).getName());

		searchResult = searchProvider.searchByName("John Snow", 0);
		assertNotNull(searchResult);
		assertEquals(0, searchResult.getTotalCount());
	}

	public void testDeleteContact() {

		LuceneSearchProvider searchProvider = new LuceneSearchProvider();
		searchProvider.addNewContact(new Contact("john_snow@gmail.com", "John Snow"));

		SearchResult searchResult = searchProvider.searchByName("John Snow", 0);
		assertNotNull(searchResult);
		assertEquals(1, searchResult.getTotalCount());
		assertNotNull(searchResult.getContacts());
		assertEquals("john_snow@gmail.com", searchResult.getContacts().get(0).getEmail());
		assertEquals("John Snow", searchResult.getContacts().get(0).getName());

		searchProvider.deleteContact("john_snow@gmail.com");

		searchResult = searchProvider.searchByName("John Snow", 0);
		assertNotNull(searchResult);
		assertEquals(0, searchResult.getTotalCount());
	}

	public void testFuzzyEmailSearch() {

		LuceneSearchProvider searchProvider = new LuceneSearchProvider();
		SearchResult searchResult;
		
		searchProvider.addNewContact(new Contact("john_snow@gmail.com", "John Snow"));

		searchResult = searchProvider.searchByEmail("john_snow", 0);
		assertNotNull(searchResult);
		assertEquals(1, searchResult.getTotalCount());
		assertNotNull(searchResult.getContacts());
		assertEquals("john_snow@gmail.com", searchResult.getContacts().get(0).getEmail());
		assertEquals("John Snow", searchResult.getContacts().get(0).getName());
		
		searchResult = searchProvider.searchByEmail("gmail.com", 0);
		assertNotNull(searchResult);
		assertEquals(1, searchResult.getTotalCount());
		assertNotNull(searchResult.getContacts());
		assertEquals("john_snow@gmail.com", searchResult.getContacts().get(0).getEmail());
		assertEquals("John Snow", searchResult.getContacts().get(0).getName());
	}

	public void testFuzzyNameSearch() {

		LuceneSearchProvider searchProvider = new LuceneSearchProvider();
		SearchResult searchResult;
		
		searchProvider.addNewContact(new Contact("john_snow@gmail.com", "John Snow"));

		searchResult = searchProvider.searchByName("Jon sno", 0);
		assertNotNull(searchResult);
		assertEquals(1, searchResult.getTotalCount());
		assertNotNull(searchResult.getContacts());
		assertEquals("john_snow@gmail.com", searchResult.getContacts().get(0).getEmail());
		assertEquals("John Snow", searchResult.getContacts().get(0).getName());
		
		searchResult = searchProvider.searchByName("John", 0);
		assertNotNull(searchResult);
		assertEquals(1, searchResult.getTotalCount());
		assertNotNull(searchResult.getContacts());
		assertEquals("john_snow@gmail.com", searchResult.getContacts().get(0).getEmail());
		assertEquals("John Snow", searchResult.getContacts().get(0).getName());
		
		searchResult = searchProvider.searchByName("Snow", 0);
		assertNotNull(searchResult);
		assertEquals(1, searchResult.getTotalCount());
		assertNotNull(searchResult.getContacts());
		assertEquals("john_snow@gmail.com", searchResult.getContacts().get(0).getEmail());
		assertEquals("John Snow", searchResult.getContacts().get(0).getName());
	}
	
	public void testPaginationSearch() {

		LuceneSearchProvider searchProvider = new LuceneSearchProvider();
		SearchResult searchResult;
		
		String firstName = "John";
		String lastName = "Snow";
		
		String email, name;
				
		for(int i = 0; i < ((2*Constants.SEARCH_RESULTS_PER_PAGE) + 2); i++) {
			email = firstName + "_" + lastName + "_" + i + "@yahoo.com";
			name = firstName + " " + lastName ;			
			searchProvider.addNewContact(new Contact(email, name));
		}
		
		searchResult = searchProvider.searchByName("John Snow", 0);
		assertNotNull(searchResult);
		assertEquals(Constants.SEARCH_RESULTS_PER_PAGE, searchResult.getContacts().size());
		
		searchResult = searchProvider.searchByName("John Snow", Constants.SEARCH_RESULTS_PER_PAGE);
		assertNotNull(searchResult);
		assertEquals(Constants.SEARCH_RESULTS_PER_PAGE, searchResult.getContacts().size());
		
		searchResult = searchProvider.searchByName("John Snow", (2*Constants.SEARCH_RESULTS_PER_PAGE));
		assertNotNull(searchResult);
		assertEquals(2, searchResult.getContacts().size());
	}

}
