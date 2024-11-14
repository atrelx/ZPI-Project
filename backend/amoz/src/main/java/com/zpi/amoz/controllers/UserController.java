package com.zpi.amoz.controllers;

import com.zpi.amoz.dtos.UserDTO;
import com.zpi.amoz.enums.ImageDirectory;
import com.zpi.amoz.models.User;
import com.zpi.amoz.requests.UserRegisterRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.responses.UserIsRegisteredResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Operation(summary = "Rejestracja użytkownika", description = "Rejestruje nowego użytkownika.")
    @ApiResponse(responseCode = "201", description = "Pomyślnie zarejestrowano użytkownika",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
    )
    @ApiResponse(responseCode = "409", description = "Użytkownik już istnieje",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PostMapping
    public ResponseEntity<?> register(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @Valid @RequestBody UserRegisterRequest request
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            User user = userService.registerUser(userPrincipal.getSub(), request);
            UserDTO userDTO = UserDTO.toUserDTO(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Aktualizacja użytkownika", description = "Aktualizuje dane użytkownika.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie zaktualizowano użytkownika",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
    )
    @ApiResponse(responseCode = "404", description = "Użytkownik nie znaleziony",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "409", description = "Błąd konfliktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PutMapping
    public ResponseEntity<?> updateUser(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @Valid @RequestBody UserRegisterRequest request
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            User user = userService.updateUser(userPrincipal.getSub(), request);
            UserDTO userDTO = UserDTO.toUserDTO(user);
            return ResponseEntity.status(HttpStatus.OK).body(userDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(summary = "Przesyłanie zdjęcia profilowego", description = "Przesyła zdjęcie profilowe użytkownika.")
    @ApiResponse(responseCode = "204", description = "Pomyślnie przesłano zdjęcie")
    @ApiResponse(responseCode = "400", description = "Błąd w przesyłaniu pliku",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono użytkownika",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PutMapping("/picture")
    public ResponseEntity<?> uploadProfilePicture(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @RequestParam("file") MultipartFile file
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            fileService.uploadPicutre(file, userPrincipal.getSub(), ImageDirectory.USER_IMAGES);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to upload file." + e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Pobieranie zdjęcia profilowego", description = "Pobiera zdjęcie profilowe użytkownika.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano zdjęcie",
            content = @Content(mediaType = "image/jpeg"))
    @ApiResponse(responseCode = "404", description = "Nie znaleziono zdjęcia",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/picture")
    public ResponseEntity<?> getProfilePicture(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            byte[] imageBytes = fileService.downloadFile(userPrincipal.getSub(), ImageDirectory.USER_IMAGES);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Sprawdzenie, czy użytkownik jest zarejestrowany", description = "Sprawdza, czy użytkownik jest zarejestrowany w systemie.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie sprawdzono status rejestracji użytkownika",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserIsRegisteredResponse.class))
    )
    @GetMapping("/isRegistered")
    public ResponseEntity<UserIsRegisteredResponse> isUserRegistered(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        boolean isRegistered = userService.isUserRegistered(userPrincipal.getSub());
        UserIsRegisteredResponse response = new UserIsRegisteredResponse(isRegistered);
        return ResponseEntity.ok(response);
    }
}

