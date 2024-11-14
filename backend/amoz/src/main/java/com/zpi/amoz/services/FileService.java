package com.zpi.amoz.services;

import com.zpi.amoz.enums.ImageDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {
    @Autowired
    private AzureBlobService azureBlobService;

    @Value("${amoz.fileservice.max-size-px}")
    private Integer maxSize;
    @Value("${amoz.fileservice.compression-quality}")
    private Float compressionQuality;

    private String saveFile(BufferedImage bufferedImage, String directory, String fileName) throws IOException {
        return azureBlobService.uploadFileToFolder(bufferedImage, directory, fileName + ".jpg");
    }

    public byte[] downloadFile(String fileName, ImageDirectory imageDirectory) throws IOException {
        BufferedImage bufferedImage = azureBlobService.downloadFileFromFolder(imageDirectory.getDirectoryName(), fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "JPG", baos);
        return baos.toByteArray();

    }

    public void uploadPicutre(MultipartFile file, String fileName, ImageDirectory imageDirectory) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("No file uploaded");
        }

        if (!Objects.requireNonNull(file.getContentType()).startsWith("image")) {
            throw new IllegalArgumentException("Uploaded file is not an image.");
        }

        BufferedImage bufferedImage = this.convertToJpg(file);

        if (bufferedImage == null) {
            throw new IllegalArgumentException("Unable to convert file to JPG format.");
        }

        this.saveFile(bufferedImage, imageDirectory.getDirectoryName(), fileName);
    }

    private BufferedImage convertToJpg(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage originalImage = ImageIO.read(inputStream);
            if (originalImage == null) {
                return null;
            }

            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            int newWidth = originalWidth;
            int newHeight = originalHeight;

            if (originalWidth > maxSize || originalHeight > maxSize) {
                double aspectRatio = (double) originalWidth / originalHeight;
                if (originalWidth > originalHeight) {
                    newWidth = maxSize;
                    newHeight = (int) (maxSize / aspectRatio);
                } else {
                    newHeight = maxSize;
                    newWidth = (int) (maxSize * aspectRatio);
                }
            }

            BufferedImage convertedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = convertedImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
            ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
            jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpgWriteParam.setCompressionQuality(compressionQuality);

            try (ImageOutputStream outputStream = ImageIO.createImageOutputStream(byteArrayOutputStream)) {
                jpgWriter.setOutput(outputStream);
                jpgWriter.write(null, new javax.imageio.IIOImage(convertedImage, null, null), jpgWriteParam);
                jpgWriter.dispose();
            }

            try (InputStream compressedImageStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray())) {
                return ImageIO.read(compressedImageStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

