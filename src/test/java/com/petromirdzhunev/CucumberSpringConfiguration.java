package com.petromirdzhunev;

import org.springframework.boot.test.context.SpringBootTest;

import com.petromirdzhunev.cucumber.spring.beans.MockHttpServer;
import com.petromirdzhunev.cucumber.spring.beans.PostgreSQLDatabaseTruncator;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.RequiredArgsConstructor;

/**
 * We have this as classes annotated with @RunWith(Cucumber.class) must not define any
 * Step Definition or Hook methods. Step Definitions and Hooks should be defined
 * in their own classes.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
                classes = TestApplication.class)
@CucumberContextConfiguration
@RequiredArgsConstructor
public class CucumberSpringConfiguration {

	private final PostgreSQLDatabaseTruncator postgreSQLDatabaseTruncator;
	private final MockHTTPServerWrapper mockHttpServerWrapper;

	@Before
	public void beforeScenario() {
		postgreSQLDatabaseTruncator.truncate();
		mockHttpServerWrapper.start();
		mockHttpServerWrapper.reset();
	}
}
