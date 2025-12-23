package com.example.cloudBalance_server.repository;

import com.example.cloudBalance_server.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {

}
