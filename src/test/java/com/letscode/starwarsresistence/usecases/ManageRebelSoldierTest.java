package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.Headquarters;
import com.letscode.starwarsresistence.domain.Inventory;
import com.letscode.starwarsresistence.domain.Location;
import com.letscode.starwarsresistence.domain.RebelSoldier;
import com.letscode.starwarsresistence.domain.exceptions.ApplicationBusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ManageRebelSoldierTest {

    @Mock
    private RebelSoldierGateway gateway;

    @InjectMocks
    private ManageRebelSoldier useCase;

    @Test
    public void should_create_soldier_successfully() throws ApplicationBusinessException {
        RebelSoldier.RebelSoldierRequest request = new RebelSoldier.RebelSoldierRequest();
        request.setGender(RebelSoldier.RebelSoldierGender.ALIEN);
        Headquarters pleiades = new Headquarters();
        pleiades.setGalaxyName("Pleiades");
        Location pleiadesLocation = new Location();
        pleiadesLocation.setLatitude(-8.234298734);
        pleiadesLocation.setLongitude(-34.879687634);
        pleiades.setLocation(pleiadesLocation);
        request.setHeadquarters(pleiades);
        Inventory inventory = new Inventory();
        inventory.setId(UUID.randomUUID());
        request.setInventory(inventory);

        RebelSoldier expectedRebelSoldier = new RebelSoldier();
        expectedRebelSoldier.setName("Any Name");
        expectedRebelSoldier.setInventory(inventory);
        expectedRebelSoldier.setHeadquarters(pleiades);

        Mockito.when(gateway.createSoldier(Mockito.any())).thenReturn(expectedRebelSoldier);

        RebelSoldier result = useCase.createSoldier(request);

        Mockito.verify(gateway, Mockito.times(1)).createSoldier(Mockito.any());
        Assertions.assertEquals(result, expectedRebelSoldier);
    }

    @Test
    public void throw_error_when_trying_create_soldier_with_null_request() {
        RebelSoldier.RebelSoldierRequest request = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            useCase.createSoldier(request);
        });

        Mockito.verify(gateway, Mockito.never()).createSoldier(Mockito.any());
    }

    @Test
    public void throw_error_when_inventory_request_is_null() {
        RebelSoldier.RebelSoldierRequest request = new RebelSoldier.RebelSoldierRequest();

        Assertions.assertThrows(ApplicationBusinessException.class, () -> {
            useCase.createSoldier(request);
        });

        Mockito.verify(gateway, Mockito.never()).createSoldier(Mockito.any());
    }
}
