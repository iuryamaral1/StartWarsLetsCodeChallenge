package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.BusinessConfiguration;
import org.springframework.stereotype.Component;

@Component
public class ManageBusinessConfiguration {

    private static final String AMOUNT_REPORT_NEEDED_TO_BE_A_TRAITOR = "AMOUNT_REPORT_NEEDED_TO_BE_A_TRAITOR";
    private BusinessConfigurationGateway gateway;

    public ManageBusinessConfiguration(BusinessConfigurationGateway gateway) {
        this.gateway = gateway;
    }

    public Integer amountOfReportsNeededToBecomeTraitor() {
        return this.gateway.findByDescription(AMOUNT_REPORT_NEEDED_TO_BE_A_TRAITOR).getValue();
    }
}
