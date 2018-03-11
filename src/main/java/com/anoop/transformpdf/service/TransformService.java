package com.anoop.transformpdf.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anoop.transformpdf.dao.FileSystemDocumentDao;
import com.anoop.transformpdf.data.Document;
import com.anoop.transformpdf.data.DocumentMetadata;
import com.anoop.transformpdf.service.converters.PDF2HTMLConverter;
import com.anoop.transformpdf.service.converters.PDF2ImageConverter;
import com.anoop.transformpdf.service.converters.PDF2TextConverter;
import com.anoop.transformpdf.service.converters.PDF2WordConverter;
import com.anoop.transformpdf.service.converters.ZipCreator;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;

/**
 * A service to save, find and get documents from an archive. 
 * 
 * @author Anoop
 */
@Service("transformService")
public class TransformService implements ITransformService, Serializable {

    private static final long serialVersionUID = 8119784722798361327L;

	@Autowired
	FileSystemDocumentDao fileSystemDocumentDao;

	@Override
	public DocumentMetadata convertPDFFile(Document document) {
		try {
			String format = document.getFormatToConvert();
			fileSystemDocumentDao.createFile(document, true);
			String outputDir = fileSystemDocumentDao.getDirectoryPath(document.getUuid(), false);
			File outputDirectory = new File(outputDir);
			switch (format) {
			case "png":
				PDF2ImageConverter.generateImageFromPDF(outputDirectory, document);
				createOutputZipFile(document, outputDirectory);
			case "jpg":
				PDF2ImageConverter.generateImageFromPDF(outputDirectory, document);
				createOutputZipFile(document, outputDirectory);
			case "doc":
				PDF2WordConverter.generateDocFromPDF(outputDirectory, document);
			case "docx":
				PDF2WordConverter.generateDocFromPDF(outputDirectory, document);
			case "txt":
				PDF2TextConverter.generateTxtFromPDF(outputDirectory, document);
			case "html":
				PDF2HTMLConverter.generateHTMLFromPDF(outputDirectory, document);

			}
		} catch (IOException e) {

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return document;
	}

	private void createOutputZipFile(Document document, File outputDirectory) {
		String outputFilePath = outputDirectory + File.separator + document.getFileName();
		File outputFile = new File(outputFilePath);
		try {
			ZipCreator.create(outputDirectory, outputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		document.setConvertedFile(outputFile);
	}

	/**
	 * Returns the document file from the archive
	 * 
	 * @see org.murygin.archive.service.IArchiveService#getDocumentFile(java.lang.String)
	 */
	@Override
	public byte[] getDocumentFile(String id) {
		Document document = fileSystemDocumentDao.load(id);
		if (document != null) {
			return document.getFileData();
		} else {
			return null;
		}
	}

	@Override
	public DocumentMetadata convertFile(Document document) {
		try {
			String format = document.getFormatToConvert();
			fileSystemDocumentDao.createFile(document, true);
			String outputDir = fileSystemDocumentDao.getDirectoryPath(document.getUuid(), false);
			File outputDirectory = new File(outputDir);
			switch (format) {
			case "png":
				PDF2ImageConverter.generatePDFFromImage(outputDirectory, document);
			case "jpg":
				PDF2ImageConverter.generatePDFFromImage(outputDirectory, document);
			case "doc":
				PDF2WordConverter.generatePDFFromDoc(outputDirectory, document);
			case "docx":
				PDF2WordConverter.generatePDFFromDoc(outputDirectory, document);
			case "txt":
				PDF2TextConverter.generatePDFFromTxt(outputDirectory, document);
			case "html":
				PDF2HTMLConverter.generatePDFFromHTML(outputDirectory, document);

			}
		} catch (IOException e) {

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return document;
	}
	 
    


}
