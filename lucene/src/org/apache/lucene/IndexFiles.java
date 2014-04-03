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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.Fieldname;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Index all text files under a directory.
 * <p>
 * This is a command-line application demonstrating simple Lucene indexing. Run
 * it with no command-line arguments for usage information.
 */
public class IndexFiles {

	private IndexFiles() {
	}
	
	private static BufferedReader br;

	/** Index all text files under a directory. */
	public static void main(String[] args) {
		String usage = "java org.apache.lucene.demo.IndexFiles"
				+ " [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\n"
				+ "This indexes the documents in DOCS_PATH, creating a Lucene index"
				+ "in INDEX_PATH that can be searched with SearchFiles";
		String indexPath = "index";
		String docsPath = null;
		boolean create = true;
		for (int i = 0; i < args.length; i++) {
			if ("-index".equals(args[i])) {
				indexPath = args[i + 1];
				i++;
			} else if ("-docs".equals(args[i])) {
				docsPath = args[i + 1];
				i++;
			} else if ("-update".equals(args[i])) {
				create = false;
			}
		}

		if (docsPath == null) {
			System.err.println("Usage: " + usage);
			System.exit(1);
		}

		final File docDir = new File(docsPath);
		if (!docDir.exists() || !docDir.canRead()) {
			System.out
					.println("Document directory '"
							+ docDir.getAbsolutePath()
							+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}

		Date start = new Date();
		try {
			System.out.println("Indexing to directory '" + indexPath + "'...");

			Directory dir = FSDirectory.open(new File(indexPath));
			// :Post-Release-Update-Version.LUCENE_XY:
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47,
					analyzer);

			if (create) {
				// Create a new index in the directory, removing any
				// previously indexed documents:
				iwc.setOpenMode(OpenMode.CREATE);
			} else {
				// Add new documents to an existing index:
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}

			// Optional: for better indexing performance, if you
			// are indexing many documents, increase the RAM
			// buffer. But if you do this, increase the max heap
			// size to the JVM (eg add -Xmx512m or -Xmx1g):
			//
			// iwc.setRAMBufferSizeMB(256.0);

			IndexWriter writer = new IndexWriter(dir, iwc);
			indexDocs(writer, docDir);

			// NOTE: if you want to maximize search performance,
			// you can optionally call forceMerge here. This can be
			// a terribly costly operation, so generally it's only
			// worth it when your index is relatively static (ie
			// you're done adding documents to it):
			//
			// writer.forceMerge(1);

			writer.close();

			Date end = new Date();
			System.out.println(end.getTime() - start.getTime()
					+ " total milliseconds");

		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass()
					+ "\n with message: " + e.getMessage());
		}
	}
	
	/**
	 * Reads the content of the file till a blank line and return this text block
	 * 
	 * @return Text block till blank line
	 * @throws IOException
	 */
	static private String searchUntillBlankLine() throws IOException {
			
		String line = "";
		String text = "";
		
		/** read until blank line */
		while((line = br.readLine()) != null) {
			/** concatenate coherent lines */
			if (!line.isEmpty())
				text += line;
			else
				break;
		}
		
		//System.out.println(text);
		
		return text;
		
	}
	
	
	private static Document parseDocument(Document doc) throws IOException {
		
		for (Fieldname field : Fieldname.values()) {
		
			String text = "";
			
			if (field == Fieldname.ORIGIN1)
				text = br.readLine();
			else
				text = searchUntillBlankLine();
			
			if (text.contains("PMID:") && !(field == Fieldname.PMID)) {
				doc = null;
				break;
			}
			
			if (field == Fieldname.CONTENT) {
				
				String content = text;
				String line = "";
				/** add everything to the CONTENT as long as it does not contains "PMID:" -> new field */
				while ( !(line = searchUntillBlankLine()).contains("PMID:") )
					content += line;
				
				int index = line.indexOf("PMID:");
				String addToContent = line.substring(0, index);
				line = line.substring(index, line.length()-1);
				
				content += addToContent;
				doc.add(new TextField(Fieldname.CONTENT.toString(), content, Field.Store.YES));
				
				String pmid = line;
				/** remove all non-digit characters */
				pmid = pmid.replaceAll("\\D+","");
				//System.out.println("PMID: " + pmid + " xxxxx "+ line);
				doc.add(new LongField(Fieldname.PMID.toString(), Long.parseLong(pmid), Field.Store.YES));
				
			} else if (field == Fieldname.PATH) {
				//TODO better solution
			}
			else if (field != Fieldname.PMID)
					doc.add(new TextField(field.toString(), text, Field.Store.YES));
			
		}
		
		return doc;
		
	}

	/**
	 * Indexes the given file using the given writer, or if a directory is
	 * given, recurses over files and directories found under the given
	 * directory.
	 * 
	 * NOTE: This method indexes one document per input file. This is slow. For
	 * good throughput, put multiple documents into your input file(s). An
	 * example of this is in the benchmark module, which can create "line doc"
	 * files, one document per line, using the <a href=
	 * "../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
	 * >WriteLineDocTask</a>.
	 * 
	 * @param writer
	 *            Writer to the index where the given file/dir info will be
	 *            stored
	 * @param file
	 *            The file to index, or the directory to recurse into to find
	 *            files to index
	 * @throws IOException
	 *             If there is a low-level I/O error
	 */
	static void indexDocs(IndexWriter writer, File file) throws IOException {
		// do not try to index files that cannot be read
		if (file.canRead()) {
			if (file.isDirectory()) {
				String[] files = file.list();
				// an IO error could occur
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						indexDocs(writer, new File(file, files[i]));
					}
				}
			} else {

				FileInputStream fis;
				try {
					fis = new FileInputStream(file);
				} catch (FileNotFoundException fnfe) {
					/** some temp files raise this exception */
					return;
				}

				try {
					
					/** Read contents of file */
					br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
					
					/** read the file until end is reached */
					// TODO add workaround
					// br.read() positions the BufferedReader behind the first character
					// in the worst case a a single character is omitted in the first field
					// storing br.readLine() in a String and setting it to the content of the field does only work,
					// if we rewrite searchUntilBlankLine() or at least add some if-else statements
					while(br.read() != -1) {
						
						/** create new dataset */
						Document doc = new Document();
						
						/** store path of file in index*/
						Field pathField = new StringField(Fieldname.PATH.toString(), file.getPath(), Field.Store.YES);
						doc.add(pathField);
						
						doc = parseDocument(doc);
						
						if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
							/** New index, so we just add the document */
							System.out.println("adding " + file);
							if (doc != null)
								writer.addDocument(doc);
						} else {
							/** Existing index so replace the old one matching the exact path, if present */
							System.out.println("updating " + file);
							writer.updateDocument(new Term("path", file.getPath()), doc);
						}
						
					}

				} finally {
					fis.close();
				}
			}
		}
	}
}
