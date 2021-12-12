package com.letscode.starwarsresistence.gateways.postgres;

import com.letscode.starwarsresistence.domain.TraitorReport;
import com.letscode.starwarsresistence.repositories.TraitorReportRepository;
import com.letscode.starwarsresistence.usecases.TraitorReportGateway;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class TraitorReportPostgresGateway implements TraitorReportGateway {

    private TraitorReportRepository repository;

    public TraitorReportPostgresGateway(TraitorReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public Integer findAmountOfReportsForRebel(UUID rebelId) {
        return this.repository.findAmountOfReportsForRebel(rebelId);
    }

    @Override
    public TraitorReport saveTraitorReport(TraitorReport traitorReport) {
        return repository.save(traitorReport);
    }

    @Override
    public List<TraitorReport> findByReporterIdAndTraitorId(UUID reporterId, UUID traitorId) {
        List<TraitorReport> reports = new ArrayList<>();
        this.repository.findByReporterIdAndTraitorId(reporterId, traitorId).forEach(reports::add);
        return reports;
    }
}
