package com.selfshelf.project.model;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.selfshelf.project.model.UserPermission.*;

public enum UserRole {
    ADMIN(Sets.newHashSet(USER_READ, USER_WRITE, BOOK_READ, BOOK_WRITE, BOOK_SEARCH_READ,
            BOOK_SEARCH_WRITE)),
    USER (Sets.newHashSet(USER_READ, BOOK_READ, BOOK_SEARCH_READ));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;

    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return permissions;
    }
}
