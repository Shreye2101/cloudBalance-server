package com.example.cloudBalance_server.controller;

import com.example.cloudBalance_server.dto.AccountRequestDTO;
import com.example.cloudBalance_server.dto.AccountResponseDTO;
import com.example.cloudBalance_server.entity.Account;
import com.example.cloudBalance_server.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/addAccount")
    public AccountResponseDTO addAccount(@RequestBody AccountRequestDTO accountRequestDTO){
        return accountService.addAccount(accountRequestDTO);
    }

    @GetMapping("/getAllAccounts")
    public List<Account> getAllAccounts(){
        return accountService.getAllAccounts();
    }

}
