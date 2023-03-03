package com.example.pafAssessment.models;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Funds {
    
    private String transactionId;
    private Date date;

    @Size(min = 10, max = 10, message = "Account number must be 10 characters.")
    private String fromAccount;
    private String fromAccountName;

    @Size(min = 10, max = 10, message = "Account number must be 10 characters.")
    private String toAccount;
    private String toAccountName;

    @Min(value = 0, message = "Amount cannot be negative.")
    @Min(value = 10, message = "Min of $10 to transfer.")
    private BigDecimal amount;

    private String comments;

    public JsonObject toJsonObject() {
        return Json.createObjectBuilder()
            .add("transactionId", this.getTransactionId())
            .add("date", this.getDate().toString())
            .add("from_account", this.getFromAccount())
            .add("to_account", this.getToAccount())
            .add("amount", this.getAmount())
            .build();
    }


}
