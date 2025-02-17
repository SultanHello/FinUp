package org.example.transactionservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    int amount;
    String description;
    String date;
}
