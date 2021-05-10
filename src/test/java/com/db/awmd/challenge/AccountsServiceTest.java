package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.AmountTransfer;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.service.AccountsService;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;

  
  @Test
  public void addAccount() throws Exception {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  public void addAccount_failsOnDuplicateId() throws Exception {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }

  }
  
  @Test
  public void transferAmount() throws Exception {
	  AmountTransfer at = new AmountTransfer();
	  at.setTransferAmount(new BigDecimal(500));
	  at.setFromAccountId("Id-123");
	  at.setToAccountId("Id-345");
	  createAccount();
      this.accountsService.transferAmount(at);
      assertThat(this.accountsService.getAccount("Id-123").getBalance()).isEqualTo(new BigDecimal(500));
      assertThat(this.accountsService.getAccount("Id-345").getBalance()).isEqualTo(new BigDecimal(1500));
  }
  
  @Test
  public void transferAmount_BalanceLow() throws Exception {
	  AmountTransfer at = new AmountTransfer();
	  at.setTransferAmount(new BigDecimal(1500));
	  at.setFromAccountId("Id-123");
	  at.setToAccountId("Id-345");
	  createAccount();
	  try {
		  this.accountsService.transferAmount(at);
	      fail("Should have failed when balance is low");
	    } catch (DuplicateAccountIdException ex) {
	      assertThat(ex.getMessage()).isEqualTo("Your Account balance is insufficient");
	    }
    
    
  }
  @Test
  public void transferAmount_AccountIdsNot valid() throws Exception {
	  AmountTransfer at = new AmountTransfer();
	  at.setTransferAmount(new BigDecimal(1500));
	  at.setFromAccountId("Id-000");
	  at.setToAccountId("Id-000");
	  createAccount();
	  try {
		  this.accountsService.transferAmount(at);
	      fail("Should have failed when provided account numbers are not valid");
	    } catch (DuplicateAccountIdException ex) {
	      assertThat(ex.getMessage()).isEqualTo("Please check Provided account id.");
	    }
    
    
  }
  private void createAccount() {
	  Account account = new Account("Id-123");
	    account.setBalance(new BigDecimal(1000));
	    this.accountsService.createAccount(account);
	    Account account1 = new Account("Id-345");
	    account.setBalance(new BigDecimal(1000));
	    this.accountsService.createAccount(account1);
  }
}
