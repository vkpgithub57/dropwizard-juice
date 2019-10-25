package com.poc.app;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.poc.dao.CustomerDAO;
import com.poc.dao.CustomerDAOImpl;

public class TestAppModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CustomerDAO.class).to(CustomerDAOImpl.class).in(Singleton.class);
	}
}