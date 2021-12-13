package com.letscode.starwarsresistence.gateways.http;

import com.letscode.starwarsresistence.domain.TraitorReport;
import com.letscode.starwarsresistence.domain.exceptions.ApplicationBusinessException;
import com.letscode.starwarsresistence.usecases.NotifyTraitor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/report")
@Api("Show resources for reporting a traitor")
@ApiOperation("Traitor Report Controller")
public class TraitorReportController {

    private NotifyTraitor notifyTraitor;

    @Autowired
    public TraitorReportController(NotifyTraitor notifyTraitor) {
        this.notifyTraitor = notifyTraitor;
    }

    @PostMapping("/traitor")
    @ApiOperation(value = "Report a traitor")
    public ResponseEntity<TraitorReport.TraitorReportOperationResponse> reportTraitor(
            @RequestBody TraitorReport.TraitorReportRequest request
    ) throws ApplicationBusinessException {
        return ResponseEntity.ok(notifyTraitor.notify(request));
    }
}
