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

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
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
        request.setBirthDate(Date.from(LocalDate.now().minusYears(25).atStartOfDay().toInstant(ZoneOffset.UTC)));
        Headquarters pleiades = new Headquarters();
        pleiades.setGalaxyName("Pleiades");
        Location pleiadesLocation = new Location();
        pleiadesLocation.setLat("-8.234298734");
        pleiadesLocation.setLng("-34.879687634");
        pleiades.setLocation(pleiadesLocation);
        request.setHeadquarters(pleiades);
        Inventory inventory = new Inventory();
        inventory.setId(UUID.randomUUID());
        request.setInventory(inventory);

        RebelSoldier expectedRebelSoldier = new RebelSoldier();
        expectedRebelSoldier.setName("Any Name");
        expectedRebelSoldier.setInventory(inventory);
        expectedRebelSoldier.setHeadquarters(pleiades);
        expectedRebelSoldier.setBirthDate(Date.from(LocalDate.now().minusYears(25).atStartOfDay().toInstant(ZoneOffset.UTC)));

        Mockito.when(gateway.saveSoldier(Mockito.any())).thenReturn(expectedRebelSoldier);

        RebelSoldier.RebelSoldierResponse result = useCase.createSoldier(request);

        var expectedResult = new RebelSoldier.RebelSoldierResponse(expectedRebelSoldier);

        Mockito.verify(gateway, Mockito.times(1)).saveSoldier(Mockito.any());
        Assertions.assertEquals(expectedResult.getCompleteName(), result.getCompleteName());
        Assertions.assertEquals(expectedResult.getSoldierGender(), result.getSoldierGender());
        Assertions.assertEquals(expectedResult.getNick(), result.getNick());
        Assertions.assertEquals(expectedResult.getAge(), result.getAge());
    }

    @Test
    public void throw_error_when_inventory_request_is_null() {
        RebelSoldier.RebelSoldierRequest request = new RebelSoldier.RebelSoldierRequest();

        Assertions.assertThrows(ApplicationBusinessException.class, () -> {
            useCase.createSoldier(request);
        });

        Mockito.verify(gateway, Mockito.never()).saveSoldier(Mockito.any());
    }
}
