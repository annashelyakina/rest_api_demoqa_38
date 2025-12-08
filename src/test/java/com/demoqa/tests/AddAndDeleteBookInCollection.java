package com.demoqa.tests;

import com.demoqa.models.BookDataModel;
import com.demoqa.models.CredentialsModel;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import pages.IconPage;
import pages.ProfilePage;

import java.util.Collections;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.demoqa.tests.TestData.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.Spec.*;

@Tag("api_demoqa_one_book_test")
public class AddAndDeleteBookInCollection extends TestBase {

    IconPage iconPage = new IconPage();
    ProfilePage profilePage = new ProfilePage();

    @Test
    @DisplayName("Добавление и удаление книги в коллекцию пользователя")
    void addAndDeleteBookToCollectionTest() {
        CredentialsModel authData = new CredentialsModel();
        authData.setUserName(login);
        authData.setPassword(password);

        Response authResponse = step("Login by user", () ->
                given(requestSpec)
                        .body(authData)
                        .when()
                        .post("/Account/v1/Login")
                        .then()
                        .spec(responseSpec200)
                        .extract().response());

        step("Delete all books from the collection", () ->
                given(requestSpec)
                        .header("Authorization", "Bearer " + authResponse.path("token"))
                        .queryParams("UserId", authResponse.path("userId"))
                        .when()
                        .delete("/BookStore/v1/Books")
                        .then()
                        .spec(responseSpec204));


        BookDataModel bookData = new BookDataModel();
        BookDataModel.CollectionOfIsbns isbnCollection = new BookDataModel.CollectionOfIsbns();
        bookData.setUserId(authResponse.path("userId"));
        isbnCollection.setIsbn(isbn);
        bookData.setCollectionOfIsbns(Collections.singletonList(isbnCollection));

        step("Add one book to the collection", () ->
                given(requestSpec)
                        .header("Authorization", "Bearer " + authResponse.path("token"))
                        .body(bookData)
                        .when()
                        .post("/BookStore/v1/Books")
                        .then()
                        .spec(responseSpec201));

        iconPage.openIconPage();

        step("Add Cookie", () -> {
            getWebDriver().manage().addCookie(new Cookie("userID", authResponse.path("userId")));
            getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
            getWebDriver().manage().addCookie(new Cookie("token", authResponse.path("token")));
        });

        profilePage.openProfilePage()
                .checkUserName()
                .checkBookInCollection()
                .deleteOneBookInCollection()
                .checkCollectionIsEmpty();

    }
}