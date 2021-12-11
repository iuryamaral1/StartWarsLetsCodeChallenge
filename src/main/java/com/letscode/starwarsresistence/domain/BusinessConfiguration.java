package com.letscode.starwarsresistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "business_configuration")
public class BusinessConfiguration {

    @Id
    private UUID id;

    @NotNull
    @Column(name = "description", unique = true, nullable = false, length = 100)
    private String description;

    @NotNull
    @Column(name = "value", nullable = false)
    private Integer value;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
