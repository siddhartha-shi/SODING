package com.example.siddhartha.soding;

/**
 * Created by Siddhartha on 03-Oct-17.
 * sid
 */

public class User {
    private int id;
    private String name, description, created_at, updated_at;

    public User(int id, String name, String description, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }
}
