package org.apache.lucene;

import java.util.List;

public interface SearchInterface {
	public List<SearchResult> search(String query);
}
