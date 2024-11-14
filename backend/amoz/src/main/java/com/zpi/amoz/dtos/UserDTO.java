package com.zpi.amoz.dtos;

import com.zpi.amoz.enums.SystemRole;
import com.zpi.amoz.models.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO reprezentujące dane użytkownika systemu, w tym identyfikator oraz rola w systemie.")
public record UserDTO(

        @Schema(description = "Identyfikator użytkownika", example = "118443418389427414489")
        String userId,

        @Schema(description = "Rola użytkownika w systemie", example = "USER")
        SystemRole systemRole

) {

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getSystemRole()
        );
    }
}


