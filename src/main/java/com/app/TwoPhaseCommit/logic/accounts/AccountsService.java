package com.app.TwoPhaseCommit.logic.accounts;

import java.util.List;

import com.app.TwoPhaseCommit.api.TransactionTO;
import com.app.TwoPhaseCommit.logic.accounts.exceptions.AccountNotFoundException;
import com.app.TwoPhaseCommit.logic.transactions.TransactionEntity;


public interface AccountsService {
	
	public void cleanup();
	
	public List<AccountEntity> getAllAccounts();
	
	public List<AccountEntity> getCommunity(String username);
		
	public AccountEntity createNewAccount(AccountEntity accountEntity) throws Exception;
	
	public AccountEntity getAccountById(String username) throws AccountNotFoundException;
	
	public boolean isAccountExists(String username);
	
	public Object updateBalanceAndPushToPendingTransactions(String username, double amount, String transactionId) throws Exception;

    public Object updateBalanceAndPullFromPendingTransactions(String username, double amount, String transactionId) throws Exception;
    
    public Object updatePullFromPendingTransactions(String username, String transactionId) throws Exception;

	public void AddNewTransaction(String source, String destination, TransactionTO transactionTO);

}
