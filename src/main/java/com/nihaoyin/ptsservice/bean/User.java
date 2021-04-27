package com.nihaoyin.ptsservice.bean;

public class User {
    private String userId;
    private String username;
    private String password;
    private boolean active;
    private String created;

    public String getCreated() {
        return created;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getActive() {
        return active;
    }

    public void setId(String id) {
        this.userId = id;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
