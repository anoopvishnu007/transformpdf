package com.anoop.transformpdf.service.converters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class TxtCreator {
//    private static final Logger log = LoggerFactory.getLogger(TxtCreator.class);

    public void process(File pdf, File output){
        PDDocument pdDoc;
        try {//Kudos for closing: http://stackoverflow.com/questions/156508/closing-a-java-fileinputstream
            File tmpfile = File.createTempFile(String.format("txttmp-%s", UUID.randomUUID().toString()), null);
            RandomAccessFile raf = new RandomAccessFile(tmpfile, "rw");
			pdDoc = PDDocument.load(pdf);
            FileWriter writer = new FileWriter(output);
            try {
                PDFTextStripper stripper = new PDFTextStripper();
                int numberOfPages = pdDoc.getNumberOfPages();

                for (int j = 1; j < numberOfPages+1; j++) {
                    stripper.setStartPage(j);
                    stripper.setEndPage(j);
                    writer.write(stripper.getText(pdDoc));
                    writer.flush();
                }
            } finally {
                pdDoc.close();
                raf.close();
                tmpfile.delete();
                writer.close();
            }
        } catch (IOException ioe) {
//            log.warn(String.format("Failed to create txt for file: %s", pdf.getName()), ioe);
        }
    }
}
