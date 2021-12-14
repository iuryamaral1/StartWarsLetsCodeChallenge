package com.letscode.starwarsresistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "traitor_report", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"rebel_soldier_id", "traitor_id"})
})
public class TraitorReport {

    @Id
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "rebel_soldier_id")
    private RebelSoldier rebelReporter;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "traitor_id")
    private RebelSoldier traitor;

    public TraitorReport() {
        this.id = UUID.randomUUID();
    }

    public RebelSoldier getRebelReporter() {
        return rebelReporter;
    }

    public void setRebelReporter(RebelSoldier rebelReporter) {
        this.rebelReporter = rebelReporter;
    }

    public RebelSoldier getTraitor() {
        return traitor;
    }

    public void setTraitor(RebelSoldier traitor) {
        this.traitor = traitor;
    }

    public static class TraitorReportRequest {
        private UUID reporterId;
        private UUID traitorId;

        public TraitorReportRequest(UUID reporterId, UUID traitorId) {
            this.reporterId = reporterId;
            this.traitorId = traitorId;
        }

        public UUID getReporterId() {
            return reporterId;
        }

        public void setReporterId(UUID reporterId) {
            this.reporterId = reporterId;
        }

        public UUID getTraitorId() {
            return traitorId;
        }

        public void setTraitorId(UUID traitorId) {
            this.traitorId = traitorId;
        }

        public TraitorReport toTraitorReport() {
            RebelSoldier reporter = new RebelSoldier();
            reporter.setId(this.getReporterId());

            RebelSoldier traitor = new RebelSoldier();
            traitor.setId(this.getTraitorId());

            TraitorReport traitorReport = new TraitorReport();
            traitorReport.setTraitor(traitor);
            traitorReport.setRebelReporter(reporter);
            return traitorReport;
        }
    }

    public static class TraitorReportOperationResponse {
        private int amountOfReports;
        private String traitorName;
        private String reportedBy;
        private int missingReportsToBecomeTraitor;
        private boolean reportedRebelIsATraitor;
        private String message;

        public int getAmountOfReports() {
            return amountOfReports;
        }

        public void setAmountOfReports(int amountOfReports) {
            this.amountOfReports = amountOfReports;
        }

        public String getTraitorName() {
            return traitorName;
        }

        public void setTraitorName(String traitorName) {
            this.traitorName = traitorName;
        }

        public String getReportedBy() {
            return reportedBy;
        }

        public void setReportedBy(String reportedBy) {
            this.reportedBy = reportedBy;
        }

        public int getMissingReportsToBecomeTraitor() {
            return missingReportsToBecomeTraitor;
        }

        public void setMissingReportsToBecomeTraitor(int missingReportsToBecomeTraitor) {
            this.missingReportsToBecomeTraitor = missingReportsToBecomeTraitor;
        }

        public boolean isReportedRebelIsATraitor() {
            return reportedRebelIsATraitor;
        }

        public void setReportedRebelIsATraitor(boolean reportedRebelIsATraitor) {
            this.reportedRebelIsATraitor = reportedRebelIsATraitor;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


}
