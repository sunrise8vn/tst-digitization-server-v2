package com.tst.utils;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


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
}
