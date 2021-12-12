package com.letscode.starwarsresistence.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "rebel")
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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private RebelSoldierGender gender;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "headquarters_id")
    private Headquarters headquarters;

    @NotNull
    @Column(name = "active", columnDefinition = "boolean default true", nullable = false)
    private boolean active;

    @NotNull
    @Column(name = "is_traitor", columnDefinition = "boolean default false", nullable = false)
    private boolean traitor;

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
        this.traitor = false;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isTraitor() {
        return traitor;
    }

    public void setTraitor(boolean traitor) {
        this.traitor = traitor;
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

    public boolean hasItems() {
        return (this.getInventory() != null && this.getInventory().getItems() != null && !this.getInventory().getItems().isEmpty());
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

    public static class RebelSoldierResponse {
        private String completeName;
        private String nick;
        private Integer age;
        private String soldierGender;
        private String headquartersName;
        private Location location;
        private Inventory inventory;

        public RebelSoldierResponse(RebelSoldier rebelSoldier) {
            this.setCompleteName(rebelSoldier.getName());
            this.setAge(calculateAge(rebelSoldier));
            this.setInventory(rebelSoldier.getInventory());
            this.setSoldierGender(rebelSoldier.getGender().name());
            this.setLocation(rebelSoldier.getLocation());
            if (rebelSoldier.getHeadquarters() != null) this.setHeadquartersName(rebelSoldier.getHeadquarters().getGalaxyName());
            this.setNick(rebelSoldier.getNickName());
        }

        private Integer calculateAge(RebelSoldier rebelSoldier) {
            LocalDate birthLocalDate = null;
            if (rebelSoldier.getBirthDate() instanceof java.sql.Date) {
                birthLocalDate = ((java.sql.Date) rebelSoldier.getBirthDate()).toLocalDate();
            } else {
                birthLocalDate = rebelSoldier.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }

            return Period.between(birthLocalDate, LocalDate.now()).getYears();
        }

        public String getCompleteName() {
            return completeName;
        }

        public void setCompleteName(String completeName) {
            this.completeName = completeName;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getSoldierGender() {
            return soldierGender;
        }

        public void setSoldierGender(String soldierGender) {
            this.soldierGender = soldierGender;
        }

        public String getHeadquartersName() {
            return headquartersName;
        }

        public void setHeadquartersName(String headquartersName) {
            this.headquartersName = headquartersName;
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
        private Location location;

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

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public RebelSoldier toRebelSoldier() {
            RebelSoldier rebelSoldier = new RebelSoldier();
            rebelSoldier.setName(this.getName());
            rebelSoldier.setNickName(this.getNickName());
            rebelSoldier.setGender(this.getGender());
            rebelSoldier.setHeadquarters(this.getHeadquarters());
            rebelSoldier.setInventory(this.getInventory());
            rebelSoldier.getInventory().setRebelSoldier(rebelSoldier);
            if (rebelSoldier.hasItems()) rebelSoldier.getInventory().getItems().forEach(item -> item.setInventory(rebelSoldier.getInventory()));
            rebelSoldier.setBirthDate(this.getBirthDate());
            rebelSoldier.setLocation(this.getLocation());
            return rebelSoldier;
        }
    }
}
