package com.zpi.amoz.dtos;

import com.zpi.amoz.enums.SystemRole;
import com.zpi.amoz.models.User;

public record UserDTO(String userId,
                      SystemRole systemRole) {

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getSystemRole()
        );
    }
}

