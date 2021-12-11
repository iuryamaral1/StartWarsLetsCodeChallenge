package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.Location;
import com.letscode.starwarsresistence.domain.RebelSoldier;
import com.letscode.starwarsresistence.domain.exceptions.ApplicationBusinessException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ManageRebelSoldier {

    private RebelSoldierGateway gateway;

    public ManageRebelSoldier(RebelSoldierGateway gateway) {
        this.gateway = gateway;
    }

    public RebelSoldier createSoldier(RebelSoldier.@NotNull RebelSoldierRequest request) throws ApplicationBusinessException {
        if (request.getInventory() == null) {
            throw new ApplicationBusinessException("You need to declare your inventory");
        }

        return this.gateway.createSoldier(request.toRebelSoldier());
    }

    public List<RebelSoldier> findAllRebelSoldiers() {
        return this.gateway.findAll();
    }

    public Optional<RebelSoldier> findById(UUID id) {
        return this.gateway.findById(id);
    }
}
