package com.letscode.starwarsresistence.gateways.http;

import com.letscode.starwarsresistence.domain.Location;
import com.letscode.starwarsresistence.domain.RebelSoldier;
import com.letscode.starwarsresistence.domain.exceptions.ApplicationBusinessException;
import com.letscode.starwarsresistence.usecases.ManageRebelSoldier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/rebels")
public class RebelSoldierController {

    @Autowired
    private ManageRebelSoldier manageRebelSoldier;

    @GetMapping
    public ResponseEntity<List<RebelSoldier.RebelSoldierResponse>> findAllRebel(@NotNull final Pageable pageable) {
        return ResponseEntity.ok(manageRebelSoldier.findAllRebelSoldiers());
    }

    @PostMapping
    public ResponseEntity<RebelSoldier.RebelSoldierResponse> create(@RequestBody RebelSoldier.RebelSoldierRequest request) throws ApplicationBusinessException {
        return ResponseEntity.ok(this.manageRebelSoldier.createSoldier(request));
    }

    @PutMapping("/{rebelId}/report-location")
    public ResponseEntity<RebelSoldier.RebelSoldierResponse> reportLastLocation(
            @PathVariable("rebelId") String rebelId,
            @RequestBody Location.LocationRequest request
    ) throws ApplicationBusinessException {
        return ResponseEntity.ok(this.manageRebelSoldier.updateLastLocation(request, UUID.fromString(rebelId)));
    }
}
