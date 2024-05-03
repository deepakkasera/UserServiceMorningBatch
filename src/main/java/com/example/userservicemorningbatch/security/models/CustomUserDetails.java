package com.example.userservicemorningbatch.security.models;

import com.example.userservicemorningbatch.models.Role;
import com.example.userservicemorningbatch.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails  {
    private String password;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonBlocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private List<CustomGrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.username = user.getEmail();
        this.password = user.getHashedPassword();
        this.accountNonBlocked = true;
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.enabled = true;

        //User object has List<Role>
        //We need to convert List<Role> into List<CustomGrantedAuthority>
        this.authorities = new ArrayList<>();

        for (Role role : user.getRoles()) {
            authorities.add(new CustomGrantedAuthority(role));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
