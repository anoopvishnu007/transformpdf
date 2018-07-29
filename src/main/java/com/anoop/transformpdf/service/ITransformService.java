package com.anoop.transformpdf.service;

import java.io.File;

import com.anoop.transformpdf.data.Document;
import com.anoop.transformpdf.data.DocumentMetadata;


/**
 * A service to convert a file to the specified format.
 * 
 * @author Anoop
 */
public interface ITransformService {
    
    /**
     * Saves a document in the archive.
     * 
     * @param document A document
     * @return DocumentMetadata The meta data of the saved document
     */
    DocumentMetadata convertFile(Document document);

	DocumentMetadata convertPDFFile(Document document);

	File getDocumentFile(String id, String fileName);
    
     
}
