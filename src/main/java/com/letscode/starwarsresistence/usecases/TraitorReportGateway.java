package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.TraitorReport;

import java.util.List;
import java.util.UUID;

public interface TraitorReportGateway {

    public Integer findAmountOfReportsForRebel(UUID rebel);
    public TraitorReport saveTraitorReport(TraitorReport traitorReport);
    public List<TraitorReport> findByReporterIdAndTraitorId(UUID reporterId, UUID traitorId);
}
