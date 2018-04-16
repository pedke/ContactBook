# ContactBook
Suite of CRUD APIs for a contact book app. APIs support adding/editing/deleting contacts

### Tech Stack
* Java 8
* Jersy for REST API
* [Apache Lucene](https://lucene.apache.org)
* Tomcat 8.5
* Angularjs (a minimal frontend not part of assignment)
* Hosted on AWS (Amazon Linux EC2 instance http://ec2-54-162-251-126.compute-1.amazonaws.com])

### Searching :
Search allows fuzzy search for name and email address separately.
eg. Search for name  "Jon Snow" returns all contacts having name like "John Sno", "Jon Snow", "John Snow" etc.
Similarly search for partial email string "john_snow" matchs with john_snow@gmail.com, john_snow@yahoo.com, john_snow@live.com etc.
Search supports pagination and return 5 items by default per invocation.

For searching we have used [Apache Lucene](https://lucene.apache.org) which is high performance, scalable java based library. 

### REST APIs :
We have used Jersey frameork with json for REST APIs.
All REST APIs are defined in [ContactBookService.java](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/service/ContactBookService.java).
1. Added a new contact : API [rest/ContactBookService/create](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/service/ContactBookService.java#L43-L80).
2. Update existing contact : API [rest/ContactBookService/update](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/service/ContactBookService.java#L86-L123).
3. Read existing contact : API [rest/ContactBookService/get](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/service/ContactBookService.java#L130-L160).
4. Delete existing contact : API [rest/ContactBookService/delete](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/service/ContactBookService.java#L168-L195).
5. Search contact by name : API [rest/ContactBookService/searchname](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/service/ContactBookService.java#L203-L241).
6. Search contact by email : API [rest/ContactBookService/searchemail](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/service/ContactBookService.java#L248-L287).
<br/>Request and Response json are defined in [package service](https://github.com/pedke/ContactBook/tree/master/ContactBookRestAPI/src/com/contactbook/service).

### Authentication :
All REST APIs are authenticated with basic http authentication header. Username and passoword are hard coded (configurable). Role bases Authentication filter is defined here [UserAuthenticationFilter.java](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/service/UserAuthenticationFilter.java). This can be extended to have multiple roles.   

### ContactBook : 
CRUD operations + search. It stores contact in database and also updates search indexes. It consumes interface [IContactBookStore.java](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/IContactBookStore.java) for storing conatact data and interface [ISearchProvider.java](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/ISearchProvider.java) for searching.

### Database :
We have used simple HashMap<email, contact> to store all contact. In memory HashMap should able to handle couple of million records.
Right now this app store contacts for one user only. It can be modified to for multiple users. In case we want millions of user with millions contact HashMap can be replaced with Key-Value database.

Also here we are storing conatct data redudently in Lucene ad documents. In this particular implementation we can completely remove HashMap. In general we integrate search provider like Lucene, Solr etc with database.
See implemenation [ContactBookStoreHashTable.java](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/ContactBookStoreHashTable.java)

### Searching :
Lucene maintains index for all contacts and provids search capability on email and name. For each operation in contact book search indexes are updated. It provides fuzzy search on name and email. We can easily change search strategy as Lucene provied different Analyzers and query capability like proxymity search.
See implemenation [LuceneSearchProvider.java](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/LuceneSearchProvider.java)

### Tests
JUnit tests are defined for all functionality
1. [ContactBookAllTests.java](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/test/ContactBookAllTests.java)
2. [ContactBookServiceTest.java](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/test/ContactBookServiceTest.java)
3. [ContactBookStoreHashTableTest.java](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/test/ContactBookStoreHashTableTest.java)
4. [ContactBookTest.java](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/test/ContactBookTest.java)
5. [LuceneSearchProviderTest.java](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/test/LuceneSearchProviderTest.java)
For Lucene provides separate framework for testing search functionality extesively howwever in favour of time I have used simple JUnit.

### Frontend
We have also included a minimal frontend to demonstrate use REST APIs. The Angularjs frontend is very basic with miminal error handling. This is not part of original assignment.
Please look into [contact_app.js](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/WebContent/js/contact_app.js) to see format to call REST APIs with json.
