package com.letscode.starwarsresistence.gateways;

import com.letscode.starwarsresistence.IntegrationTest;
import com.letscode.starwarsresistence.domain.Inventory;
import com.letscode.starwarsresistence.domain.Location;
import com.letscode.starwarsresistence.domain.RebelSoldier;
import com.letscode.starwarsresistence.repositories.RebelSoldierRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.flywaydb.core.Flyway;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class RebelSoldierPostgresGatewayTest extends IntegrationTest {

    private RebelSoldierRepository repository;

    @Autowired
    public RebelSoldierPostgresGatewayTest(RebelSoldierRepository repository) {
        this.repository = repository;
    }

    @Test
    public void should_save_rebel_soldier_on_database() {
        var id = UUID.randomUUID();
        RebelSoldier rebelSoldier = new RebelSoldier();
        rebelSoldier.setBirthDate(Date.from(LocalDate.now().minusYears(15).atStartOfDay().toInstant(ZoneOffset.UTC)));
        rebelSoldier.setLocation(new Location(-8.765765, -34.989876));
        rebelSoldier.setName("Any name");
        rebelSoldier.setNickName("AnyNickName");
        rebelSoldier.setGender(RebelSoldier.RebelSoldierGender.HUMAN);

        rebelSoldier.setId(id);

        Inventory inventory = new Inventory();
        rebelSoldier.setInventory(inventory);
        inventory.setRebelSoldier(rebelSoldier);

        repository.save(rebelSoldier);

        RebelSoldier expectedSoldier = new RebelSoldier();
        expectedSoldier.setGender(RebelSoldier.RebelSoldierGender.HUMAN);
        expectedSoldier.setBirthDate(Date.from(LocalDate.now().minusYears(15).atStartOfDay().toInstant(ZoneOffset.UTC)));
        expectedSoldier.setLocation(new Location(-8.765765, -34.989876));
        expectedSoldier.setName("Any Name");
        expectedSoldier.setNickName("AnyNickName");
        expectedSoldier.setId(id);
        expectedSoldier.setInventory(inventory);
        inventory.setRebelSoldier(expectedSoldier);

        Optional<RebelSoldier> optionalRebelSoldier = repository.findById(id);
        RebelSoldier rebel = optionalRebelSoldier.get();

        Assertions.assertNotNull(rebel);
        Assertions.assertTrue(expectedSoldier.getId().equals(rebel.getId()));
        Assertions.assertTrue(expectedSoldier.getInventory().getId().equals(rebel.getInventory().getId()));
        Assertions.assertNotNull(rebel.getInventory().getId());
        Assertions.assertNotNull(rebel.getInventory().getItems());
    }
}
