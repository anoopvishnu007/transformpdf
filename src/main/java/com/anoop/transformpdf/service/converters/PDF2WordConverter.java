package com.anoop.transformpdf.service.converters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.anoop.transformpdf.data.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class PDF2WordConverter {

	public static void generateDocFromPDF(File outputDir, Document documentObj) throws IOException {
		XWPFDocument doc = new XWPFDocument();

		PdfReader reader = new PdfReader(documentObj.getSourceFile().toPath().toString());
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);

		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			TextExtractionStrategy strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
			String text = strategy.getResultantText();
			XWPFParagraph p = doc.createParagraph();
			XWPFRun run = p.createRun();
			run.setText(text);
			run.addBreak(BreakType.PAGE);
		}
		FileOutputStream out = new FileOutputStream(
				outputDir.getPath() + File.separator + FilenameUtils.getBaseName(documentObj.getFileName()) + ".docx");
		doc.write(out);
		out.close();
		reader.close();
		doc.close();
	}

	public static void generatePDFFromDoc(File outputDir, Document documentObj) {

		String k = null;
		OutputStream fileForPdf = null;
		try {

			// Below Code is for .doc file
			if (documentObj.getSourceFile().getName().endsWith(".doc"))
			{
				HWPFDocument doc = new HWPFDocument(new FileInputStream(documentObj.getSourceFile()));
				WordExtractor we = new WordExtractor(doc);
				k = we.getText();

				fileForPdf = new FileOutputStream(
						new File(FilenameUtils.getBaseName(documentObj.getSourceFile().getName()) + ".pdf"));

				we.close();
			}

			// Below Code for

			else if (documentObj.getSourceFile().getName().endsWith(".docx"))
			{
				XWPFDocument docx = new XWPFDocument(new FileInputStream(
						documentObj.getSourceFile()));
				// using XWPFWordExtractor Class
				XWPFWordExtractor we = new XWPFWordExtractor(docx);
				k = we.getText();

				fileForPdf = new FileOutputStream(
						new File(FilenameUtils.getBaseName(documentObj.getSourceFile().getName()) + ".pdf"));
				we.close();
			}

			com.itextpdf.text.Document document = new com.itextpdf.text.Document();
			PdfWriter.getInstance(document, fileForPdf);

			document.open();

			document.add(new Paragraph(k));

			document.close();
			fileForPdf.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
