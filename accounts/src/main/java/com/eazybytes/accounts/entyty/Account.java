package com.eazybytes.accounts.entyty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account extends BaseEntity{


    private Long customerId;

    @Id
    private Long accountNumber;

    private String accountType;

    private String branchAddress;

}
