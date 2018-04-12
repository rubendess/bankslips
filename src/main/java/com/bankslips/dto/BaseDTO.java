package com.bankslips.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseDTO implements Serializable {
    private String id;

    public BaseDTO() {
    }

    public BaseDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @JsonIgnore
    public UUID getIdAsUUID() {
        return id != null ? UUID.fromString(this.id) : null;
    }

    public void setId(String id) {
        this.id = id;
    }
}
