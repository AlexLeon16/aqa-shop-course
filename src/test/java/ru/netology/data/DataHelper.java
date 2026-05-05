package ru.netology.data;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.util.Locale;

public class DataHelper {
    private static final Faker faker = new Faker(new Locale("en"));

    public static CardData approvedCard() {
        return validCard("1111 2222 3333 4444");
    }

    public static CardData declinedCard() {
        return validCard("5555 6666 7777 8888");
    }

    public static CardData unknownCard() {
        return validCard("4444 4444 4444 4444");
    }

    public static CardData validCard(String number) {
        LocalDate date = LocalDate.now().plusYears(1);
        return new CardData(
                number,
                String.format("%02d", date.getMonthValue()),
                String.valueOf(date.getYear()).substring(2),
                faker.name().fullName().toUpperCase(),
                faker.number().digits(3)
        );
    }

    public static CardData expiredYearCard() {
        LocalDate date = LocalDate.now().minusYears(1);
        CardData valid = approvedCard();
        return new CardData(valid.number, valid.month, String.valueOf(date.getYear()).substring(2), valid.holder, valid.cvc);
    }

    public static CardData invalidMonthCard() {
        CardData valid = approvedCard();
        return new CardData(valid.number, "13", valid.year, valid.holder, valid.cvc);
    }

    public static CardData emptyHolderCard() {
        CardData valid = approvedCard();
        return new CardData(valid.number, valid.month, valid.year, "", valid.cvc);
    }
}
