package org.apache.lucene;

public class GeneDocument {

	private String source;
	private String title;
	private String author;
	private String origin1;
	private String origin2;
	private String content;
	private int PMID;
	private String path;
	
	public GeneDocument(){
		this.source = "";
		this.title = "";
		this.author = "";
		this.origin1 = "";
		this.origin2 = "";
		this.content = "";
		this.PMID = -1;
		this.path = "";
	}

	public GeneDocument(String source, String title, String author,
			String origin1, String origin2, String content, int PMID, String path) {
		super();
		this.source = source;
		this.title = title;
		this.author = author;
		this.origin1 = origin1;
		this.origin2 = origin2;
		this.content = content;
		this.PMID = PMID;
		this.path = path;
	}

	public void set(Fieldname f, Object value){
		switch(f){
		case SOURCE:
			setSource(value.toString());
			break;
		case TITLE:
			setTitle(value.toString());
			break;
		case AUTHOR:
			setAuthor(value.toString());
			break;
		case ORIGIN1:
			setOrigin1(value.toString());
			break;
		case ORIGIN2:
			setOrigin2(value.toString());
			break;
		case CONTENT:
			setContent(value.toString());
			break;
		case PMID:
			setPMID(Integer.parseInt(value.toString()));
			break;
		case PATH:
			setPath(value.toString());
			break;
		}
	}
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
	
	public String getPath(){
		return path;
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

	public void setPMID(int PMID) {
		this.PMID = PMID;
	}
	
	public void setPath(String path){
		this.path = path;
	}
}
