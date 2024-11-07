package com.zpi.amoz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {
    @Autowired
    private AzureBlobService azureBlobService;

    @Value("${amoz.fileservice.max-size-px}") private Integer maxSize;

    public String saveFile(BufferedImage bufferedImage, String directory, String fileName) throws IOException {
        return azureBlobService.uploadFileToFolder(bufferedImage, directory,fileName + ".jpg");
    }

    public BufferedImage downloadFile(String directory, String fileName) throws IOException {
        return azureBlobService.downloadFileFromFolder(directory, fileName);
    }
    public BufferedImage convertToJpg(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage originalImage = ImageIO.read(inputStream);
            if (originalImage == null) {
                return null;
            }

            int maxWidth = maxSize;
            int maxHeight = maxSize;

            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            int newWidth = originalWidth;
            int newHeight = originalHeight;

            if (originalWidth > maxWidth) {
                newWidth = maxWidth;
                newHeight = (int) ((double) originalHeight / originalWidth * maxWidth);
            }

            if (newHeight > maxHeight) {
                newHeight = maxHeight;
                newWidth = (int) ((double) originalWidth / originalHeight * maxHeight);
            }

            BufferedImage convertedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = convertedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();

            return convertedImage;
        } catch (IOException e) {
            return null;
        }
    }

}

