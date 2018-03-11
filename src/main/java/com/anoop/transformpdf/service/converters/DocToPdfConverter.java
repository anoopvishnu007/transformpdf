package com.anoop.transformpdf.service.converters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class DocToPdfConverter {

	public static void convertToPDF(File file) {

		String k = null;
		OutputStream fileForPdf = null;
		try {

			// Below Code is for .doc file
			if (file.getName().endsWith(".doc"))
			{
				HWPFDocument doc = new HWPFDocument(new FileInputStream(file));
				WordExtractor we = new WordExtractor(doc);
				k = we.getText();

				fileForPdf = new FileOutputStream(new File(FilenameUtils.getBaseName(file.getName()) + ".pdf"));

				we.close();
			}

			// Below Code for

			else if (file.getName().endsWith(".docx"))
			{
				XWPFDocument docx = new XWPFDocument(new FileInputStream(
						file));
				// using XWPFWordExtractor Class
				XWPFWordExtractor we = new XWPFWordExtractor(docx);
				k = we.getText();

				fileForPdf = new FileOutputStream(new File(FilenameUtils.getBaseName(file.getName()) + ".pdf"));
				we.close();
			}



			Document document = new Document();
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

