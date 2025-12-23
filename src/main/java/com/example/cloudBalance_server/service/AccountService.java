package com.example.cloudBalance_server.service;

import com.example.cloudBalance_server.dto.AccountRequestDTO;
import com.example.cloudBalance_server.dto.AccountResponseDTO;
import com.example.cloudBalance_server.entity.Account;
import com.example.cloudBalance_server.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public AccountResponseDTO addAccount(AccountRequestDTO accountDTO){
        Account newAcc = new Account();
        newAcc.setAccountId(accountDTO.getAccountId());
        newAcc.setAccountName(accountDTO.getAccountName());
        newAcc.setAccountArn(accountDTO.getAccountArn());

        accountRepository.save(newAcc);

        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setAccountArn(accountDTO.getAccountArn());
        accountResponseDTO.setAccountStatus(newAcc.getAccountStatus());
        accountResponseDTO.setAccountName(newAcc.getAccountName());
        accountResponseDTO.setAccountId(newAcc.getAccountId());

        return accountResponseDTO;
    }
}
