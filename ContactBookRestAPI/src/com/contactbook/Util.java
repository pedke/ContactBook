package com.contactbook;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Util {

	public static boolean isValidContact(Contact contact) {

		if (contact == null || contact.getEmail() == null || contact.getEmail().isEmpty()) {
			return false;
		}

		return true;
	}

	public static boolean isValidEmail(String email) {

		boolean isValid = false;

		if (email == null || email.isEmpty()) {
			isValid = false;
		} else {

			try {
				InternetAddress internetAddress = new InternetAddress(email);
				internetAddress.validate();
				isValid = true;
			} catch (AddressException e) {
				isValid = false;
			}
		}

		return isValid;
	}

	public static String getEmailTokens(String email) {
		return email.replace('@', ' ');
	}

	public static boolean isValidName(String name) {
		if (name == null || name.isEmpty()) {
			return false;
		}

		return true;
	}
	
	public static void log(Object object, Level level, String message ) {
		Logger logger = Logger.getLogger(object.getClass().getName());
		logger.log(level, message);
		}

}
