package org.apache.lucene;

public class SearchResult {
	private String source;
	private String title;
	private String author;
	private String origin1;
	private String origin2;
	private String content;
	private int PMID;
	
	public String getSource() {
		return source;
	}
	public String getTitle() {
		return title;
	}
	public String getAuthor() {
		return author;
	}
	public String getOrigin1() {
		return origin1;
	}
	public String getOrigin2() {
		return origin2;
	}
	public String getContent() {
		return content;
	}
	public int getPMID() {
		return PMID;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setOrigin1(String origin1) {
		this.origin1 = origin1;
	}
	public void setOrigin2(String origin2) {
		this.origin2 = origin2;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setPMID(int pMID) {
		PMID = pMID;
	}	
}
