package com.anoop.transformpdf.service.converters;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import com.anoop.transformpdf.data.Document;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

public class PDF2ImageConverter {



	public static void generateImageFromPDF(File outputDir, Document documentObj) throws IOException {
		PDDocument document = PDDocument.load(documentObj.getSourceFile());
		PDFRenderer pdfRenderer = new PDFRenderer(document);
		for (int page = 0; page < document.getNumberOfPages(); ++page) {
			BufferedImage bim = pdfRenderer.renderImageWithDPI(page, documentObj.getResolution(), ImageType.RGB);
			ImageIOUtil.writeImage(bim,
					String.format(FilenameUtils.getBaseName(documentObj.getFileName()) + "%d.%s", page + 1,
							documentObj.getFormatToConvert()),
					documentObj.getResolution());
		}
		document.close();
	}

	public static void generatePDFFromImage(File outputDir, Document documentObj)
			throws IOException, BadElementException, DocumentException {
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		FileOutputStream fos = new FileOutputStream(
				outputDir.getPath() + File.separator + FilenameUtils.getBaseName(documentObj.getFileName()) + ".pdf");
		PdfWriter writer = PdfWriter.getInstance(document, fos);
		writer.open();
		document.open();
		document.add(Image.getInstance((new URL(documentObj.getSourceFile().getPath()))));
		document.close();
		writer.close();
	}

}
