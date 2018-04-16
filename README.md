# ContactBook
Suite of CRUD APIs for a contact book app. APIs support adding/editing/deleting contacts

<b>Searching :</b> <br/>
Search allows fuzzy search for name and email address separately.
eg. Search for name  "Jon Snow" returns all contacts having name like "John Sno", "Jon Snow", "John Snow" etc.
Similarly search for partial email string "john_snow" matchs with john_snow@gmail.com, john_snow@yahoo.com, john_snow@live.com etc.
Search supports pagination and return 5 items by default per invocation.

For searching we have used [Apache Lucene](https://lucene.apache.org) which is high performance, scalable java based library. 

<b>REST APIs : </b><br/>
All REST APIs are defined in [ContactBookService.java](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/service/ContactBookService.java).
1. Added a new contact : API [/create](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/service/ContactBookService.java#L43-L80).
2. Update existing contact : API [/update](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/service/ContactBookService.java#L86-L123).
3. Read existing contact : API [/get](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/service/ContactBookService.java#L130-L160).
4. Delete existing contact : API [/delete](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/service/ContactBookService.java#L168-L195).
5. Search contact by name : API [/searchname](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/service/ContactBookService.java#L203-L241).
6. Search contact by email : API [/update](https://github.com/pedke/ContactBook/blob/master/ContactBookRestAPI/src/com/contactbook/service/ContactBookService.java#L248-L287).



Tests
Add unit tests and Integration tests for each functionality.
Add basic authentication for the app. Use environment variables or basic auth(for rest APIs)
Code should scale out for millions of contacts per contact book. 
