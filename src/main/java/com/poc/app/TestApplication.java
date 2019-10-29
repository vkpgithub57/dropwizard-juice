package com.poc.app;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.poc.resource.CustomerResource;
import com.poc.util.MongoDBConfig;
import com.poc.util.MongoDBConnection;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TestApplication extends Application<MongoDBConfig> {

    private static final Logger logger = LoggerFactory.getLogger(TestApplication.class);
    public static void main(String[] args) throws Exception {
    	new TestApplication().run("server", args[0]);
    }

    @Override
    public void initialize(Bootstrap<MongoDBConfig> b) {
    }

    @Override
    public void run(MongoDBConfig config, Environment env)
            throws Exception {
    	
    	// Enable CORS headers
        final FilterRegistration.Dynamic cors =
        		env.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    	
		logger.info("run()-->Registering RESTful API resources");
		final MongoDBConnection mongoDBManagerConn = new MongoDBConnection(config,env);
		final Injector injector = Guice.createInjector(new TestAppModule());
		env.jersey().register(injector.getInstance(CustomerResource.class));
		env.healthChecks().register("DropwizardMongoDBHealthCheck",
		new com.poc.resource.TestAppMongoDBHealthCheckResource(mongoDBManagerConn.getMongoClient()));
    }
}
