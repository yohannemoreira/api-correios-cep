package buscadorcep.controller;

import buscadorcep.exception.NoContentException;
import buscadorcep.exception.NotReadyException;
import buscadorcep.model.Address;
import buscadorcep.service.CorreiosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorreiosController {

    @Autowired //injeção de dependencia
    private CorreiosService service;
    @GetMapping("/status")
    public String status(){
        return "Service Status: " + this.service.getStatus();
    }

    @GetMapping("zip/{zipcode}")
    public Address getByZipcode (@PathVariable("zipcode") String zipcode) throws NoContentException, NotReadyException {
        return this.service.getAddressByZipcode(zipcode);
    }
}
