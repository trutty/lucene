package org.apache.lucene;

public interface SearchInterface {

	/**
	 * Run lucene search on default/all fields
	 * 
	 * @param query
	 *            Query string to search for
	 * @param startResult
	 *            Number of the first result which should be returned, first
	 *            result is 1
	 * @param numberOfResults
	 *            Number of results
	 * @return Object of class SearchResult containing the found documents and
	 *         some statistics
	 */
	public SearchResult search(String query, int startResult, int numberOfResults);

	/**
	 * Run lucene search on default/all fields
	 * 
	 * @param query
	 *            Query string to search for
	 * @param startResult
	 *            Number of the fist result which should be returned, first
	 *            result is 1
	 * @param numberOfResults
	 *            Number of results
	 * @param fieldname
	 *            Name of the field to run the query on
	 * @return Object of class SearchResult containing the found documents and
	 *         some statistics
	 */
	public SearchResult search(String query, int startResult, int numberOfResults,
			Fieldname fieldname);
}
