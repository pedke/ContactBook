package com.contactbook;

import java.io.IOException;
import java.util.logging.Level;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * Search provider for contact book. We used Lucene to index and search email and name in contact book.
 * Each contact is redundantly added to Lucene as documents which can be search using fuzzy search.
 * We can use different search types like Fuzyy, proximity, exact search however right now we have 
 * only used fuzzy search.  
 * 
 * Also, for email search in Lucene we should use UAX29URLEmailTokenizer/UAX29URLEmailAnalyzer for
 * better fuzzy search however right now we are not able find correct java package in Lucene for 
 * UAX29URLEmailTokenizer/UAX29URLEmailAnalyzer. We'll Fix this later.
 *
 */

public class LuceneSearchProvider implements ISearchProvider {

	Analyzer analyzer;
	Directory index;

	IndexWriter indexWriter;
	SearcherManager searcherManager;

	public LuceneSearchProvider() {

		analyzer = new StandardAnalyzer();
		index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		try {
			indexWriter = new IndexWriter(index, config);
			searcherManager = new SearcherManager(indexWriter, null);
		} catch (IOException e) {
			Util.log(this, Level.SEVERE, "Failed to initialize LuceneSearchProvider + \n" +
					e.getMessage() + e.getStackTrace());  
			e.printStackTrace();
		}
	}

	@Override
	public boolean addNewContact(Contact contact) {

		Document doc = new Document();

		// In Lucene StringField is not tokenized and won't be partially searchable
		// Also we TextField can not be uniquely addressed for delete and update
		// A workaround for this is to store it redundantly as StringField and as
		// TextField
		// Ideally we should have be been using
		// UAX29URLEmailTokenizer/UAX29URLEmailAnalyzer
		// however I am not able to make work properly.
		// Fix this later.
		doc.add(new StringField(Constants.TEXT_ID, contact.getEmail(), Field.Store.YES));
		doc.add(new TextField(Constants.TEXT_EMAIL, contact.getEmail(), Field.Store.YES));
		doc.add(new TextField(Constants.TEXT_NAME, contact.getName(), Field.Store.YES));

		try {

			indexWriter.addDocument(doc);
			indexWriter.commit();
			return true;

		} catch (IOException e) {
			Util.log(this, Level.SEVERE, "exeception while adding document. "
					+ "Document may not be searchable + \n" + 
					e.getMessage() + e.getStackTrace());
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean updateContact(Contact contact) {

		Term term = new Term(Constants.TEXT_ID, contact.getEmail());

		Document doc = new Document();
		doc.add(new StringField(Constants.TEXT_ID, contact.getEmail(), Field.Store.YES));
		doc.add(new TextField(Constants.TEXT_EMAIL, contact.getEmail(), Field.Store.YES));
		doc.add(new TextField(Constants.TEXT_NAME, contact.getName(), Field.Store.YES));

		try {
			
			indexWriter.updateDocument(term, doc);			
			indexWriter.commit();
			return true;
		} catch (IOException e) {
			Util.log(this, Level.SEVERE, "exeception while updating document. "
					+ "Document may not be searchable + \n" + 
					e.getMessage() + e.getStackTrace());
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteContact(String email) {

		Term term = new Term(Constants.TEXT_ID, email);

		try {

			indexWriter.deleteDocuments(term);
			indexWriter.commit();
			return true;

		} catch (IOException e) {
			Util.log(this, Level.SEVERE, "exeception while delete document. "
					+ "Document may be still searchable + \n" + 
					e.getMessage() + e.getStackTrace());
			e.printStackTrace();
		}
		
		return false;
	}

	public SearchResult search(Query query, int startIndex) {

		SearchResult searchResult = new SearchResult();
		IndexSearcher searcher = null;

		try {

			TopScoreDocCollector collector = TopScoreDocCollector.create(Constants.MAX_SEARCH_RESULTS);

			if (!searcherManager.isSearcherCurrent()) {
				searcherManager.maybeRefresh();
			}

			searcher = searcherManager.acquire();
			searcher.search(query, collector);

			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			searchResult.setTotalCount(collector.getTotalHits());

			int lastIndex = startIndex + Constants.SEARCH_RESULTS_PER_PAGE;
			lastIndex = lastIndex < hits.length ? lastIndex : hits.length;

			if (startIndex < hits.length) {
				for (int i = startIndex; i < lastIndex; i++) {
					Document document = searcher.doc(hits[i].doc);
					searchResult.addContact(document.get(Constants.TEXT_ID), document.get(Constants.TEXT_NAME));
				}
			}

		} catch (IOException e) {
			Util.log(this, Level.SEVERE, "exeception while searching document. "
					+ "Search results may not be complete. + \n" + 
					e.getMessage() + e.getStackTrace());
			e.printStackTrace();
		} finally {
			try {
				searcherManager.release(searcher);
				searcher = null;
			} catch (IOException | NullPointerException e) {
				e.printStackTrace();
			}
		}

		return searchResult;
	}

	@Override
	public SearchResult searchByEmail(String emailQueryString, int startIndex) {

		SearchResult searchResult = new SearchResult();

		try {
			Query query = buildSimpleQuery(Constants.TEXT_EMAIL, emailQueryString);
			searchResult = search(query, startIndex);
		} catch (ParseException e) {
			Util.log(this, Level.SEVERE, "Exeception while parsing search string emailQueryString : " +
					emailQueryString + e.getMessage() + e.getStackTrace());
			e.printStackTrace();
		}

		return searchResult;
	}

	@Override
	public SearchResult searchByName(String nameQueryString, int startIndex) {
		Query query = buildFuzzySpanQuery(Constants.TEXT_NAME, nameQueryString);
		return search(query, startIndex);
	}

	private Query buildFuzzySpanQuery(String field, String queryString) {

		Query query = null;

		String[] nameTokens = queryString.split(Constants.DELIMITERS);

		// Build span query for multiple words
		// and simple query for single word
		if (nameTokens.length > 1) {

			SpanQuery[] clauses = new SpanQuery[nameTokens.length];

			for (int i = 0; i < nameTokens.length; i++) {
				clauses[i] = new SpanMultiTermQueryWrapper<FuzzyQuery>(new FuzzyQuery(new Term(field, nameTokens[i])));
			}

			query = new SpanNearQuery(clauses, 0, true);
		} else if (nameTokens.length == 1) {
			query = new FuzzyQuery(new Term(field, nameTokens[0]));
		}

		return query;
	}

	private Query buildSimpleQuery(String field, String queryString) throws ParseException {

		QueryParser parser = new QueryParser(field, analyzer);
		parser.setDefaultOperator(QueryParser.Operator.AND);
		return parser.parse(queryString);
	}

}