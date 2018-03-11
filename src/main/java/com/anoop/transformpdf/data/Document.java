package com.anoop.transformpdf.data;

import java.io.Serializable;
import java.util.Properties;

import com.anoop.transformpdf.service.ITransformService;

/**
 * A document object to convert which is managed by {@link ITransformService}.
 * 
 * @author Anoop
 */
public class Document extends DocumentMetadata implements Serializable {

    private static final long serialVersionUID = 2004955454853853315L;
	private byte[] fileData;

	public Document(byte[] fileData, String fileName, String convertFormat) {
		super(fileName, convertFormat);
		this.setFileData(fileData);
	}

	public Document(Properties properties) {
		super(properties);
	}

	public Document(DocumentMetadata metadata) {
		super(metadata.getUuid(), metadata.getFileName(), null);
	}
	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

    
}
