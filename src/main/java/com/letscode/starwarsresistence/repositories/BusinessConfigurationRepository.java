package com.letscode.starwarsresistence.repositories;

import com.letscode.starwarsresistence.domain.BusinessConfiguration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BusinessConfigurationRepository extends CrudRepository<BusinessConfiguration, UUID> {

    public BusinessConfiguration findByDescription(String description);

    @Query("SELECT bc FROM BusinessConfiguration bc WHERE bc.description IN :configurations")
    public Iterable<BusinessConfiguration> loadItemValues(List<String> configurations);
}
