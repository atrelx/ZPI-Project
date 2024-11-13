package com.zpi.amoz.controllers;

import com.azure.core.annotation.Post;
import com.zpi.amoz.enums.ImageDirectory;
import com.zpi.amoz.enums.SystemRole;
import com.zpi.amoz.models.ContactPerson;
import com.zpi.amoz.models.Employee;
import com.zpi.amoz.models.Person;
import com.zpi.amoz.models.User;
import com.zpi.amoz.requests.UserRegisterRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.responses.PathResponse;
import com.zpi.amoz.responses.UserIsRegisteredResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @PostMapping
    public ResponseEntity<User> register(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal, @Valid @RequestBody UserRegisterRequest request) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        String sub = userPrincipal.getSub();

        try {
            User user = userService.registerUser(sub, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal, @Valid @RequestBody UserRegisterRequest request) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        String sub = userPrincipal.getSub();

        try {
            User user = userService.updateUser(sub, request);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/picture")
    public ResponseEntity<?> uploadProfilePicture(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @RequestParam("file") MultipartFile file) {

        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        String sub = userPrincipal.getSub();

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

            String uploadedFilePath = fileService.saveFile(bufferedImage, ImageDirectory.USER_IMAGES.getDirectoryName(), sub);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new PathResponse(uploadedFilePath));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to upload file."));
        }
    }

    @GetMapping("/picture")
    public ResponseEntity<byte[]> getProfilePicture(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {

        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        String sub = userPrincipal.getSub();

        try {
            BufferedImage bufferedImage = fileService.downloadFile(ImageDirectory.USER_IMAGES.getDirectoryName(), sub);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "JPG", baos);
            byte[] imageBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/isRegistered")
    public ResponseEntity<UserIsRegisteredResponse> isUserRegistered(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        boolean isRegistered = userService.isUserRegistered(userPrincipal.getSub());
        UserIsRegisteredResponse response = new UserIsRegisteredResponse(isRegistered);
        return ResponseEntity.ok(response);
    }
}
