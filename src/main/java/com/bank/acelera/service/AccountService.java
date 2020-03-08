/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.acelera.service;

import com.bank.acelera.controller.converter.MovementConverter;
import com.bank.acelera.controller.request.MovementRequest;
import com.bank.acelera.controller.response.MovementResponse;
import com.bank.acelera.model.Account;
import com.bank.acelera.model.Movement;
import com.bank.acelera.repository.AccountRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author lauro
 */
@Service
public class AccountService {
    
    @Autowired
    MovementService movementService;
    
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    MovementConverter movementConverter;
    
    /**
     * find by number
     * 
     * @param number
     * @return 
     */
    public Account findByNumber(long number) throws IllegalArgumentException {
        
        Optional<Account> found = accountRepository.findByNumber(number);
        
        if(found.isEmpty()) {
            throw new IllegalArgumentException("Account not found");
        }
        
        return found.get();
    }
    
    /**
     * Save
     * @param account
     * @return 
     */
    public Account save(Account account) {
        return accountRepository.save(account);
    }
    
    /**
     * Add movement
     * 
     * @param movement
     * @return
     */
    public boolean addMovement(long number, Movement movement) throws IllegalArgumentException {
        Account account = this.findByNumber(number);
        
        if(this.allowedMovement(account, movement)) {
            
            movement.setDate(new Date());
            movement.setAccount(account);
            
            try {
                movement = movementService.save(movement);
                this.calculateTheBalance(account, movement);
                this.save(account);
                return true;
            } catch (Exception e) {
                return false;
            }            
        }
        return false;
    }

    /**
     *
     * @param movementRequest
     * @return
     */
    public boolean addMovement(MovementRequest movementRequest) {
        Movement movement = movementConverter.movementConverter(movementRequest);
        return this.addMovement(movementRequest.getAccountNumber(), movement);
    }

    /**
     * Allowed movement
     * 
     * @param Moviment
     * @return 
     */
    public boolean allowedMovement(Account account, Movement movement){
        if(account.isOpened()){
            switch(movement.getType()){
                case CREDIT:
                    return true;
                case DEBIT:
                    return account.getBalance() > movement.getValue();
            }
        }
        return false;
    }
    
    /**
     * Calculate the balance
     * 
     * @param movement 
     */
    public void calculateTheBalance(Account account, Movement movement){
        if(account.isOpened()){
            switch(movement.getType()){
                case CREDIT:
                    account.increment(movement.getValue());
                   break;
                case DEBIT:
                    account.reduce(movement.getValue());
                    break;
            }
        } else {
            throw new IllegalArgumentException("Closed account");
        }
    }

    /**
     * Returns the account movements
     *
     * @param accountNumber
     * @return
     */
    public List<MovementResponse> getMovements(Long accountNumber) throws IllegalArgumentException {
        Account account = this.findByNumber(accountNumber);
        return movementConverter.converterList(account.getMovements());
    }
}
