package com.eazybytes.accounts.service;


import com.eazybytes.accounts.dto.CustomerDto;

public interface AccountService {

    /**
     *
     * @param customerDto - CustomerDto Object
     */
    void createAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Account Details based on a given mobileNumber
     */
    CustomerDto fetchAccount(String mobileNumber);


    /**
     *
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful ot not
     */
    boolean updateAccount(CustomerDto customerDto);

}
