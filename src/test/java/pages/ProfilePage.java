package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.demoqa.tests.TestData.login;

public class ProfilePage {

    @Step("Open user profile page")
    public ProfilePage openProfilePage() {
        open("/profile");
        return this;
    }

    @Step("Check User Name on profile page")
    public ProfilePage checkUserName(){
        $("#userName-value").shouldHave(text(login));
        return this;
    }

    @Step("Check the book in the collection")
    public ProfilePage checkBookInCollection(){
        $(".ReactTable").shouldHave(text("Speaking JavaScript"));
        return this;
    }

    @Step("Delete one book in the collection")
    public ProfilePage deleteOneBookInCollection(){
        $("#delete-record-undefined").click();
        $("#closeSmallModal-ok").click();
        return this;
    }

    @Step("Check the collection is empty")
    public ProfilePage checkCollectionIsEmpty(){
        $(".rt-noData").shouldHave(text("No rows found"));
        return this;
    }
}
