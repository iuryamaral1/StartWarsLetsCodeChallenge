package com.letscode.starwarsresistence.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "rebel_soldier")
public class RebelSoldier {

    @Id
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull
    @Column(name = "nick_name", nullable = false, unique = true, length = 100)
    private String nickName;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date", nullable = false)
    private Date birthDate;

    @Transient
    private Integer age;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private RebelSoldierGender gender;

    @OneToOne(fetch = FetchType.EAGER)
    private Headquarters headquarters;

    @NotNull
    @Column(name = "active", columnDefinition = "boolean default true", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "is_traitor", columnDefinition = "boolean default false", nullable = false)
    private Boolean isTraitor;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "service_started_at", nullable = false)
    private Date serviceStartedAt;

    @Temporal(TemporalType.DATE)
    @Column(name = "service_end_at")
    private Date serviceEndAt;

    @OneToOne(mappedBy = "rebelSoldier", cascade = CascadeType.ALL)
    private Inventory inventory;

    @Embedded
    private Location location;

    public RebelSoldier() {
        this.id = UUID.randomUUID();
        this.active = true;
        this.gender = RebelSoldierGender.UNIDENTIFIED;
        this.isTraitor = false;
        this.serviceStartedAt = new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getAge() {
        return (int) ChronoUnit.YEARS.between(LocalDate.now(), this.birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public RebelSoldierGender getGender() {
        return gender;
    }

    public void setGender(RebelSoldierGender gender) {
        this.gender = gender;
    }

    public Headquarters getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(Headquarters headquarters) {
        this.headquarters = headquarters;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getTraitor() {
        return isTraitor;
    }

    public void setTraitor(Boolean traitor) {
        isTraitor = traitor;
    }

    public Date getServiceStartedAt() {
        return serviceStartedAt;
    }

    public void setServiceStartedAt(Date serviceStartedAt) {
        this.serviceStartedAt = serviceStartedAt;
    }

    public Date getServiceEndAt() {
        return serviceEndAt;
    }

    public void setServiceEndAt(Date serviceEndAt) {
        this.serviceEndAt = serviceEndAt;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RebelSoldier)) return false;
        RebelSoldier that = (RebelSoldier) o;
        return getId().equals(that.getId()) && getName().equals(that.getName()) && getNickName().equals(that.getNickName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getNickName());
    }

    public enum RebelSoldierGender {
        HUMAN,
        ROBOT,
        ALIEN,
        UNIDENTIFIED
    }

    public static class RebelSoldierRequest {

        @NotNull
        private String name;

        @NotNull
        private String nickName;

        @NotNull
        private Date birthDate;

        @NotNull
        private RebelSoldierGender gender;

        private Headquarters headquarters;

        @NotNull
        private Inventory inventory;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public Date getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(Date birthDate) {
            this.birthDate = birthDate;
        }

        public RebelSoldierGender getGender() {
            return gender;
        }

        public void setGender(RebelSoldierGender gender) {
            this.gender = gender;
        }

        public Headquarters getHeadquarters() {
            return headquarters;
        }

        public void setHeadquarters(Headquarters headquarters) {
            this.headquarters = headquarters;
        }

        public Inventory getInventory() {
            return inventory;
        }

        public void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }

        public RebelSoldier toRebelSoldier() {
            RebelSoldier rebelSoldier = new RebelSoldier();
            rebelSoldier.setName(this.name);
            rebelSoldier.setNickName(this.nickName);
            rebelSoldier.setGender(this.gender);
            rebelSoldier.setHeadquarters(this.headquarters);
            rebelSoldier.setInventory(this.inventory);
            return rebelSoldier;
        }
    }
}
