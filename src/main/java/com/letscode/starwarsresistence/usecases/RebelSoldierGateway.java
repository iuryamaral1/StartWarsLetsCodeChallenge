package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.RebelSoldier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RebelSoldierGateway {

    public RebelSoldier saveSoldier(RebelSoldier rebelSoldier);
    public List<RebelSoldier> findAll();
    public Optional<RebelSoldier> findById(UUID id);
}
