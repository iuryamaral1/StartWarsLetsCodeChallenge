package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.RebelSoldier;
import com.letscode.starwarsresistence.domain.TraitorReport;
import com.letscode.starwarsresistence.domain.exceptions.ApplicationBusinessException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NotifyTraitor {

    private ManageRebelSoldier manageRebelSoldier;
    private TraitorReportGateway traitorReportGateway;
    private ManageBusinessConfiguration manageBusinessConfiguration;

    public NotifyTraitor(
            TraitorReportGateway traitorReportGateway,
            ManageRebelSoldier manageRebelSoldier,
            ManageBusinessConfiguration manageBusinessConfiguration
    ) {
        this.traitorReportGateway = traitorReportGateway;
        this.manageRebelSoldier = manageRebelSoldier;
        this.manageBusinessConfiguration = manageBusinessConfiguration;
    }

    public TraitorReport.TraitorReportOperationResponse notify(TraitorReport.TraitorReportRequest notifyTraitor) throws ApplicationBusinessException {
        Optional<RebelSoldier> optionalSuspectToBeTraitor = this.manageRebelSoldier.findById(notifyTraitor.getTraitorId());

        if (!canReport(notifyTraitor, optionalSuspectToBeTraitor)) {
            throw new ApplicationBusinessException("You can't report!");
        }

        RebelSoldier suspectToBeTraitor = optionalSuspectToBeTraitor.get();

        if (suspectToBeTraitor.isTraitor()) {
            TraitorReport.TraitorReportOperationResponse response = new TraitorReport.TraitorReportOperationResponse();
            response.setTraitorName(suspectToBeTraitor.getName());
            response.setMissingReportsToBecomeTraitor(0);
            response.setMessage("This rebel is already marked as a traitor. Thanks for your report!");
            return response;
        }

        if (isRepeatedReport(notifyTraitor)) {
            throw new ApplicationBusinessException("You already have reported this rebel as a traitor!");
        }

        this.traitorReportGateway.saveTraitorReport(notifyTraitor.toTraitorReport());
        Integer amountOfReports = this.traitorReportGateway.findAmountOfReportsForRebel(suspectToBeTraitor.getId());
        Integer amountOfReportsNeededToBecomeTraitor = this.manageBusinessConfiguration.amountOfReportsNeededToBecomeTraitor();

        if (shouldMarkAsTraitor(amountOfReports, amountOfReportsNeededToBecomeTraitor)) {
            RebelSoldier newTraitor = this.manageRebelSoldier.markSoldierAsTraitor(suspectToBeTraitor);
            return buildResponse(
                    newTraitor,
                    amountOfReports,
                    amountOfReportsNeededToBecomeTraitor,
                    "This soldier was marked as a traitor!!! Thanks for your report!");
        }

        return buildResponse(
                suspectToBeTraitor,
                amountOfReports,
                amountOfReportsNeededToBecomeTraitor,
                "Your report has been saved!");
    }

    private boolean isRepeatedReport(TraitorReport.TraitorReportRequest notifyTraitor) {
        var reports = this.traitorReportGateway.findByReporterIdAndTraitorId(notifyTraitor.getReporterId(), notifyTraitor.getTraitorId());
        return reports.size() > 0;
    }

    /*
    *  Both reporter and suspect soldier must exist.
    *  You can't report yourself.
    *  You can't report a traitor if you are a traitor.
    */
    private boolean canReport(TraitorReport.TraitorReportRequest notifyTraitor, Optional<RebelSoldier> optionalSuspectToBeTraitor) throws ApplicationBusinessException {
        Optional<RebelSoldier> optionalReporterSoldier = this.manageRebelSoldier.findById(notifyTraitor.getReporterId());

        return (
                (optionalSuspectToBeTraitor.isPresent()) &&
                (optionalReporterSoldier.isPresent()) &&
                (!optionalReporterSoldier.get().isTraitor()) &&
                (!notifyTraitor.getTraitorId().equals(notifyTraitor.getReporterId()))
        );
    }

    private boolean shouldMarkAsTraitor(Integer amountOfReports, Integer amountOfReportsNeededToBecomeTraitor) {
        return (amountOfReports >= amountOfReportsNeededToBecomeTraitor);
    }

    private TraitorReport.TraitorReportOperationResponse buildResponse(
            RebelSoldier suspectToBeTraitor,
            Integer amountOfReports,
            Integer amountOfReportsNeededToBeTraitor,
            String message) {
        TraitorReport.TraitorReportOperationResponse response = new TraitorReport.TraitorReportOperationResponse();
        response.setReportedRebelIsATraitor(suspectToBeTraitor.isTraitor());
        response.setAmountOfReports(amountOfReports);
        response.setTraitorName(suspectToBeTraitor.getName());
        response.setMissingReportsToBecomeTraitor((amountOfReportsNeededToBeTraitor - amountOfReports));
        response.setMessage(message);
        return response;
    }
}
