package com.bankslips.repo;

import com.bankslips.domain.BankSlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface BankSlipRepo extends JpaRepository<BankSlip, UUID>{
}