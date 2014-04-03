package org.apache.lucene;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/** Simple command-line based search demo. */
public class SearchFiles implements SearchInterface {
	/*
	 * Static variables, for performance reasons, to avoid unnecessary creation
	 * of IndexSearcher
	 */
	private static String index = "index";
	private static IndexReader reader = null;
	private static IndexSearcher searcher = null;

	public static void main(String [] argv){
		SearchFiles searchFile = new SearchFiles();
		SearchResult searchResult = searchFile.search("is",1,10);
		System.out.println(searchResult.getTotalHits());
	}
	
	private static void initIndexSearcher() {
		if (reader == null) {
			try {
				reader = DirectoryReader.open(FSDirectory.open(new File(index)));
				searcher = new IndexSearcher(reader);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private SearchFiles() {
	}

	@Override
	public SearchResult search(String queryString, int startResult, int numberOfResults) {
		String[] fieldnames = new String[Fieldname.values().length - 1];

		for (Fieldname f : Fieldname.values()) {
			if (f != Fieldname.PATH)
				fieldnames[f.ordinal()] = f.toString();
		}

		return executeSearch(queryString, startResult, numberOfResults, fieldnames);
	}

	@Override
	public SearchResult search(String queryString, int startResult, int numberOfResults,
			Fieldname fieldname) {

		String[] fieldnames = new String[1];
		fieldnames[0] = fieldname.toString();

		return executeSearch(queryString, startResult, numberOfResults, fieldnames);
	}

	private SearchResult executeSearch(String queryString, int startResult, int numberOfResults,
			String[] fieldnames) {
		SearchResult searchResult = null;

		initIndexSearcher();

		try {
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);

			QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_47, fieldnames, analyzer);

			queryString = queryString.trim();

			if (queryString.length() == 0) {
				return null;
			}

			Query query = parser.parse(queryString);
			System.out.println("Query: " + query.toString(fieldnames[0]));
			
			searchResult = doPagingSearch(searcher, query, startResult, numberOfResults);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return searchResult;
	}

	/**
	 * This demonstrates a typical paging search scenario, where the search
	 * engine presents pages of size n to the user. The user can then go to the
	 * next page if interested in the next hits.
	 * 
	 * When the query is executed for the first time, then only enough results
	 * are collected to fill 5 result pages. If the user wants to page beyond
	 * this limit, then the query is executed another time and all hits are
	 * collected.
	 * 
	 */
	private SearchResult doPagingSearch(IndexSearcher searcher, Query query, int offset,
			int numberOfResults) throws IOException {

		int requestedResults = offset + numberOfResults - 1;

		TopDocs results = searcher.search(query, requestedResults);
		
		int numTotalHits = results.totalHits;

		ScoreDoc[] hits = results.scoreDocs;
		System.out.println(hits.length);
		SearchResult searchResult = new SearchResult();

		searchResult.setTotalHits(numTotalHits);

		int end = Math.min(numTotalHits, requestedResults);

		for (int i = offset - 1; i < end; i++) {
			GeneDocument geneDoc = new GeneDocument();

			Document doc = searcher.doc(hits[i].doc);

			for (Fieldname f : Fieldname.values()) {
				if (doc.get(f.toString()) != null)
					geneDoc.set(f, doc.get(f.toString()));
			}

			searchResult.addGeneDocument(geneDoc);
		}

		return searchResult;
	}

}
