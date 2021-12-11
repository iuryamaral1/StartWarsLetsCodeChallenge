package com.letscode.starwarsresistence.gateways.postgres;

import com.letscode.starwarsresistence.domain.Headquarters;
import com.letscode.starwarsresistence.repositories.HeadquartersRepository;
import com.letscode.starwarsresistence.usecases.HeadquartersGateway;
import org.springframework.stereotype.Component;

@Component
public class HeadquartersPostgresGateway implements HeadquartersGateway {

    private HeadquartersRepository repository;

    public HeadquartersPostgresGateway(HeadquartersRepository repository) {
        this.repository = repository;
    }

    @Override
    public Headquarters createHeadquarters(Headquarters headquarters) {
        return repository.save(headquarters);
    }
}
