package com.tst.utils;

import com.tst.exceptions.DataInputException;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Component
public class FileUtils {
    public boolean isPDFUsingTika(MultipartFile multipartFile) throws IOException {
        byte[] data = multipartFile.getBytes();
        Tika tika = new Tika();
        String detectedType = tika.detect(data);
        return "application/pdf".equals(detectedType);
    }

//    public boolean isPDFUsingIText(byte[] data) {
//        try {
//            PdfReader reader = new PdfReader(data);
//            reader.close();
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }

    public void renameFile(String source, String newName) throws IOException {
        Path sourcePath = Paths.get(source);

        if (Files.exists(sourcePath)) {
            Path newPath = sourcePath.resolveSibling(newName);
            Files.move(sourcePath, newPath);
        } else {
            System.out.println("File does not exist.");
            throw new DataInputException("Tập tin không tồn tại.");
        }
    }

    public String getParentFolderOfFile(String filePath) {
        Path currentPath = Paths.get(filePath);
        Path parentDir = currentPath.getParent().getParent();

        return parentDir.toString();
    }

    public void moveFile(String oldFilePath, String newDirectoryPath) throws IOException {
        Path currentPath = Paths.get(oldFilePath);
        Path newDir = Paths.get(newDirectoryPath);
        Path targetPath = newDir.resolve(currentPath.getFileName());
        Files.move(currentPath, targetPath);
    }

    public String moveFileUpOneDirectory(String filePath) throws IOException {
        Path currentPath = Paths.get(filePath);
        Path parentDir = currentPath.getParent().getParent();
        Path targetPath = parentDir.resolve(currentPath.getFileName());
        Files.move(currentPath, targetPath);

        return parentDir.toString(); // Trả về đường dẫn thư mục cha sau khi di chuyển file
    }

    public boolean isDirectoryEmpty(Path directory) throws IOException {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        }
    }

}
