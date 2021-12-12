package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.Location;
import com.letscode.starwarsresistence.domain.RebelSoldier;
import com.letscode.starwarsresistence.domain.exceptions.ApplicationBusinessException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ManageRebelSoldier {

    private RebelSoldierGateway gateway;

    public ManageRebelSoldier(RebelSoldierGateway gateway) {
        this.gateway = gateway;
    }

    public RebelSoldier.RebelSoldierResponse createSoldier(RebelSoldier.@NotNull RebelSoldierRequest request) throws ApplicationBusinessException {
        if (request.getInventory() == null) {
            throw new ApplicationBusinessException("You need to declare your inventory");
        }

        return new RebelSoldier.RebelSoldierResponse(this.gateway.saveSoldier(request.toRebelSoldier()));
    }

    public List<RebelSoldier.RebelSoldierResponse> findAllRebelSoldiers() {
        return this.gateway.findAll().stream().map(RebelSoldier.RebelSoldierResponse::new).collect(Collectors.toList());
    }

    public Optional<RebelSoldier> findById(UUID id) {
        return this.gateway.findById(id);
    }

    public RebelSoldier.RebelSoldierResponse updateLastLocation(Location.LocationRequest request, UUID rebelId) throws ApplicationBusinessException {
        var optionalRebel = findById(rebelId);
        if (!optionalRebel.isPresent()) throw new ApplicationBusinessException("This rebel does not exist");
        var rebel = optionalRebel.get();
        rebel.setLocation(new Location(request.getLatitude(), request.getLongitude()));
        return new RebelSoldier.RebelSoldierResponse(this.gateway.saveSoldier(rebel));
    }

    public RebelSoldier markSoldierAsTraitor(RebelSoldier rebelSoldier) {
        rebelSoldier.setTraitor(true);
        rebelSoldier.getInventory().setNegotiable(false);
        return this.gateway.saveSoldier(rebelSoldier);
    }
}
