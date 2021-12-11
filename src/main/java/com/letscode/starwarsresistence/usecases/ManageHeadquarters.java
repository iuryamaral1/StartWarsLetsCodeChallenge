package com.letscode.starwarsresistence.usecases;

import com.letscode.starwarsresistence.domain.Headquarters;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class ManageHeadquarters {

    private HeadquartersGateway gateway;

    public ManageHeadquarters(HeadquartersGateway gateway) {
        this.gateway = gateway;
    }

    public Headquarters createHeadquarters(Headquarters.@NotNull HeadquartersRequest request) {
        return this.gateway.createHeadquarters(request.toHeadquarters());
    }
}
