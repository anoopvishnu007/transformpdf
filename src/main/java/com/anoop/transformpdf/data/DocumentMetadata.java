package com.anoop.transformpdf.data;

import java.io.File;
import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

/**
 * Meta data of a document from an archive managed by {@link IArchiveService}.
 * 
 * @author Anoop
 */
public class DocumentMetadata implements Serializable {
    
	static final long serialVersionUID = 7283287076019483950L;
	protected File sourceFile;
    protected File convertedFile;
    protected String formatToConvert;
	protected String uuid;
	protected String fileName;
	protected int resolution;
	public static final String PROP_UUID = "uuid";
	public static final String PROP_FILE_NAME = "file-name";
    public DocumentMetadata() {
        super();
    }

    
	public DocumentMetadata(String fileName, String formatToConvert) {
		this(UUID.randomUUID().toString(), fileName, formatToConvert);
         
    }


	public DocumentMetadata(String uuid, String fileName, String formatToConvert) {
		this.uuid = uuid;
		this.fileName = fileName;
		this.formatToConvert = formatToConvert;
	}

	public DocumentMetadata(Properties properties) {
		this(properties.getProperty(PROP_UUID), properties.getProperty(PROP_FILE_NAME), null);

	}
	public File getSourceFile() {
		return sourceFile;
	}



	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}



	public File getConvertedFile() {
		return convertedFile;
	}



	public void setConvertedFile(File convertedFile) {
		this.convertedFile = convertedFile;
	}



	public String getFormatToConvert() {
		return formatToConvert;
	}



	public void setFormatToConvert(String formatToConvert) {
		this.formatToConvert = formatToConvert;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
    
	public Properties createProperties() {
		Properties props = new Properties();
		props.setProperty(PROP_UUID, getUuid());
		props.setProperty(PROP_FILE_NAME, getFileName());

		return props;
	}

	public int getResolution() {
		return resolution;
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}
}
