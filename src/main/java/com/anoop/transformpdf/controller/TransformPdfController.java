package com.anoop.transformpdf.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.anoop.transformpdf.data.Document;
import com.anoop.transformpdf.data.DocumentMetadata;
import com.anoop.transformpdf.service.ITransformService;

@Controller
@RequestMapping(value = "/transform")
public class TransformPdfController {

    private static final Logger LOG = Logger.getLogger(TransformPdfController.class);
    
	@Autowired
	ITransformService transformService;

	@RequestMapping(value = "/uploadPDFFile", method = RequestMethod.POST)
	public @ResponseBody DocumentMetadata uploadPdfFile(
			@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "convertFormat", required = true) String convertFormat,
			@RequestParam(value = "resolution", required = false) String resolution) {
		DocumentMetadata metadata = null;
		try {
			Document document = new Document(file.getBytes(), file.getOriginalFilename(), convertFormat);
			document.setResolution(Integer.valueOf(resolution));
			String selectedFileFormat = FilenameUtils.getExtension(document.getFileName());
			if (selectedFileFormat.equalsIgnoreCase("pdf")) {
				metadata = transformService.convertPDFFile(document);
			} else {
				metadata = transformService.convertFile(document);
			}

		} catch (RuntimeException e) {
			LOG.error("Error while uploading.", e);
			throw e;
		} catch (Exception e) {
			LOG.error("Error while uploading.", e);
			throw new RuntimeException(e);
		}
		return metadata;
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody DocumentMetadata uploadFile(
			@RequestParam(value = "file", required = true) MultipartFile file) {
		DocumentMetadata metadata = null;
    	
        try {
			String convertFormat = "pdf";
			Document document = new Document(file.getBytes(), file.getOriginalFilename(), convertFormat);
			metadata = transformService.convertFile(document);
          
        } catch (RuntimeException e) {
            LOG.error("Error while uploading.", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Error while uploading.", e);
            throw new RuntimeException(e);
        }
		return metadata;
    }
    
	/**
	 * Returns the document file from the archive with the given UUID.
	 * 
	 * Url: /archive/document/{id} [GET]
	 * 
	 * @param id
	 *            The UUID of a document
	 * @return The document file
	 */
	@RequestMapping(value = "/downloadFile/{id}/{fileName:.+}", method = RequestMethod.GET)
	public void getDocument(@PathVariable String id, @PathVariable String fileName, HttpServletResponse response) {
		// send it back to the client
		InputStream is;
		try {
			response.setContentType("application/*");

			is = new FileInputStream(transformService.getDocumentFile(id, fileName));
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
