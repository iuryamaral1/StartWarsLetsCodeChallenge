package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.BusinessConfiguration;

public interface BusinessConfigurationGateway {

    public BusinessConfiguration findByDescription(String description);
}
