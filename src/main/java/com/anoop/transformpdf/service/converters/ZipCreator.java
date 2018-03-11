package com.anoop.transformpdf.service.converters;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCreator {
//    private static final Logger log = LoggerFactory.getLogger(ZipCreator.class);

	public static void create(File imgsDir, File output) throws IOException {
        pack(imgsDir.getAbsolutePath(), output.getAbsolutePath());
    }

    private static void pack(String sourceDirPath, String zipFilePath) throws IOException {
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            zs.write(Files.readAllBytes(path));
                            zs.closeEntry();
                        } catch (Exception e) {
//                            log.warn("Failed to create zip file", e);
                        }
                    });
        }
    }

    public static void main(String[] args) {
        new ImgCreator().process(new File("/home/joze/Downloads/ASEFlogo_popravljen.pdf"), new File("/home/joze/Downloads"), 300, 800, 800, ImageFileExtension.PNG);
    }
}


