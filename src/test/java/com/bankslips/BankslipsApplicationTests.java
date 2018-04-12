package com.bankslips;

import com.bankslips.domain.BankSlip;
import com.bankslips.dto.BankSlipDTO;
import com.bankslips.enums.EnumBankSlipStatus;
import com.bankslips.helper.ConvertHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BankslipsApplicationTests {

	@Autowired
    TestRestTemplate rest;

	@Autowired
    ConvertHelper convertHelper;

	@Autowired
    ObjectMapper objectMapper;

    private synchronized HttpEntity getEntityForSend(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return new HttpEntity(body, headers);
    }

    private BankSlipDTO getDefaultBankSlipDTO() {
        BankSlipDTO bankSlip = new BankSlipDTO();
        bankSlip.setCustomer("Customer 1");
        bankSlip.setDueDate(new Date());
        bankSlip.setTotalInCents(14000);
        bankSlip.setStatus("PENDING");
        return bankSlip;
    }

    private ResponseEntity createBankSlip(Object body) {
        return rest.postForEntity("/", this.getEntityForSend(body), String.class);
    }

    private ResponseEntity payBankSlip(Object body, String bankSlipId) {
        String uri = MessageFormat.format("/{0}/pay", StringUtils.isEmpty(bankSlipId) ? "" : bankSlipId);
        return rest.exchange(uri, HttpMethod.PUT, this.getEntityForSend(body), String.class);
    }

    private ResponseEntity cancelBankSlip(String bankSlipId) {
        String uri = MessageFormat.format("/{0}/cancel", StringUtils.isEmpty(bankSlipId) ? "" : bankSlipId);
        return rest.exchange(uri, HttpMethod.DELETE, this.getEntityForSend(null), String.class);
    }

    private ResponseEntity getAllBankSlipList() {
        return rest.getForEntity("/", List.class);
    }

    private ResponseEntity getBankSlipDetailsById(String bankSlipId) {
        return rest.getForEntity("/"+bankSlipId, String.class);
    }

	@Test
    public void createBankSlipTest() {
        ResponseEntity res = this.createBankSlip(this.getDefaultBankSlipDTO());
        assertTrue(res.getStatusCode().equals(HttpStatus.CREATED));
    }

    @Test
    public void payBankSlipTest() throws IOException {
        BankSlipDTO bankSlipDTO = this.getDefaultBankSlipDTO();

        ResponseEntity resCreate = this.createBankSlip(bankSlipDTO);
        if(resCreate.getStatusCode().equals(HttpStatus.CREATED) && !StringUtils.isEmpty(resCreate.getBody())) {
            BankSlipDTO bankSlip = objectMapper.readValue(resCreate.getBody().toString(), BankSlipDTO.class);
            ResponseEntity resPut = this.payBankSlip(bankSlip, bankSlip.getId());

            assertTrue(resPut.getStatusCode().equals(HttpStatus.NO_CONTENT));
        }
    }

    @Test
    public void payBankSlipNotFoundTest() {
        BankSlipDTO bankSlip = this.getDefaultBankSlipDTO();
        bankSlip.setId("7c83ddff-3798-440a-87fd-b55652c1ab5d");

        ResponseEntity res = this.payBankSlip(bankSlip, bankSlip.getId());
        assertTrue(res.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }

    @Test
    public void cancelBankSlipTest() throws IOException {
        BankSlipDTO bankSlipDTO = this.getDefaultBankSlipDTO();

        ResponseEntity resCreate = this.createBankSlip(bankSlipDTO);
        if(resCreate.getStatusCode().equals(HttpStatus.CREATED) && !StringUtils.isEmpty(resCreate.getBody())) {
            BankSlipDTO bankSlip = objectMapper.readValue(resCreate.getBody().toString(), BankSlipDTO.class);
            ResponseEntity resDelete = this.payBankSlip(bankSlip, bankSlip.getId());

            assertTrue(resDelete.getStatusCode().equals(HttpStatus.NO_CONTENT));
        }
    }

    @Test
    public void cancelBankSlipNotFoundTest() {
        BankSlipDTO bankSlip = this.getDefaultBankSlipDTO();
        bankSlip.setId("7c83ddff-3798-440a-87fd-b55652c1ab5d");

        ResponseEntity res = this.cancelBankSlip(bankSlip.getId());
        assertTrue(res.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }

	@Test
	public void getAllBankSlipListTest() {
        ResponseEntity res = this.getAllBankSlipList();
        assertTrue(res.getStatusCode().equals(HttpStatus.OK));
	}

	@Test
    public void convertEntityToDTOTest() throws Exception {
        BankSlipDTO bankSlipDTO = this.getDefaultBankSlipDTO();

        // Convert from DTO to domain
        BankSlip bankSlip = convertHelper.convert(bankSlipDTO, BankSlip.class);

        // Convert from domain to DTO
        BankSlipDTO bankSlipDTO2 = convertHelper.convert(bankSlip, BankSlipDTO.class);
        bankSlipDTO2.setCustomer("Customer DTO 2");

        assertTrue(bankSlipDTO2 != null && bankSlipDTO2.getCustomer().equals("Customer DTO 2"));
    }

    @Test
    public void bankSlipExpiredTest() throws IOException {
        BankSlipDTO bankSlipDTO = this.getDefaultBankSlipDTO();
        bankSlipDTO.setDueDate(new Date("2018/1/1"));

        ResponseEntity resCreate = this.createBankSlip(bankSlipDTO);
        if(resCreate.getStatusCode().equals(HttpStatus.CREATED) && !StringUtils.isEmpty(resCreate.getBody())) {
            BankSlipDTO bankSlip = objectMapper.readValue(resCreate.getBody().toString(), BankSlipDTO.class);
            ResponseEntity resGet = this.getBankSlipDetailsById(bankSlip.getId());
            if(resGet.getStatusCode().equals(HttpStatus.OK) && !StringUtils.isEmpty(resGet.getBody())) {
                BankSlipDTO bankSlipGet = objectMapper.readValue(resGet.getBody().toString(), BankSlipDTO.class);
                assertTrue(bankSlipGet.getFine() > 0);
            }
        }
    }
}
