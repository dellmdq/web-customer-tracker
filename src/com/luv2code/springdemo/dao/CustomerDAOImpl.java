package com.luv2code.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.luv2code.springdemo.entity.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

	//need to inject the session factory
	@Autowired//from spring config. bean
	private SessionFactory sessionFactory;
	
	//@Transactional//spring manages beginning and ending sessions.//commented cause the service is gonna manage the transactions
	@Override
	public List<Customer> getCustomers() {
		
		//get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		//create a query ... sort by last name
		Query<Customer> theQuery = currentSession.createQuery("from Customer order by lastName", Customer.class);
	
		//execute query and get result list
		List<Customer> customers = theQuery.getResultList();
		
		//return the list of customers		
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		//get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		//save or update the customer
		currentSession.saveOrUpdate(theCustomer);	
	}

	@Override
	public Customer getCustomer(int theId) {
		//get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		//read obj from the db using the primary key and return it
		Customer theCustomer = currentSession.get(Customer.class, theId);
		return theCustomer;
	}

	@Override
	public void deleteCustomer(int theId) {
		//get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		//delete obj with the primary key 1) query 2) set parameter 3) execute
		Query theQuery = currentSession.createQuery("delete from Customer where id=:customerId");
		theQuery.setParameter("customerId", theId);
		theQuery.executeUpdate();
	}

	@Override
	public List<Customer> searchCustomers(String theSearchName) {
		//get the session from the session factory
		Session currentSession = sessionFactory.getCurrentSession();
		//create the query
		Query theQuery = null;
		
		if(theSearchName != null && theSearchName.trim().length() > 0) {
			theQuery = currentSession.createQuery("from Customer where lower(firstName) like :theName or lower(lastName) like :theName", Customer.class);// search for firstName or lastName ... case insensitive
			theQuery.setParameter("theName", "%"  + theSearchName.toLowerCase() + "%");	//set parameter
		}
		else {
			//the searchName is empty... so just get all customers
			theQuery = currentSession.createQuery("from Customer", Customer.class);		
		}
		//execute the query
		List<Customer> customers = theQuery.getResultList();
		//return		
		return customers;
	}

}
