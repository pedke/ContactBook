package com.contactbook.service;

import java.util.logging.Level;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.contactbook.Constants;
import com.contactbook.Contact;
import com.contactbook.ContactBook;
import com.contactbook.ContactBookRespose;
import com.contactbook.Util;


/**
 * Entry point for rest APIs
 * All rest APIs are defined in this class. 
 * All input validations are done here at entry point. 
 * We have not done input validation at other places in system
 */

@Path("/ContactBookService")
public class ContactBookService {
	
	ContactBook contactBook;

	public ContactBookService() {
		contactBook = ContactBook.getInstance();
	}
	
	/**
	 * @param webRequest { email : "email_id", name : "name" }
	 * @return webResponse { rsp : "Operation response Enum" , msg : "Operation success message"}
	 */
	@PUT
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ContactBookWebResponse createContact(ContactBookWebRequest webRequest) {

		ContactBookWebResponse webResponse = new ContactBookWebResponse();

		if (!Util.isValidEmail(webRequest.email)) {
			webResponse.response = ContactBookRespose.INVALID_INPUT;
			webResponse.message = "Invalid email";
			Util.log(this, Level.WARNING, "Invalid email  in request email : " + webRequest.email);
			return webResponse;
		}

		if (!Util.isValidName(webRequest.name)) {
			webResponse.response = ContactBookRespose.INVALID_INPUT;
			webResponse.message = "Invalid name";
			Util.log(this, Level.WARNING, "Invalid name  in request name : " + webRequest.name);
			return webResponse;
		}

		if (contactBook.getContact(webRequest.email) != null) {
			webResponse.response = ContactBookRespose.DUPLICATE;
			webResponse.message = "Contact aleady exists";
			Util.log(this, Level.INFO, "Trying to add already existing contact email : " + webRequest.email);
			return webResponse;
		}

		webResponse.response = contactBook.addNewContact(new Contact(webRequest.email, webRequest.name));
		
		if(webResponse.response == ContactBookRespose.SUCCESS) {
			webResponse.message = "Contact added Succcessfully";
			Util.log(this, Level.INFO, "Contact added Succcessfully : " + webRequest.email);
		}

		return webResponse;
	}

	/**
	 * @param webRequest { email : "email_id", name : "name" }
	 * @return webResponse { rsp : "Operation response enum" , msg : "Operation success message"}
	 */
	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ContactBookWebResponse updateContact(ContactBookWebRequest webRequest) {

		ContactBookWebResponse webResponse = new ContactBookWebResponse();

		if (!Util.isValidEmail(webRequest.email)) {
			webResponse.response = ContactBookRespose.INVALID_INPUT;
			webResponse.message = "Invalid email";
			Util.log(this, Level.WARNING, "Invalid email  in request email : " + webRequest.email);
			return webResponse;
		}

		if (!Util.isValidName(webRequest.name)) {
			webResponse.response = ContactBookRespose.INVALID_INPUT;
			webResponse.message = "Invalid name";
			Util.log(this, Level.WARNING, "Invalid name  in request name : " + webRequest.name);
			return webResponse;
		}

		if (contactBook.getContact(webRequest.email) == null) {
			webResponse.response = ContactBookRespose.NOT_FOUND;
			webResponse.message = "Contact does not exists";
			Util.log(this, Level.INFO, "Updating for non existing contact email : " + webRequest.email);
			return webResponse;
		}

		webResponse.response = contactBook.updateContact(new Contact(webRequest.email, webRequest.name));
		
		if(webResponse.response == ContactBookRespose.SUCCESS) {
			webResponse.message = "Contact Updated Succcessfully";
			Util.log(this, Level.INFO, "Contact updated succcessfully email : " + webRequest.email);
		}

		return webResponse;
	}
	
	/**
	 * @param email
	 * @return webResponse { rsp : "Operation response enum" , msg : "Operation success message", 
	 * 						 cont[] : "if found single contact in array else empty array"	}
	 */
	@RolesAllowed(UserAuthenticationFilter.USER_ROLE)
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public ContactBookWebSearchResponse getContact(@QueryParam (value = "email") String email) {

		ContactBookWebSearchResponse webResponse = new ContactBookWebSearchResponse();

		if (!Util.isValidEmail(email)) {
			webResponse.response = ContactBookRespose.INVALID_INPUT;
			webResponse.message = "Invalid email";
			Util.log(this, Level.WARNING, "Invalid email  in request email : " + email);
			return webResponse;
		}

		Contact contact = contactBook.getContact(email);

		if (contact == null) {
			webResponse.response = ContactBookRespose.NOT_FOUND;
			webResponse.message = "Contact deos not exists";
			Util.log(this, Level.INFO, "Reading non existing contact email : " + email);
		} else {
			webResponse.searchResult.addContact(contact);
			webResponse.searchResult.setTotalCount(1);
			webResponse.response = ContactBookRespose.SUCCESS;
			webResponse.message = "Read contact successfully";
			Util.log(this, Level.INFO, "Read contact successfully email : " + email);
		}

		return webResponse;
	}
	
