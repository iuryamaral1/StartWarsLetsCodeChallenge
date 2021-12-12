package com.letscode.starwarsresistence.gateways.postgres;

import com.letscode.starwarsresistence.domain.BusinessConfiguration;
import com.letscode.starwarsresistence.repositories.BusinessConfigurationRepository;
import com.letscode.starwarsresistence.usecases.BusinessConfigurationGateway;
import org.springframework.stereotype.Component;

@Component
public class BusinessConfigurationPostgresGateway implements BusinessConfigurationGateway {

    private BusinessConfigurationRepository repository;

    public BusinessConfigurationPostgresGateway(BusinessConfigurationRepository repository) {
        this.repository = repository;
    }

    public BusinessConfiguration findByDescription(String description) {
        return this.repository.findByDescription(description);
    }
}
