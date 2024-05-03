package com.example.userservicemorningbatch.security.models;

import com.example.userservicemorningbatch.models.Role;
import org.springframework.security.core.GrantedAuthority;

public class CustomGrantedAuthority implements GrantedAuthority  {
    private String authority;

    public CustomGrantedAuthority(Role role) {
        this.authority = role.getName();
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
