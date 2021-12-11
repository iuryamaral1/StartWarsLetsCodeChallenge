package com.letscode.starwarsresistence.gateways.postgres;

import com.letscode.starwarsresistence.domain.RebelSoldier;
import com.letscode.starwarsresistence.repositories.RebelSoldierRepository;
import com.letscode.starwarsresistence.usecases.RebelSoldierGateway;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RebelSoldierPostgresGateway implements RebelSoldierGateway {

    private RebelSoldierRepository repository;

    public RebelSoldierPostgresGateway(RebelSoldierRepository repository) {
        this.repository = repository;
    }

    @Override
    public RebelSoldier saveSoldier(RebelSoldier rebelSoldier) {
        return repository.save(rebelSoldier);
    }

    @Override
    public List<RebelSoldier> findAll() {
        List<RebelSoldier> rebelSoldiers = new ArrayList<>();
        repository.findAll().forEach(rebelSoldiers::add);
        return rebelSoldiers;
    }

    @Override
    public Optional<RebelSoldier> findById(UUID id) {
        return this.repository.findById(id);
    }
}