	/**
	 * @param email
	 * @return webResponse { rsp : "Operation response enum" , msg : "Operation success message", 
	 * 						 cont[] : "empty array, not used"	}
	 */

	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public ContactBookWebResponse deleteContact(@QueryParam (value = "email") String email) {

		ContactBookWebResponse webResponse = new ContactBookWebResponse();

		if (!Util.isValidEmail(email)) {
			webResponse.response = ContactBookRespose.INVALID_INPUT;
			webResponse.message = "Invalid email";
			Util.log(this, Level.WARNING, "Invalid email  in request email : " + email);
			return webResponse;
		}

		webResponse.response = contactBook.deleteContact(email);

		if (webResponse.response != ContactBookRespose.SUCCESS) {
			webResponse.message = "Error while deleting contact";
			Util.log(this, Level.SEVERE, "Error while deleting contact email : " + email
					+ " " + ContactBookRespose.SUCCESS);
			
		} else {
			webResponse.message = "Deleted Contact Successfully";
			Util.log(this, Level.INFO, "Deleted contact successfully email : " + email);
		}

		return webResponse;
	}
	
	/**
	 * @param webRequest  { sstr : "email to search", sin : "start index to search from(for pagination)"}
	 * @return webResponse { rsp : "Operation response enum" , msg : "Operation success message", 
	 * 						 sr[] : "search results"	}
	 */

	@POST
	@Path("/searchemail")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ContactBookWebSearchResponse searchContactByEmail(ContactBookWebSearchRequest webRequest) {
		
		ContactBookWebSearchResponse webResponse = new ContactBookWebSearchResponse();
		
		if (!Util.isValidName(webRequest.searchString)) {
			webResponse.response = ContactBookRespose.INVALID_INPUT;
			webResponse.message = "Invalid search string";
			Util.log(this, Level.WARNING, "Invalid search string : " + webRequest.searchString);
			return webResponse;
		}
		
		if ( webRequest.startIndex < 0 && webRequest.startIndex >=  Constants.MAX_SEARCH_RESULTS ) {
			webResponse.response = ContactBookRespose.INVALID_INPUT;
			webResponse.message = "Start index should be between 0 and " + Constants.MAX_SEARCH_RESULTS;
			Util.log(this, Level.WARNING, "Invalid start index" + 
					"Start index should be between 0 and " + Constants.MAX_SEARCH_RESULTS +
					"searchString : " + webRequest.searchString);
			return webResponse;
		}
		
		webResponse.searchResult = 
				contactBook.searchByEmail(webRequest.searchString, webRequest.startIndex);
		
		if(webResponse.searchResult.getContacts().size() <=  0) {
			webResponse.response = ContactBookRespose.NOT_FOUND;
			webResponse.message = "Not contact exists with given email";
			Util.log(this, Level.INFO, "Search found 0 results." + "searchString : " + webRequest.searchString );
		} else {
			webResponse.response = ContactBookRespose.SUCCESS;
			Util.log(this, Level.INFO, "Search found " + webResponse.searchResult.getTotalCount() +
					" results. " + "searchString : " + webRequest.searchString );
		}
		
		 return webResponse;
	}
	
	/**
	 * @param webRequest  { sstr : "name to search", sin : "start index to search from(for pagination)"}
	 * @return webResponse { rsp : "Operation response enum" , msg : "Operation success message", 
	 * 						 sr[] : "search results"	}
	 */	
	@POST
	@Path("/searchname")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ContactBookWebSearchResponse searchContactByName(ContactBookWebSearchRequest webRequest) {
				
		ContactBookWebSearchResponse webResponse = new ContactBookWebSearchResponse();
		
		if (!Util.isValidName(webRequest.searchString)) {
			webResponse.response = ContactBookRespose.INVALID_INPUT;
			webResponse.message = "Invalid search string";
			Util.log(this, Level.WARNING, "Invalid search string : " + webRequest.searchString);
			return webResponse;
		}
		
		if ( webRequest.startIndex < 0 && webRequest.startIndex >=  Constants.MAX_SEARCH_RESULTS ) {
			webResponse.response = ContactBookRespose.INVALID_INPUT;
			webResponse.message = "Start "
					+ "index should be between 0 and " + Constants.MAX_SEARCH_RESULTS;
			Util.log(this, Level.WARNING, "Invalid start index" + 
					"Start index should be between 0 and " + Constants.MAX_SEARCH_RESULTS +
					"searchString : " + webRequest.searchString);
			return webResponse;
		}
		
		webResponse.searchResult = 
				contactBook.searchByName(webRequest.searchString, webRequest.startIndex);
		
		if(webResponse.searchResult.getContacts().size() <=  0) {
			webResponse.response = ContactBookRespose.NOT_FOUND;
			webResponse.message = "Not contact exists with given name";
			Util.log(this, Level.INFO, "Search found 0 results." + "searchString : " + webRequest.searchString );
		} else {
			webResponse.response = ContactBookRespose.SUCCESS;
			Util.log(this, Level.INFO, "Search found " + webResponse.searchResult.getTotalCount() +
					" results. " + "searchString : " + webRequest.searchString );
		}
		
		 return webResponse;
	}
}