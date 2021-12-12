package com.letscode.starwarsresistence.repositories;

import com.letscode.starwarsresistence.domain.TraitorReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TraitorReportRepository extends CrudRepository<TraitorReport, UUID> {

    @Query("SELECT count(tr) FROM TraitorReport tr WHERE tr.traitor.id = ?1")
    public Integer findAmountOfReportsForRebel(UUID traitorId);

    @Query("SELECT tr FROM TraitorReport tr WHERE tr.rebelReporter.id = ?1 AND tr.traitor.id = ?2")
    public Iterable<TraitorReport> findByReporterIdAndTraitorId(UUID reporterId, UUID traitorId);
}
