package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.netology.data.CardData;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TourPage {
    private final SelenideElement payButton = $$('button').findBy(text("Купить"));
    private final SelenideElement creditButton = $$('button').findBy(text("Купить в кредит"));
    private final SelenideElement cardNumber = $("input[placeholder='0000 0000 0000 0000']");
    private final SelenideElement month = $("input[placeholder='08']");
    private final SelenideElement year = $("input[placeholder='22']");
    private final SelenideElement holder = $x("//span[text()='Владелец']/following::input[1]");
    private final SelenideElement cvc = $("input[placeholder='999']");
    private final SelenideElement success = $(".notification_status_ok");
    private final SelenideElement error = $(".notification_status_error");

    @Step("Открыть главную страницу")
    public static TourPage openPage() {
        open(System.getProperty("sut.url", "http://localhost:8080"));
        return new TourPage();
    }

    @Step("Открыть форму оплаты картой")
    public TourPage openPaymentForm() {
        payButton.click();
        return this;
    }

    @Step("Открыть форму покупки в кредит")
    public TourPage openCreditForm() {
        creditButton.click();
        return this;
    }

    @Step("Заполнить данные карты")
    public TourPage fillCard(CardData card) {
        cardNumber.setValue(card.number);
        month.setValue(card.month);
        year.setValue(card.year);
        holder.setValue(card.holder);
        cvc.setValue(card.cvc);
        return this;
    }

    @Step("Отправить форму")
    public void submit() {
        $$('button').findBy(text("Продолжить")).click();
    }

    @Step("Проверить успешное уведомление")
    public void shouldShowSuccess() {
        success.shouldBe(visible, Duration.ofSeconds(15));
    }

    @Step("Проверить уведомление об ошибке")
    public void shouldShowError() {
        error.shouldBe(visible, Duration.ofSeconds(15));
    }

    @Step("Проверить ошибку валидации")
    public void shouldShowValidationError(String text) {
        $$(".input__sub").findBy(exactText(text)).shouldBe(visible);
    }
}
