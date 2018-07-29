package com.anoop.transformpdf.dao;

import com.anoop.transformpdf.data.Document;

/**
 * Data access object to create the file in the file system for
 * {@link Document}s
 * 
 * @author Anoop
 */
public interface IDocumentDao {

    /**
     * Inserts a document in the data store.
     * 
     * @param document A Document
     */
	void createFile(Document document, boolean isInputDoc);

	Document load(String uuid, String fileName);

	void deleteOldFiles();


}