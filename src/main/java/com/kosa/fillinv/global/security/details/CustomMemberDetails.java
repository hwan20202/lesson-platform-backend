package com.kosa.fillinv.global.security.details;

import com.kosa.fillinv.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public record CustomMemberDetails(
        String memberId,
        String email,
        String password,
        Collection<? extends GrantedAuthority> authorities) implements UserDetails {

    public CustomMemberDetails(Member member) {
        this(member.getId(), member.getEmail(), member.getPassword(), Collections.emptyList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
