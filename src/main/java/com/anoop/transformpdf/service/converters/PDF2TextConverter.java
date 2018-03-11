package com.anoop.transformpdf.service.converters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.anoop.transformpdf.data.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class PDF2TextConverter {


	public static void generateTxtFromPDF(File outputDir, Document documentObj) throws IOException {
		File f = documentObj.getSourceFile();
		String parsedText;
		PDFParser parser = new PDFParser(new RandomAccessFile(f, "r"));
		parser.parse();

		COSDocument cosDoc = parser.getDocument();

		PDFTextStripper pdfStripper = new PDFTextStripper();
		PDDocument pdDoc = new PDDocument(cosDoc);

		parsedText = pdfStripper.getText(pdDoc);

		if (cosDoc != null)
			cosDoc.close();
		if (pdDoc != null)
			pdDoc.close();

		PrintWriter pw = new PrintWriter(
				outputDir.getPath() + File.separator + FilenameUtils.getBaseName(documentObj.getFileName()) + ".txt");
		pw.print(parsedText);
		pw.close();
	}

	public static void generatePDFFromTxt(File outputDir, Document documentObj) throws IOException, DocumentException {
		com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document(PageSize.A4);
		PdfWriter
				.getInstance(pdfDoc,
						new FileOutputStream(outputDir.getPath() + File.separator
								+ FilenameUtils.getBaseName(documentObj.getFileName()) + ".pdf"))
				.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
		pdfDoc.open();
		
		Font myfont = new Font();
		myfont.setStyle(Font.NORMAL);
		myfont.setSize(11);
		pdfDoc.add(new Paragraph("\n"));
		
		BufferedReader br = new BufferedReader(new FileReader(documentObj.getSourceFile()));
		String strLine;
		while ((strLine = br.readLine()) != null) {
			Paragraph para = new Paragraph(strLine + "\n", myfont);
			para.setAlignment(Element.ALIGN_JUSTIFIED);
			pdfDoc.add(para);
		}
		
		pdfDoc.close();
		br.close();
	}

}
