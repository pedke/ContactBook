package test;

import com.contactbook.ContactBook;
import com.contactbook.ContactBookRespose;
import com.contactbook.service.ContactBookService;
import com.contactbook.service.ContactBookWebRequest;
import com.contactbook.service.ContactBookWebResponse;
import com.contactbook.service.ContactBookWebSearchRequest;
import com.contactbook.service.ContactBookWebSearchResponse;

import junit.framework.TestCase;

public class ContactBookServiceTest extends TestCase {
	
	public void setUp() {
		ContactBook.resetInstance();
	}

	public void testCreateContact() {
		
		ContactBookService contactBookService = new ContactBookService();
		ContactBookWebRequest webRequest = new ContactBookWebRequest();
		ContactBookWebResponse webResponse;
		ContactBookWebSearchResponse webGetResponse;
		
		webRequest.email = "jon_snow@gmail.com";
		webRequest.name = "Jon Snow";		
		webResponse =  contactBookService.createContact(webRequest);
		assertEquals(ContactBookRespose.SUCCESS, webResponse.response);
		
		webGetResponse =  contactBookService.getContact("jon_snow@gmail.com");
		assertEquals(ContactBookRespose.SUCCESS, webGetResponse.response);
		assertEquals("jon_snow@gmail.com", webGetResponse.searchResult.getContacts().get(0).getEmail());
		assertEquals("Jon Snow", webGetResponse.searchResult.getContacts().get(0).getName());
		
		webRequest.email = "";
		webRequest.name = "Jon Snow";		
		webResponse =  contactBookService.createContact(webRequest);
		assertNotNull(webResponse);
		assertEquals(ContactBookRespose.INVALID_INPUT, webResponse.response);
		
		webRequest.email = "jon_snow@gmail.com";
		webRequest.name = "";		
		webResponse =  contactBookService.createContact(webRequest);
		assertNotNull(webResponse);
		assertEquals(ContactBookRespose.INVALID_INPUT, webResponse.response);
	}

	public void testUpdateContact() {
		ContactBookService contactBookService = new ContactBookService();
		ContactBookWebRequest webRequest = new ContactBookWebRequest();
		ContactBookWebResponse webResponse;
		ContactBookWebSearchResponse webGetResponse;		
		
		webRequest.email = "jon_snow@gmail.com";
		webRequest.name = "Jon Snow";		
		webResponse =  contactBookService.createContact(webRequest);
				
		webRequest.email = "jon_snow@gmail.com";
		webRequest.name = "Kit Harington";		
		webResponse =  contactBookService.updateContact(webRequest);
		
		webGetResponse =  contactBookService.getContact("jon_snow@gmail.com");
		assertEquals(ContactBookRespose.SUCCESS, webGetResponse.response);
		assertEquals("jon_snow@gmail.com", webGetResponse.searchResult.getContacts().get(0).getEmail());
		assertEquals("Kit Harington", webGetResponse.searchResult.getContacts().get(0).getName());
		
		webRequest.email = "jon_snow@gmail.com";
		webRequest.name = "";	
		webResponse =  contactBookService.createContact(webRequest);
		assertNotNull(webResponse);
		assertEquals(ContactBookRespose.INVALID_INPUT, webResponse.response);
	}

	public void testDeleteContact() {
		ContactBookService contactBookService = new ContactBookService();
		ContactBookWebRequest webRequest = new ContactBookWebRequest();
		ContactBookWebResponse webResponse;
		ContactBookWebSearchResponse webGetResponse;		
		
		webRequest.email = "jon_snow@gmail.com";
		webRequest.name = "Jon Snow";		
		webResponse =  contactBookService.createContact(webRequest);
			
		webResponse =  contactBookService.deleteContact("jon_snow@gmail.com");
		assertEquals(ContactBookRespose.SUCCESS, webResponse.response);
		
		webGetResponse =  contactBookService.getContact("jon_snow@gmail.com");
		assertEquals(ContactBookRespose.NOT_FOUND, webGetResponse.response);		
	}

	public void testSearchContact() {
		ContactBookService contactBookService = new ContactBookService();
		ContactBookWebRequest webRequest = new ContactBookWebRequest();
		ContactBookWebResponse webResponse;
		
		ContactBookWebSearchRequest webSearchRequest = new ContactBookWebSearchRequest() ;
		ContactBookWebSearchResponse webSearchResponse;		
		
		webRequest.email = "jon_snow@gmail.com";
		webRequest.name = "Jon Snow";		
		webResponse =  contactBookService.createContact(webRequest);
		assertEquals(ContactBookRespose.SUCCESS, webResponse.response);
				
		webSearchRequest.searchString = "jon_snow@gmail.com";
		webSearchRequest.startIndex = 0;		
		webSearchResponse =  contactBookService.searchContactByEmail(webSearchRequest);
		assertEquals(ContactBookRespose.SUCCESS, webSearchResponse.response);
		assertEquals("jon_snow@gmail.com", webSearchResponse.searchResult.getContacts().get(0).getEmail());
		assertEquals("Jon Snow", webSearchResponse.searchResult.getContacts().get(0).getName());
		
		webSearchRequest.searchString = "Jon Snow";
		webSearchRequest.startIndex = 0;		
		webSearchResponse =  contactBookService.searchContactByName(webSearchRequest);
		assertEquals(ContactBookRespose.SUCCESS, webSearchResponse.response);
		assertEquals("jon_snow@gmail.com", webSearchResponse.searchResult.getContacts().get(0).getEmail());
		assertEquals("Jon Snow", webSearchResponse.searchResult.getContacts().get(0).getName());
	}

}
