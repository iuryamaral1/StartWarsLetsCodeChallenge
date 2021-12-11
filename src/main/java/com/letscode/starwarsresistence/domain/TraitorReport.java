package com.letscode.starwarsresistence.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "traitor_report", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"rebel_soldier_id", "traitor_id"})
})
public class TraitorReport {

    @NotNull
    @JoinColumn(name = "rebel_soldier_id")
    private RebelSoldier rebelReporterId;

    @NotNull
    @JoinColumn(name = "traitor_id")
    private RebelSoldier traitor;

    public RebelSoldier getRebelReporterId() {
        return rebelReporterId;
    }

    public void setRebelReporterId(RebelSoldier rebelReporterId) {
        this.rebelReporterId = rebelReporterId;
    }

    public RebelSoldier getTraitor() {
        return traitor;
    }

    public void setTraitor(RebelSoldier traitor) {
        this.traitor = traitor;
    }
}
