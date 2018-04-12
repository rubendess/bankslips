package com.bankslips.service;

import com.bankslips.domain.BankSlip;
import com.bankslips.dto.BankSlipDTO;
import com.bankslips.enums.EnumBankSlipStatus;
import com.bankslips.exceptions.RestBadRequestException;
import com.bankslips.exceptions.RestInvalidItemException;
import com.bankslips.exceptions.RestNotFoundException;
import com.bankslips.helper.ConvertHelper;
import com.bankslips.repo.BankSlipRepo;
import com.bankslips.utils.DateTimeUtils;
import com.google.common.base.Enums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BankSlipService {

    @Autowired
    BankSlipRepo bankSlipRepo;

    @Autowired
    ConvertHelper convertHelper;

    public void validateBankSlips(BankSlipDTO bankSlipDTO) {
        if(bankSlipDTO == null || bankSlipDTO.getDueDate() == null || bankSlipDTO.getCustomer() == null || bankSlipDTO.getStatus() == null)
            throw new RestInvalidItemException("Check if you fill all the fields.");

        if(!Enums.getIfPresent(EnumBankSlipStatus.class, bankSlipDTO.getStatus()).isPresent())
            throw new RestInvalidItemException("Invalid value to Status.");
    }

    private long getDaysOfExpired(BankSlipDTO bankSlipDTO) {
        //long days = Duration.between(bankSlipDTO.getDueDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
        long daysDiff = DateTimeUtils.getDaysDiff(bankSlipDTO.getDueDate(), new Date());
        return daysDiff < 0 ? 0 : daysDiff;
    }

    public double calculateFine(BankSlipDTO bankSlipDTO) {
        if(!bankSlipDTO.getStatus().equals(EnumBankSlipStatus.PENDING.name()))
            return 0D;

        long days = this.getDaysOfExpired(bankSlipDTO);
        if(days == 0)
            return 0D;

        double fine = 0D;
        if(days < 10) {
            fine = bankSlipDTO.getTotalInCents() * 0.005;
        } else {
            fine = bankSlipDTO.getTotalInCents() * 0.01;
        }

        return fine;
    }

    public List getAllBankSlips() {
        return bankSlipRepo.findAll()
                .stream()
                .map(bankSlip ->
                    {
                        BankSlipDTO bankSlipDTO = convertHelper.convert(bankSlip, BankSlipDTO.class);
                        bankSlipDTO.setFine(this.calculateFine(bankSlipDTO));
                        return bankSlipDTO;
                    })
                .collect(Collectors.toList());
    }

    private BankSlip getBankSlipById(UUID id) {
        if (!bankSlipRepo.existsById(id))
            throw new RestNotFoundException("Bank slip not found! Id: " + id);

        return bankSlipRepo.getOne(id);
    }

    public BankSlipDTO getBankSlipDetailsById(UUID id) {
        BankSlip bankSlip = this.getBankSlipById(id);

        BankSlipDTO bankSlipDTO = convertHelper.convert(bankSlip, BankSlipDTO.class);
        bankSlipDTO.setFine(this.calculateFine(bankSlipDTO));

        return bankSlipDTO;
    }

    public BankSlipDTO createBankSlip(BankSlipDTO bankSlipDTO) {
        this.validateBankSlips(bankSlipDTO);

        BankSlip bankSlip = convertHelper.convert(bankSlipDTO, BankSlip.class);
        return convertHelper.convert(bankSlipRepo.save(bankSlip), BankSlipDTO.class);
    }

    public BankSlipDTO payBankSlip(BankSlipDTO bankSlipDTO) throws RestNotFoundException {
        return convertHelper.convert(this.updateStatusBankSlip(bankSlipDTO, EnumBankSlipStatus.PAID), BankSlipDTO.class);
    }

    public BankSlipDTO cancelBankSlip(BankSlipDTO bankSlipDTO) throws RestNotFoundException {
        return convertHelper.convert(this.updateStatusBankSlip(bankSlipDTO, EnumBankSlipStatus.CANCELED), BankSlipDTO.class);
    }

    private BankSlip updateStatusBankSlip(BankSlipDTO bankSlipDTO, EnumBankSlipStatus status) throws RestNotFoundException {
        if(bankSlipDTO.getId() == null || bankSlipDTO.getId().toString().isEmpty())
            throw new RestBadRequestException("The Id parameter is required to update a bank slip!");

        BankSlip bankSlipDB = this.getBankSlipById(bankSlipDTO.getIdAsUUID());
        bankSlipDB.setStatus(status);
        return bankSlipRepo.save(bankSlipDB);
    }
}
