package com.selfshelf.project.model;

public enum UserPermission {

    USER_READ("user:read"),
    USER_WRITE("user:write"),

    BOOK_READ("book:read"),
    BOOK_WRITE("book:write"),

    BOOK_SEARCH_READ("bookSearch:read"),
    BOOK_SEARCH_WRITE("bookSearch:write");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
