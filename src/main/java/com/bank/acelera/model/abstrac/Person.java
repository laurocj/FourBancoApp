/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.acelera.model.abstrac;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;

/**
 *
 * @author lauro
 */
@Entity
@Inheritance
public abstract class Person {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    protected String name;
    
    @OneToMany(mappedBy = "person")
    protected List<Account> accounts;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public List<Account> getAccounts() {
        return accounts;
    }
}
