package com.zpi.amoz.services;

import com.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class AzureBlobService {
    private final BlobServiceClient blobServiceClient;

    private final String containerName;

    @Autowired
    public AzureBlobService(@Value("${azure.storage.connection-string}") String connectionString,
                            @Value("${azure.storage.container-name}") String containerName) {
        this.containerName = containerName;
        this.blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
    }

    private BlobContainerClient getBlobContainerClient() {
        return blobServiceClient.getBlobContainerClient(containerName);
    }

    public String uploadFileToFolder(BufferedImage bufferedImage, String folderName, String fileName) throws IOException {
        BlobContainerClient containerClient = getBlobContainerClient();

        String blobName = folderName + "/" + fileName;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "JPG", byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        BlobClient blobClient = containerClient.getBlobClient(blobName);

        try (InputStream inputStream = new java.io.ByteArrayInputStream(imageBytes)) {
            blobClient.upload(inputStream, imageBytes.length, true);

            return blobClient.getBlobUrl();
        }
    }

    public BufferedImage downloadFileFromFolder(String folderName, String fileName) throws IOException {
        BlobContainerClient containerClient = getBlobContainerClient();

        String blobName = folderName + "/" + fileName + ".jpg";

        BlobClient blobClient = containerClient.getBlobClient(blobName);

        if (!blobClient.exists()) {
            throw new IOException("The file does not exist in the blob container.");
        }

        try (InputStream inputStream = blobClient.openInputStream()) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            if (bufferedImage == null) {
                throw new IOException("The file is not a valid image.");
            }

            return bufferedImage;
        }
    }
}
