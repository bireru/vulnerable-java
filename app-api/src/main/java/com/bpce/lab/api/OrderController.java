package com.bpce.lab.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Couche API.
 * USED : jackson-databind (ObjectMapper) + spring-web.
 * La desertialisation d'une entree utilisateur rend la CVE jackson EXPLOITABLE
 * => vrai positif Critical a corriger en priorite.
 */
@RestController
public class OrderController {

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/orders")
    public Object create(@RequestBody String body) throws Exception {
        // entree utilisateur desertialisee sans controle => exploitable
        return mapper.readValue(body, Object.class);
    }
}
