package com.pje.kelompok4.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SubagianUpdateReq {
    @NotNull(message = "id subagian harus diisi")
    private Long id;

    @NotBlank(message = "nama subagian harus diisi")
    @Size(max = 50)
    private String name;
    
    @NotBlank(message = "deskripsi subagian harus diisi")
    private String description;

    @NotNull(message = "id bagian harus diisi")
    private Long idBagian;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIdBagian() {
        return idBagian;
    }

    public void setIdBagian(Long idBagian) {
        this.idBagian = idBagian;
    }
}
