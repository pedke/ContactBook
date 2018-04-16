package test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ContactBookAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(ContactBookAllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(ContactBookServiceTest.class);
		suite.addTestSuite(ContactBookStoreHashTableTest.class);
		suite.addTestSuite(ContactBookTest.class);
		suite.addTestSuite(LuceneSearchProviderTest.class);
		//$JUnit-END$
		return suite;
	}
}
