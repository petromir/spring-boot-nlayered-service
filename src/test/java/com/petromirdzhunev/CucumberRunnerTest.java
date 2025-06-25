package com.petromirdzhunev;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
// NOTE: Be careful when adding your root packages inside `glue` attribute, as Cucumber might attempt
//  to load more than necessary an mess with Spring beans loading.
//  An additional package is required as CucumberSpringConfiguration must be loaded.
@CucumberOptions(features = "classpath:/features",
                 glue = {"com.petromirdzhunev"}, // This covers the andromeda-spring-boot-starter
                 plugin = {"pretty", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"})
public class CucumberRunnerTest {}
