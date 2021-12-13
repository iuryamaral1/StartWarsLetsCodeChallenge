package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.BusinessConfiguration;

import java.util.List;

public interface BusinessConfigurationGateway {

    public BusinessConfiguration findByDescription(String description);
    public List<BusinessConfiguration> loadItemValues(List<String> configsToBeLoaded);
}
