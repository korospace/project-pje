package com.pje.kelompok4.payload.request;

import javax.validation.constraints.*;

public class UserUpdateReq {
    @NotBlank(message = "id is required")
    private String id;

    @NotBlank(message = "NIK is required")
    @Size(max = 20)
    private String nik;

    @NotBlank(message = "username is required")
    @Size(min = 6, max = 20)
    private String username;
    
    @Size(min = 6, max = 20)
    private String password;

    @NotBlank(message = "role is required")
    private String role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
  
}
