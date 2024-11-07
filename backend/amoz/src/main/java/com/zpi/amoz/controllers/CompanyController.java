package com.zpi.amoz.controllers;


import com.zpi.amoz.enums.ImageDirectory;
import com.zpi.amoz.models.Address;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.requests.CompanyCreateRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.CompanyService;
import com.zpi.amoz.services.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private FileService fileService;

    @PostMapping
    public ResponseEntity<Company> createCompany(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
                                                 @Valid @RequestBody CompanyCreateRequest companyDetails) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        Company company = companyService.createCompany(userPrincipal.getSub(), companyDetails);
        return new ResponseEntity<>(company, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable UUID id) {
        return companyService.findById(id)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable UUID id,
                                                 @Valid @RequestBody CompanyCreateRequest companyDetails) {
        return companyService.update(id, companyDetails)
                .map(updatedCompany -> new ResponseEntity<>(updatedCompany, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MessageResponse> deactivateCompany(@PathVariable UUID id) {
        try {
            companyService.deactivateCompanyById(id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/companyPicture/{id}")
    public ResponseEntity<MessageResponse> uploadCompanyProfilePicture(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file) {

        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse("No file uploaded."));
            }

            if (!Objects.requireNonNull(file.getContentType()).startsWith("image")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse("Uploaded file is not an image."));
            }

            BufferedImage bufferedImage = fileService.convertToJpg(file);

            if (bufferedImage == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse("Unable to convert file to JPG format."));
            }

            String uploadedFilePath = fileService.saveFile(bufferedImage, ImageDirectory.COMPANY_IMAGES.getDirectoryName(), id.toString());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MessageResponse(uploadedFilePath));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to upload file."));
        }
    }

    @GetMapping("/companyPicture/{id}")
    public ResponseEntity<byte[]> getCompanyProfilePicture(@PathVariable UUID id) {
        try {
            BufferedImage bufferedImage = fileService.downloadFile(ImageDirectory.COMPANY_IMAGES.getDirectoryName(), id.toString());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "JPG", baos);
            byte[] imageBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);

        } catch (IOException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
