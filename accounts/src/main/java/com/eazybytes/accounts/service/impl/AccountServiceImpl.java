package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.constants.AccountConstants;
import com.eazybytes.accounts.dto.AccountDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entyty.Account;
import com.eazybytes.accounts.entyty.Customer;
import com.eazybytes.accounts.exception.CustomerAlreadyExistException;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;

    /**
     *
     * @param customerDto - CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {

        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistException("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomer = customerRepository.save(customer);
        accountRepository.save(createNewAccount(savedCustomer));

    }

    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private Account createNewAccount(Customer customer) {

        Long randomAccNumber = 100000000000L + new Random().nextInt(900000000);

        Account account = Account.builder()
                .customerId(customer.getCustomerId())
                .accountNumber(randomAccNumber)
                .accountType(AccountConstants.SAVINGS)
                .branchAddress(AccountConstants.ADDRESS)
                .build();
        account.setCreatedAt(LocalDateTime.now());
        account.setCreatedBy("Anonymous");
        return account;
    }

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Account Details based on a given mobileNumber
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
                );

        Account account = accountRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "CustomerId", customer.getCustomerId().toString())
                );

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountDto(AccountMapper.mapToAccountsDto(account, new AccountDto()));

        return customerDto;
    }

    /**
     *
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful ot not
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {

        boolean isUpdated = false;

        AccountDto accountDto = customerDto.getAccountDto();
        if (accountDto != null) {
            Account account = accountRepository.findById(accountDto.getAccountNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("Account", "AccountNumber", accountDto.getAccountNumber().toString()));

            AccountMapper.mapToAccounts(accountDto, account);
            account = accountRepository.save(account);

            Long customerId = account.getCustomerId();
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString()));
            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);

            isUpdated = true;

        }
        return isUpdated;
    }

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account is successful or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        accountRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());

        return true;
    }

}
