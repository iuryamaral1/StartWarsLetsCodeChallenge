package com.letscode.starwarsresistence.gateways.postgres;

import com.letscode.starwarsresistence.domain.BusinessConfiguration;
import com.letscode.starwarsresistence.repositories.BusinessConfigurationRepository;
import com.letscode.starwarsresistence.usecases.BusinessConfigurationGateway;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BusinessConfigurationPostgresGateway implements BusinessConfigurationGateway {

    private BusinessConfigurationRepository repository;

    public BusinessConfigurationPostgresGateway(BusinessConfigurationRepository repository) {
        this.repository = repository;
    }

    @Override
    public BusinessConfiguration findByDescription(String description) {
        return this.repository.findByDescription(description);
    }

    @Override
    public List<BusinessConfiguration> loadItemValues(List<String> configsToBeLoaded) {
        List<BusinessConfiguration> configurations = new ArrayList<>();
        this.repository.loadItemValues(configsToBeLoaded).forEach(configurations::add);
        return configurations;
    }
}
