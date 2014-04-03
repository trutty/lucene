package org.apache.lucene;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
	private List<GeneDocument> documents;
	private int totalHits;

	public List<GeneDocument> getDocuments() {
		return documents;
	}
	
	public SearchResult() {
		documents = new ArrayList<>();
		totalHits = 0;
	}

	public int getTotalHits() {
		return totalHits;
	}

	public void setDocuments(List<GeneDocument> documents) {
		this.documents = documents;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}
	
	public void addGeneDocument(GeneDocument document){
		documents.add(document);
	}
}
