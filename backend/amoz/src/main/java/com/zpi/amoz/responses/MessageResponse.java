package com.zpi.amoz.responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response zawierający wiadomość, która jest zwracana w odpowiedzi na zapytania API.")
public record MessageResponse(

        @Schema(description = "Wiadomość zawierająca informacje zwrotne, takie jak treść błędu.")
        String message

) {
}
