package com.example.pafAssessment.repositories;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.pafAssessment.models.Funds;

// import redis.clients.jedis.Transaction;

@Repository
public class FundsRepositories {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String updateFromAccountById = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
    private static final String updateToAccountById = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";

    public Funds transferFunds(Funds funds) {
        BigDecimal amount = funds.getAmount();
        String fromAccountId = funds.getFromAccount();
        String toAccountId = funds.getToAccount();
        jdbcTemplate.update(updateFromAccountById, amount, fromAccountId);
        jdbcTemplate.update(updateToAccountById, amount, toAccountId);

        return funds;
    }
}
