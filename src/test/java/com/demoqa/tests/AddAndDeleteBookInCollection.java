package com.demoqa.tests;

import com.demoqa.models.CredentialsModel;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.demoqa.tests.TestData.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static specs.Spec.*;

@Tag("api_demoqa_one_book_test")
public class AddAndDeleteBookInCollection extends TestBase {

    @Test
    @DisplayName("Добавление и удаление книги в коллекции пользователя")
    void addAndDeleteBookToCollectionTest() {
        CredentialsModel authData = new CredentialsModel();
         authData.setUserName(login);
         authData.setPassword(password);

        Response authResponse = step("Login by user", ()->
                given(requestSpec)
                .body(authData)
                .when()
                .post("/Account/v1/Login")
                .then()
                .spec(responseSpec200)
                .extract().response());

        step("Delete all books from the collection", ()->
            given(requestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .queryParams("UserId", authResponse.path("userId"))
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .spec(responseSpec204));


        String bookData = format("{\"userId\":\"%s\",\"collectionOfIsbns\":[{\"isbn\":\"%s\"}]}",
                authResponse.path("userId") , isbn);

        step("Add one book to the collection", ()->
            given(requestSpec)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .body(bookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .spec(responseSpec201));

        step("Open page with icon", ()->
            open("/favicon.ico"));

        step("Add Cookie", ()-> {
                    getWebDriver().manage().addCookie(new Cookie("userID", authResponse.path("userId")));
                    getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
                    getWebDriver().manage().addCookie(new Cookie("token", authResponse.path("token")));
        });

        step("Open user profile page", ()->
            open("/profile"));

        step("Check User Name: " + login + " on profile page", ()->
            $("#userName-value").shouldHave(text(login)));

        step("Check the book in the collection", ()->
            $(".ReactTable").shouldHave(text("Speaking JavaScript")));

        step("Delete one book in the collection", ()-> {
            $("#delete-record-undefined").click();
            $("#closeSmallModal-ok").click();
        });
        step("Check the collection is empty", ()->
                $(".rt-noData").shouldHave(text("No rows found")));
    }
  }