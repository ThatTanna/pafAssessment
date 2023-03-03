package com.example.pafAssessment.repositories;

import java.math.BigDecimal;
// import java.sql.ResultSet;
// import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

// import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.pafAssessment.models.Account;

@Repository
public class AccountRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // public AccountRepository(DataSource dataSource) {
    //     this.jdbcTemplate = new JdbcTemplate(dataSource);
    // }

    public static final String findAllSQL = "SELECT * FROM accounts";
    public static final String findAccountByAccountIdSQL = "SELECT * FROM accounts WHERE account_id = ?";

    public List<Account> findAll() {
        return jdbcTemplate.query(findAllSQL, BeanPropertyRowMapper
                                .newInstance(Account.class));
    }

    public boolean accountExistByAccountId(String accountId) {
        Account result = jdbcTemplate.queryForObject(findAccountByAccountIdSQL,
                        BeanPropertyRowMapper.newInstance(Account.class), accountId);

        if (result == null){
            System.out.println("Account cannot be found!");
            return false;
        } else {
            System.out.println(result.getName() + " name is found");
            return true;
        }
    }

    public Account findAccountByAccountId(String accountId) {
        Account result = jdbcTemplate.queryForObject(findAccountByAccountIdSQL,
                        BeanPropertyRowMapper.newInstance(Account.class), accountId);
        return result;
    }

    public BigDecimal findBalanceByAccountId(String fromAccount) {
        Account result = jdbcTemplate.queryForObject(findAccountByAccountIdSQL,
                        BeanPropertyRowMapper.newInstance(Account.class), fromAccount);
        return result.getBalance();
    }


    // private static class AccountRowMapper implements RowMapper<Account> {
    //     @Override
    //     public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
    //         Account account = new Account();
    //         account.setAccountId(rs.getString("account_id"));
    //         account.setName(rs.getString("name"));
    //         account.setBalance(rs.getBigDecimal("balance"));
    //         return account;
    //     }
    // }
}
