package com.app.TwoPhaseCommit.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.app.TwoPhaseCommit.logic.accounts.AccountEntity;
import com.app.TwoPhaseCommit.logic.accounts.AccountsService;
import com.app.TwoPhaseCommit.logic.accounts.exceptions.AccountNotFoundException;
import com.app.TwoPhaseCommit.logic.accounts.jpa.JpaAccountsService;
import com.app.TwoPhaseCommit.logic.transactions.TransactionService;
import com.app.TwoPhaseCommit.logic.transactions.TransactionState;
import com.app.TwoPhaseCommit.logic.transactions.jpa.JpaTransactionService;

@Controller
public class ClientController {
	
	@Autowired
	private JpaAccountsService accountservice;
	
	@Autowired
	private JpaTransactionService transactionservice;
	
	@RequestMapping(path="/login",method=RequestMethod.GET)
	public ModelAndView login()
	{
		ModelAndView model = new ModelAndView();
		model.setViewName("login");
		model.addObject("accountTo", new AccountTO());
		
		return model;
	}
	
	@RequestMapping(path="/login",method=RequestMethod.POST)
	public ModelAndView loginPost(@ModelAttribute("accountTo") AccountTO accountTO) throws AccountNotFoundException
	{
		
		ModelAndView model = new ModelAndView();
		if(accountservice.isAccountExists(accountTO.getUsername()))
		{
			model.setViewName("useraccount");
			
			model.addObject("trans", transactionservice.GetAllTrans());
			
			model.addObject("userEntity",accountservice.getAccountById(accountTO.getUsername()));
		}
		else
		{
			model.setViewName("login");
			model.addObject("accountTo", new AccountTO());		

		}
		
		return model;
		
	}
	
	@RequestMapping(path="/register",method=RequestMethod.GET)
	public ModelAndView register()
	{
		ModelAndView model = new ModelAndView();
		model.setViewName("register");
		model.addObject("AccountTo", new AccountTO());
		
		return model;
	}
	
	@RequestMapping(path="/register",method=RequestMethod.POST)
	public ModelAndView registerPost(@ModelAttribute("AccountTo") AccountTO accountTo)
	{
		ModelAndView model = new ModelAndView();
		
		try {
			accountservice.createNewAccount(accountTo.toEntity());
			model.setViewName("useraccount");
			model.addObject("trans", transactionservice.GetAllTrans());

			model.addObject("userEntity", accountTo);

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			model.setViewName("register");
			model.addObject("accountTo", new AccountTO());
		}
		
		
		return model;
	}
	
	
	
	@RequestMapping(path="/transaction",method=RequestMethod.POST)
	public ModelAndView MoveToTrans(@ModelAttribute("userEntity") AccountTO accountTO)
	{
		
		ModelAndView model = new ModelAndView();
		model.setViewName("trans"); 
		model.addObject("TransactionTO",new TransactionTO());
		model.addObject("username", accountTO.getUsername());
		model.addObject("community", accountservice.getCommunity(accountTO.getUsername()));
		return model;
		
	}
	
	
	@RequestMapping(path="/DoTrans",method=RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView Tranfer(@RequestBody TransactionTO transfer) throws Exception
	{
		
	
		ModelAndView model = new ModelAndView();

		transactionservice.createNewTransaction
		(transfer.getSource(), transfer.getDestination(), transfer.getValue(), TransactionState.INITIAL);
		
		System.out.println("here");
		
		model.setViewName("useraccount");
		model.addObject("userEntity", accountservice.getAccountById(transfer.getSource()));

		
		return model;
		
	}
	

}
