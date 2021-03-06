/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.acelera.repository;

import com.bank.acelera.repository.person.PersonRepository;
import com.bank.acelera.repository.account.AccountRepository;
import com.bank.acelera.model.CheckingAccount;
import com.bank.acelera.model.abstrac.Account;
import com.bank.acelera.model.Physical;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author lauro
 */
@SpringBootTest
public class AccountRepositoryTests {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Physical physical;

    @BeforeEach
    public void setUp() {

        if (personRepository.findByName("João alfredo") == null) {
            this.physical = new Physical("João alfredo", "123.123.123-53");
            this.physical = personRepository.save(this.physical);
        } else {
            this.physical = (Physical) personRepository.findByName("João alfredo");
        }
    }

    @Test
    public void whenFindById_thenOpenDateNotNull() {
        // given
        Account account = new CheckingAccount();
        account.open(11111112L, "PasSwOrd", this.physical);
        accountRepository.save(account);
        
        // when
        Account found = accountRepository.findById(account.getId()).get();

        // then
        Assertions.assertThat(found.getOpenDate())
                .isNotNull();
    }

}
