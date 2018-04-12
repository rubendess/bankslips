package com.bankslips.web;

import com.bankslips.dto.BankSlipDTO;
import com.bankslips.exceptions.RestNotFoundException;
import com.bankslips.service.BankSlipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin("*")
public class BankSlipController {

    @Autowired
    BankSlipService bankSlipService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getAllBankSlips() {
        return ResponseEntity.status(HttpStatus.OK).body(bankSlipService.getAllBankSlips());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getBankSlipById(@PathVariable("id") String bankSlipId) {
        UUID id = UUID.fromString(bankSlipId);

        return ResponseEntity.status(HttpStatus.OK).body(bankSlipService.getBankSlipDetailsById(id));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createBankSlip(@RequestBody BankSlipDTO body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bankSlipService.createBankSlip(body));
    }

    @RequestMapping(value = "/{id}/pay", method = RequestMethod.PUT)
    public ResponseEntity<?> payBankSlip(@RequestBody BankSlipDTO body, @PathVariable("id") String bankSlipId) throws RestNotFoundException {
        body.setId(bankSlipId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(bankSlipService.payBankSlip(body));
    }

    @RequestMapping(value = "/{id}/cancel", method = RequestMethod.DELETE)
    public ResponseEntity<?> cancelBankSlip(@PathVariable("id") String bankSlipId) throws RestNotFoundException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(bankSlipService.cancelBankSlip(new BankSlipDTO(bankSlipId)));
    }

}
