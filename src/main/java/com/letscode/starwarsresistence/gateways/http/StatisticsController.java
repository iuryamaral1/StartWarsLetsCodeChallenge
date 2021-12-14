package com.letscode.starwarsresistence.gateways.http;

import com.letscode.starwarsresistence.domain.StatisticsContext;
import com.letscode.starwarsresistence.usecases.ManageStatistics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("Statistics controller")
@ApiOperation("Provides important business statistics")
@RequestMapping("/v1/statistics")
public class StatisticsController {

    private ManageStatistics manageStatistics;

    @Autowired
    public StatisticsController(ManageStatistics manageStatistics) {
        this.manageStatistics = manageStatistics;
    }

    @GetMapping("/traitors")
    public ResponseEntity<StatisticsContext.EntityPercentageResponse> getTraitorPercentage() {
        return ResponseEntity.ok(this.manageStatistics.getTraitorPercentage());
    }

    @GetMapping("/rebels")
    public ResponseEntity<StatisticsContext.EntityPercentageResponse> getRebelsPercentage() {
        return ResponseEntity.ok(this.manageStatistics.getRebelsPercentage());
    }

    @GetMapping("/items-average")
    public ResponseEntity<StatisticsContext.ItemsAveragePerRebelResponse> getItemsAveragePerRebel() {
        return ResponseEntity.ok(this.manageStatistics.getItemsAveragePerRebel());
    }

    @GetMapping("/lost-points")
    public ResponseEntity<StatisticsContext.LostPointsResponse> getLostPointsBecauseOfTraitors() {
        return ResponseEntity.ok(this.manageStatistics.getLostPointsBecauseOfTraitors());
    }
}
