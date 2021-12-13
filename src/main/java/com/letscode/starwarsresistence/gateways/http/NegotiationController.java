package com.letscode.starwarsresistence.gateways.http;

import com.letscode.starwarsresistence.domain.Inventory;
import com.letscode.starwarsresistence.usecases.ManageNegotiation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/negotiation")
@Api("Negotiation Controller")
@ApiOperation("Negotiate items")
public class NegotiationController {

    private ManageNegotiation manageNegotiation;

    @Autowired
    public NegotiationController(ManageNegotiation manageNegotiation) {
        this.manageNegotiation = manageNegotiation;
    }

    @PostMapping
    @ApiOperation("Let rebels to negotiate items among them")
    public ResponseEntity<Inventory.NegotiationOperationResponse> negotiate(
            @RequestBody Inventory.NegotiationRequest request
            ) {
        return ResponseEntity.ok(this.manageNegotiation.negotiateItems(request));
    }
}
