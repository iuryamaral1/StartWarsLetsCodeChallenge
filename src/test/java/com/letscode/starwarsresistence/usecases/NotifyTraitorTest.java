package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.Inventory;
import com.letscode.starwarsresistence.domain.Item;
import com.letscode.starwarsresistence.domain.RebelSoldier;
import com.letscode.starwarsresistence.domain.TraitorReport;
import com.letscode.starwarsresistence.domain.exceptions.ApplicationBusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class NotifyTraitorTest {

    @Mock
    private ManageRebelSoldier manageRebelSoldier;

    @Mock
    private TraitorReportGateway traitorReportGateway;

    @Mock
    private ManageBusinessConfiguration manageBusinessConfiguration;

    @InjectMocks
    private NotifyTraitor notifyTraitor;

    @Test
    public void should_throw_error_when_soldier_tries_report_himself() {
        var reporterId = UUID.randomUUID();
        var traitorId = reporterId;

        TraitorReport.TraitorReportRequest request = new TraitorReport.TraitorReportRequest(reporterId, traitorId);

        Mockito.when(manageRebelSoldier.findById(Mockito.any())).thenReturn(Optional.of(new RebelSoldier()));

        Assertions.assertThrows(ApplicationBusinessException.class, () -> {
            notifyTraitor.notify(request);
        }, "You can't report!");
    }

    @Test
    public void should_throw_error_when_soldier_does_not_exist() {
        var reporterId = UUID.randomUUID();
        var traitorId = UUID.randomUUID();

        TraitorReport.TraitorReportRequest request = new TraitorReport.TraitorReportRequest(reporterId, traitorId);

        Mockito.when(manageRebelSoldier.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(ApplicationBusinessException.class, () -> {
            notifyTraitor.notify(request);
        }, "You can't report!");
    }

    @Test
    public void should_throw_error_when_traitor_suspect_doesnt_exist() {
        var reporterId = UUID.randomUUID();
        var traitorId = UUID.randomUUID();

        TraitorReport.TraitorReportRequest request = new TraitorReport.TraitorReportRequest(reporterId, traitorId);

        Mockito.when(manageRebelSoldier.findById(reporterId)).thenReturn(
                Optional.of(new RebelSoldier())
        );

        Mockito.when(manageRebelSoldier.findById(traitorId)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(ApplicationBusinessException.class, () -> {
            notifyTraitor.notify(request);
        }, "You can't report!");
    }

    @Test
    public void should_throw_error_when_soldier_is_traitor_trying_to_report() {
        var reporterId = UUID.randomUUID();
        var traitorId = UUID.randomUUID();

        var reporterIsTraitorSoldier = new RebelSoldier();
        reporterIsTraitorSoldier.setTraitor(true);

        TraitorReport.TraitorReportRequest request = new TraitorReport.TraitorReportRequest(reporterId, traitorId);

        Mockito.when(manageRebelSoldier.findById(Mockito.any())).thenReturn(
                Optional.of(
                        reporterIsTraitorSoldier
                )
        );

        Assertions.assertThrows(ApplicationBusinessException.class, () -> {
           notifyTraitor.notify(request);
        }, "You can't report!");
    }

    @Test
    public void should_return_specific_message_when_soldier_tries_report_an_already_traitor() throws ApplicationBusinessException {
        var reporterId = UUID.randomUUID();
        var traitorId = UUID.randomUUID();

        var reporter = new RebelSoldier();

        var traitor = new RebelSoldier();
        traitor.setName("Luke");
        traitor.setTraitor(true);

        var expectedAnswer = new TraitorReport.TraitorReportOperationResponse();
        expectedAnswer.setMessage("This rebel is already marked as a traitor. Thanks for your report!");
        expectedAnswer.setTraitorName("Luke");
        expectedAnswer.setMissingReportsToBecomeTraitor(0);

        Mockito.when(manageRebelSoldier.findById(reporterId)).thenReturn(
                Optional.of(reporter)
        );

        Mockito.when(manageRebelSoldier.findById(traitorId)).thenReturn(
                Optional.of(traitor)
        );

        TraitorReport.TraitorReportRequest request = new TraitorReport.TraitorReportRequest(reporterId, traitorId);

        var result = notifyTraitor.notify(request);

        Assertions.assertEquals(expectedAnswer.getMessage(), result.getMessage());
        Assertions.assertEquals(expectedAnswer.getTraitorName(), result.getTraitorName());
        Assertions.assertEquals(expectedAnswer.getMissingReportsToBecomeTraitor(), result.getMissingReportsToBecomeTraitor());
    }

    @Test
    public void should_not_report_repeatedly() {
        var reporterId = UUID.randomUUID();
        var traitorId = UUID.randomUUID();

        var reporter = new RebelSoldier();
        reporter.setId(reporterId);

        var traitor = new RebelSoldier();
        traitor.setId(traitorId);
        traitor.setTraitor(false);

        var report = new TraitorReport();
        report.setTraitor(traitor);
        report.setRebelReporter(reporter);
        var reportList = List.of(report);

        var request = new TraitorReport.TraitorReportRequest(reporterId, traitorId);

        Mockito.when(manageRebelSoldier.findById(reporterId)).thenReturn(Optional.of(reporter));
        Mockito.when(manageRebelSoldier.findById(traitorId)).thenReturn(Optional.of(traitor));
        Mockito.when(traitorReportGateway.findByReporterIdAndTraitorId(reporterId, traitorId)).thenReturn(reportList);

        Assertions.assertThrows(ApplicationBusinessException.class, () -> {
           notifyTraitor.notify(request);
        });
    }

    @Test
    public void should_mark_as_traitor_when_there_is_enough_reports() throws ApplicationBusinessException {
        var reporterId = UUID.randomUUID();
        var traitorId = UUID.randomUUID();

        var request = new TraitorReport.TraitorReportRequest(reporterId, traitorId);

        var expectedAnswer = new TraitorReport.TraitorReportOperationResponse();
        expectedAnswer.setReportedRebelIsATraitor(true);
        expectedAnswer.setAmountOfReports(3);
        expectedAnswer.setTraitorName("Oola");
        expectedAnswer.setMissingReportsToBecomeTraitor(0);
        expectedAnswer.setMessage("This soldier was marked as a traitor!!! Thanks for your report!");

        var suspectToBeTraitor = new RebelSoldier();
        suspectToBeTraitor.setId(traitorId);
        suspectToBeTraitor.setName("Oola");

        var reporter = new RebelSoldier();
        reporter.setId(reporterId);

        var traitorReport = new TraitorReport();
        traitorReport.setRebelReporter(reporter);
        traitorReport.setTraitor(suspectToBeTraitor);

        var suspectToBeTraitorMarkedAsTraitor = new RebelSoldier();
        suspectToBeTraitorMarkedAsTraitor.setId(traitorId);
        suspectToBeTraitorMarkedAsTraitor.setTraitor(true);
        suspectToBeTraitorMarkedAsTraitor.setName("Oola");
        var inventory = new Inventory();
        inventory.setItems(Set.of(new Item()));
        inventory.setNegotiable(false);
        inventory.setRebelSoldier(suspectToBeTraitorMarkedAsTraitor);
        suspectToBeTraitorMarkedAsTraitor.setInventory(inventory);

        Mockito.when(manageRebelSoldier.findById(reporterId)).thenReturn(Optional.of(reporter));
        Mockito.when(manageRebelSoldier.findById(traitorId)).thenReturn(Optional.of(suspectToBeTraitor));
        Mockito.when(traitorReportGateway.findByReporterIdAndTraitorId(reporterId, traitorId)).thenReturn(new ArrayList<>());
        Mockito.when(traitorReportGateway.saveTraitorReport(Mockito.any())).thenReturn(traitorReport);
        Mockito.when(traitorReportGateway.findAmountOfReportsForRebel(traitorId)).thenReturn(3);
        Mockito.when(manageBusinessConfiguration.amountOfReportsNeededToBecomeTraitor()).thenReturn(3);
        Mockito.when(manageRebelSoldier.markSoldierAsTraitor(suspectToBeTraitor)).thenReturn(suspectToBeTraitorMarkedAsTraitor);

        var result = notifyTraitor.notify(request);

        Mockito.verify(manageRebelSoldier, Mockito.times(1)).markSoldierAsTraitor(Mockito.any());
        Assertions.assertEquals(expectedAnswer.getAmountOfReports(), result.getAmountOfReports());
        Assertions.assertEquals(expectedAnswer.getTraitorName(), result.getTraitorName());
        Assertions.assertEquals(expectedAnswer.getMissingReportsToBecomeTraitor(), result.getMissingReportsToBecomeTraitor());
        Assertions.assertEquals(expectedAnswer.getMessage(), result.getMessage());
    }

    @Test
    public void should_report_traitor_successfully() throws ApplicationBusinessException {
        var reporterId = UUID.randomUUID();
        var traitorId = UUID.randomUUID();

        var reporter = new RebelSoldier();
        reporter.setId(reporterId);

        var traitor = new RebelSoldier();
        traitor.setId(traitorId);
        traitor.setName("Chewbacca");

        var request = new TraitorReport.TraitorReportRequest(reporterId, traitorId);

        var expectedAnswer = new TraitorReport.TraitorReportOperationResponse();
        expectedAnswer.setTraitorName("Chewbacca");
        expectedAnswer.setReportedRebelIsATraitor(false);
        expectedAnswer.setAmountOfReports(1);
        expectedAnswer.setMissingReportsToBecomeTraitor(2);
        expectedAnswer.setMessage("Your report has been saved!");

        Mockito.when(manageRebelSoldier.findById(traitorId)).thenReturn(Optional.of(traitor));
        Mockito.when(manageRebelSoldier.findById(reporterId)).thenReturn(Optional.of(reporter));
        Mockito.when(traitorReportGateway.findByReporterIdAndTraitorId(reporterId, traitorId)).thenReturn(new ArrayList<>());
        Mockito.when(traitorReportGateway.saveTraitorReport(Mockito.any())).thenReturn(new TraitorReport());
        Mockito.when(traitorReportGateway.findAmountOfReportsForRebel(traitorId)).thenReturn(1);
        Mockito.when(manageBusinessConfiguration.amountOfReportsNeededToBecomeTraitor()).thenReturn(3);

        var result = notifyTraitor.notify(request);

        Mockito.verify(manageRebelSoldier, Mockito.never()).markSoldierAsTraitor(Mockito.any());
        Assertions.assertEquals(expectedAnswer.getAmountOfReports(), result.getAmountOfReports());
        Assertions.assertEquals(expectedAnswer.getTraitorName(), result.getTraitorName());
        Assertions.assertEquals(expectedAnswer.getMissingReportsToBecomeTraitor(), result.getMissingReportsToBecomeTraitor());
        Assertions.assertEquals(expectedAnswer.getMessage(), result.getMessage());
    }
}
