package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.helpers.DbHelper;
import ru.netology.pages.TourPage;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentFormTest {

    @BeforeAll
    static void setUpAll() {
        Configuration.browserSize = "1366x768";
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
    }

    @BeforeEach
    void setUp() {
        DbHelper.cleanDatabase();
    }

    @Test
    @DisplayName("Оплата по APPROVED карте успешна, в БД сохраняется APPROVED")
    void shouldApprovePaymentByApprovedCard() {
        TourPage.openPage().openPaymentForm().fillCard(DataHelper.approvedCard()).submit();
        new TourPage().shouldShowSuccess();
        assertThat(DbHelper.getLatestPaymentStatus()).isEqualTo("APPROVED");
        assertThat(DbHelper.getPaymentCount()).isEqualTo(1);
        assertThat(DbHelper.getOrderCount()).isEqualTo(1);
        DbHelper.assertNoCardDataSaved();
    }

    @Test
    @DisplayName("Оплата по DECLINED карте отклоняется, в БД сохраняется DECLINED")
    void shouldDeclinePaymentByDeclinedCard() {
        TourPage.openPage().openPaymentForm().fillCard(DataHelper.declinedCard()).submit();
        new TourPage().shouldShowError();
        assertThat(DbHelper.getLatestPaymentStatus()).isEqualTo("DECLINED");
        DbHelper.assertNoCardDataSaved();
    }

    @Test
    @DisplayName("Неизвестный номер карты не должен приводить к успешной оплате")
    void shouldNotApproveUnknownCard() {
        TourPage.openPage().openPaymentForm().fillCard(DataHelper.unknownCard()).submit();
        new TourPage().shouldShowError();
        assertThat(DbHelper.getLatestPaymentStatus()).isNotEqualTo("APPROVED");
    }

    @Test
    @DisplayName("Нельзя оплатить картой с истёкшим годом")
    void shouldValidateExpiredYear() {
        TourPage.openPage().openPaymentForm().fillCard(DataHelper.expiredYearCard()).submit();
        new TourPage().shouldShowValidationError("Истёк срок действия карты");
        assertThat(DbHelper.getPaymentCount()).isZero();
    }

    @Test
    @DisplayName("Нельзя оплатить картой с месяцем 13")
    void shouldValidateInvalidMonth() {
        TourPage.openPage().openPaymentForm().fillCard(DataHelper.invalidMonthCard()).submit();
        new TourPage().shouldShowValidationError("Неверно указан срок действия карты");
        assertThat(DbHelper.getPaymentCount()).isZero();
    }

    @Test
    @DisplayName("Поле Владелец обязательно")
    void shouldValidateEmptyHolder() {
        TourPage.openPage().openPaymentForm().fillCard(DataHelper.emptyHolderCard()).submit();
        new TourPage().shouldShowValidationError("Поле обязательно для заполнения");
        assertThat(DbHelper.getPaymentCount()).isZero();
    }
}
