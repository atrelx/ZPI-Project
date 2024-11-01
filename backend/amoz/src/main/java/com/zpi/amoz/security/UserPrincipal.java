package com.zpi.amoz.security;

import java.util.HashMap;
import java.util.Map;

public class UserPrincipal {
    private String sub;
    private String email;

    public UserPrincipal(Map<String, Object> userPrincipal) {
        this.sub = (String) userPrincipal.get("sub");
        this.email = (String) userPrincipal.get("email");
    }

    public String getSub() {
        return sub;
    }

    public String getEmail() {
        return email;
    }
}

