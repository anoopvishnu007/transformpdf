package com.anoop.transformpdf.dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.anoop.transformpdf.data.Document;
import com.anoop.transformpdf.data.DocumentMetadata;

/**
 * Data access object to insert, find and load {@link Document}s.
 * 
 * FileSystemDocumentDao saves documents in the file system. No database in
 * involved. For each document a folder is created. The folder contains the
 * document and a properties files with the meta data of the document. Each
 * document in the archive has a Universally Unique Identifier (UUID). The name
 * of the documents folder is the UUID of the document.
 * 
 * @author Anoop
 */
@Service("documentDao")
public class FileSystemDocumentDao implements IDocumentDao {

    private static final Logger LOG = Logger.getLogger(FileSystemDocumentDao.class);
    
	public static final String DIRECTORY = "transform";
	public static final String IN_DIR = "input";
	public static final String OUT_DIR = "output";
	public static final String META_DATA_FILE_NAME = "metadata.properties";

	public static final long TEN_MINUTES = 10 * 60 * 1000;

    @PostConstruct
    public void init() {
        createDirectory(DIRECTORY);
    }
    
    /**
     * Inserts a document to the archive by creating a folder with the UUID
     * of the document. In the folder the document is saved and a properties file
     * with the meta data of the document. 
     * 
     * @see org.murygin.archive.dao.IDocumentDao#insert(org.murygin.archive.service.Document)
     */
    @Override
	public void createFile(Document document, boolean isInputDoc) {
        try {
			createDirectory(document, isInputDoc);
			saveFileData(document, isInputDoc);
		} catch (IOException e) {
            String message = "Error while inserting document";
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }


	private void saveFileData(Document document, boolean isInputDoc) throws IOException {
		String path = getDirectoryPath(document.getUuid(), isInputDoc);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(new File(path), document.getFileName())));
		stream.write(document.getFileData());
        stream.close();
		document.setSourceFile(new File(path));
    }

	public void saveMetaData(Document document) throws IOException {
		String path = getDirectoryPath(document.getUuid(), false);
		Properties props = document.createProperties();
		File f = new File(new File(path), META_DATA_FILE_NAME);
		OutputStream out = new FileOutputStream(f);
		props.store(out, "Document meta data");
	}
	private Document loadFromFileSystem(String uuid) throws IOException {
		DocumentMetadata metadata = loadMetadataFromFileSystem(uuid);
		if (metadata == null) {
			return null;
		}
		Path path = Paths.get(getDirectoryPath(uuid, false));
		
		Document document = new Document(metadata);
		document.setFileData(Files.readAllBytes(path));
		return document;
	}
    

	private List<File> getOldFileList() {
        File file = new File(DIRECTORY);
		File[] files = file.listFiles(new FileFilter() {
          @Override
			public boolean accept(File current) {
				boolean isOld = false;
				long createdTime = current.lastModified();
				long tenAgo = System.currentTimeMillis() - TEN_MINUTES;
				if (createdTime < tenAgo) {
					isOld = true;
				}
				return isOld;
          }
        });
		return Arrays.asList(files);
    }
    

    
	public String createDirectory(Document document, boolean isInputDoc) {
		String path = getDirectoryPath(document.getUuid(), isInputDoc);
        createDirectory(path);
        return path;
    }

    

	public String getDirectoryPath(String uuid, boolean isInputDir) {
		String path = "";
		if (isInputDir) {
			path = getInputDirectory(uuid);
		} else {
			path = getOutputDirectory(uuid);
		}
        return path;
    }

	public String getInputDirectory(String uuid) {
		StringBuilder sb = new StringBuilder();
		sb.append(DIRECTORY).append(File.separator).append(IN_DIR).append(File.separator).append(uuid);
		String path = sb.toString();
		return path;
	}

	private String getOutputDirectory(String uuid) {
		StringBuilder sb = new StringBuilder();
		sb.append(DIRECTORY).append(File.separator).append(OUT_DIR).append(File.separator).append(uuid);
		String path = sb.toString();
		return path;
	}

    private void createDirectory(String path) {
        File file = new File(path);
        file.mkdirs();
    }

	@Override
	public Document load(String uuid) {
		try {
			return loadFromFileSystem(uuid);
		} catch (IOException e) {
			String message = "Error while loading document with id: " + uuid;
			LOG.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	@Override
	@Scheduled(fixedRate = 300000)
	public void deleteOldFiles() {
		List<File> files = getOldFileList();
		for (File file : files) {
			if (file.exists()) {
				file.delete();
			}
		}
	}
	private DocumentMetadata loadMetadataFromFileSystem(String uuid) throws IOException {
		DocumentMetadata document = null;
		String dirPath = getDirectoryPath(uuid, false);
		File file = new File(dirPath);
		if (file.exists()) {
			Properties properties = readProperties(uuid);
			document = new DocumentMetadata(properties);

		}
		return document;
	}
	private Properties readProperties(String uuid) throws IOException {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(new File(getDirectoryPath(uuid, false), META_DATA_FILE_NAME));
			prop.load(input);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

}
