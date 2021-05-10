package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.AmountTransfer;
import com.db.awmd.challenge.exception.AccountNotPresent;
import com.db.awmd.challenge.exception.LowBalanceException;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountsService {

	// Not autowiring because EmailNotificationService not annoted with @Component
	EmailNotificationService notificationService = new EmailNotificationService();
  @Getter
  private final AccountsRepository accountsRepository;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }
  
  /**
   * Trasnfering amount  bank account to other account
   * @param transfer
   * @return
   */
  public Account transferAmount(AmountTransfer transfer) {
	  Account toAccount = this.accountsRepository.getAccount(transfer.getToAccountId());
	  Account fromAccount = this.accountsRepository.getAccount(transfer.getFromAccountId());
	  if(toAccount == null || fromAccount == null) {
		  log
	      .info("toAccount or fromAccount is not present", account.getAccountId(), transferDescription);
		  throw new AccountNotPresent("Please check Provided account id.");
	  }
	  ReentrantLock lock = new ReentrantLock();
	  lock.lock();
	  if(fromAccount.getBalance().compareTo(transfer.getTransferAmount()) == 1) {
		  
		  fromAccount.setBalance(fromAccount.getBalance().subtract(transfer.getTransferAmount()));
		  // Updating the balance 
		  this.accountsRepository.updateBalance(fromAccount);
		  toAccount.setBalance(toAccount.getBalance().add(transfer.getTransferAmount()));
		// Updating the balance 
		  this.accountsRepository.updateBalance(toAccount);
		  notificationService.notifyAboutTransfer(fromAccount, "You transfered "+transfer.getTransferAmount()
		  + " successfully to "+  toAccount.getAccountId()+ " from " + fromAccount.getAccountId() );
		  log
	      .info("Amount is trasnfered successfully from {{}} to {{}}" ,fromAccount.getAccountId() ,  toAccount.getAccountId());
	  } else {
		  throw new LowBalanceException("Your Account balance is insufficient");
	  }
	  lock.unlock();
  }
}
