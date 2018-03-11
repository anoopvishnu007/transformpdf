package com.anoop.transformpdf.service.converters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;

import com.anoop.transformpdf.data.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class PDF2HTMLConverter {



	public static void generateHTMLFromPDF(File outputDir, Document documentObj)
			throws ParserConfigurationException, IOException {
		PDDocument pdf = PDDocument.load(documentObj.getSourceFile());
		PDFDomTree parser = new PDFDomTree();
		Writer output = new PrintWriter(
				outputDir.getPath() + File.separator + FilenameUtils.getBaseName(documentObj.getFileName()) + ".html",
				"utf-8");
		parser.writeText(pdf, output);
		output.close();
		if (pdf != null) {
			pdf.close();
		}
	}

	public static void generatePDFFromHTML(File outputDir, Document documentObj)
			throws ParserConfigurationException, IOException, DocumentException {
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(
				outputDir.getPath() + File.separator + FilenameUtils.getBaseName(documentObj.getFileName()) + ".pdf"));
		document.open();
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(documentObj.getSourceFile()));
		document.close();
	}
}
