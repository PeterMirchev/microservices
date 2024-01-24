package com.eazybytes.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private String name;
    private String email;
    private String mobileNumber;
    private AccountDto accountDto;

}
