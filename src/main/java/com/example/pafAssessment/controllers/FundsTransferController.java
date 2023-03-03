package com.example.pafAssessment.controllers;

import java.math.BigDecimal;
// import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.pafAssessment.models.Account;
import com.example.pafAssessment.models.Funds;
import com.example.pafAssessment.repositories.AccountRepository;
import com.example.pafAssessment.repositories.FundsRepositories;
// import com.fasterxml.jackson.databind.annotation.JsonAppend.Attr;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
// import redis.clients.jedis.Transaction;

@Controller
@RequestMapping({"/", "index"})
public class FundsTransferController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    FundsRepositories fundsRepositories;
    
    @GetMapping
    public String getAccount(Model model, HttpSession session){

        List<Account> accounts = accountRepository.findAll();

        model.addAttribute("accounts", accounts);
        model.addAttribute("funds", new Funds()); 
        return "index";
    }

    @PostMapping(path = "/transfer", consumes = "application/x-www-form-urlencoded")
    public String postForm(@Valid Funds funds, BindingResult bindingResult,
    Model model, HttpSession session) {

        String fromAccount = funds.getFromAccount();
        String toAccount = funds.getToAccount();
        BigDecimal transferAmount = funds.getAmount();
        

        if (bindingResult.hasErrors()){
            List<Account> listOfAccounts = accountRepository.findAll();
            
            model.addAttribute("listOfAccounts", listOfAccounts);
            System.out.println("both accounts not the same, but binding errors");
            return "index"; 
        }

        if (toAccount.equals(fromAccount)) {
            List<Account> listOfAccounts = accountRepository.findAll();
            model.addAttribute("listOfAccounts", listOfAccounts);
            System.out.println("accounts same error");

            String error = "Both accounts are the same. Transaction failed.";
            model.addAttribute("error", error);
            return "index";
        } 


        if (!accountRepository.accountExistByAccountId(fromAccount) || !accountRepository.accountExistByAccountId(toAccount)){
            List<Account> listOfAccounts = accountRepository.findAll();
            model.addAttribute("listOfAccounts", listOfAccounts);
            System.out.println("Account does not exist!");

            String error = "Account(s) does not exist!";
            model.addAttribute("error", error);
            return "index";

        }

        BigDecimal fromBalance = accountRepository.findBalanceByAccountId(fromAccount);

        if (transferAmount.compareTo(fromBalance) > 0) {
            List<Account> listOfAccounts = accountRepository.findAll();
            model.addAttribute("listOfAccounts", listOfAccounts);
            System.out.println("Insufficient balance!");

            String error = "Insufficient funds!";
            model.addAttribute("error", error);
            return "index";
        }

        Funds validTransaction = fundsRepositories.transferFunds(funds);
    
        System.out.println("Result transaction: " + validTransaction);
        model.addAttribute("validTransaction", validTransaction);
        session.setAttribute("validTransaction", validTransaction);
        return "transfer";

    }

    @GetMapping("/transfer")
    public String formSubmitted(Model model, HttpSession session) {
        Funds funds = (Funds) session.getAttribute("validTransaction");
        model.addAttribute("validTransaction", funds);
        return "transfer";
    }
}
