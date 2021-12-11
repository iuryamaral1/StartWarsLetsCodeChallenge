package com.letscode.starwarsresistence.gateways;

import com.letscode.starwarsresistence.domain.RebelSoldier;
import com.letscode.starwarsresistence.repositories.RebelSoldierRepository;
import com.letscode.starwarsresistence.usecases.RebelSoldierGateway;
import org.springframework.stereotype.Component;

@Component
public class RebelSoldierPostgresGateway implements RebelSoldierGateway {

    private RebelSoldierRepository repository;

    public RebelSoldierPostgresGateway(RebelSoldierRepository repository) {
        this.repository = repository;
    }

    @Override
    public RebelSoldier createSoldier(RebelSoldier rebelSoldier) {
        return repository.save(rebelSoldier);
    }
}
