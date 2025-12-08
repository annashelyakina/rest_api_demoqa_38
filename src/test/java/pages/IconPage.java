package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.open;

public class IconPage {

    @Step("Open page with icon")
    public IconPage openIconPage(){
        open("/favicon.ico");
        return this;
    }
}
