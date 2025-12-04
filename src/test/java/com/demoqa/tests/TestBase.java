package com.demoqa.tests;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    @BeforeAll
    static void setup(){
        Configuration.baseUrl = "https://demoqa.com";
        RestAssured.baseURI="https://demoqa.com";
        Configuration.browserSize = "1920x1080";
        Configuration.browser = "chrome";
        Configuration.browserVersion =("128.0");
        Configuration.timeout = 10000;
        Configuration.remote = ("https://user1:1234@selenoid.autotests.cloud/wd/hub");
    }

    @AfterEach
    void shutDown(){
        closeWebDriver();
    }
}
