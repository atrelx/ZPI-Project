package com.zpi.amoz.controllers;


import com.zpi.amoz.dtos.CompanyDTO;
import com.zpi.amoz.enums.ImageDirectory;
import com.zpi.amoz.models.Address;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.requests.CompanyCreateRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.responses.PathResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.CompanyService;
import com.zpi.amoz.services.FileService;
import jakarta.persistence.EntityNotFoundException;
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
    public ResponseEntity<?> createCompany(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
                                                 @Valid @RequestBody CompanyCreateRequest companyDetails) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            Company company = companyService.createCompany(userPrincipal.getSub(), companyDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(CompanyDTO.toCompanyDTO(company));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected exception has occured: "  + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserCompany(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            Company company = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"));

            return ResponseEntity.status(HttpStatus.OK).body(CompanyDTO.toCompanyDTO(company));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCompany(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
                                                 @Valid @RequestBody CompanyCreateRequest companyDetails) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();
            Company updatedCompany = companyService.update(companyId, companyDetails)
                    .orElseThrow(() -> new RuntimeException("Could not update company"));
            return ResponseEntity.status(HttpStatus.OK).body(CompanyDTO.toCompanyDTO(updatedCompany));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Unexpected exception occured: " + e.getMessage()));
        }
    }

    @PatchMapping
    public ResponseEntity<MessageResponse> deactivateCompany(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        try {
            UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();
            companyService.deactivateCompanyById(companyId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/companyPicture")
    public ResponseEntity<?> uploadCompanyProfilePicture(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @RequestParam("file") MultipartFile file) {

        try {
            UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();

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

            String uploadedFilePath = fileService.saveFile(bufferedImage, ImageDirectory.COMPANY_IMAGES.getDirectoryName(), companyId.toString());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new PathResponse(uploadedFilePath));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to upload file."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Could not found company for given user ID"));
        }
    }

    @GetMapping("/companyPicture")
    public ResponseEntity<byte[]> getCompanyProfilePicture(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        try {
            UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();
            BufferedImage bufferedImage = fileService.downloadFile(ImageDirectory.COMPANY_IMAGES.getDirectoryName(), companyId.toString());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "JPG", baos);
            byte[] imageBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);

        } catch (IOException | EntityNotFoundException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
