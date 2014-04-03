package org.apache.lucene;

public enum Fieldname {
	SOURCE("source"), 
	TITLE ("title"),
	AUTHOR ("author"), 
	ORIGIN1 ("origin1"), 
	ORIGIN2 ("origin2"),
	CONTENT ("content"),
	PMID ("PMID");
	
	private final String name;
	
	private Fieldname(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
       return name;
    }
}
