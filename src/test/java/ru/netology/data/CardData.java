package ru.netology.data;

public class CardData {
    public final String number;
    public final String month;
    public final String year;
    public final String holder;
    public final String cvc;

    public CardData(String number, String month, String year, String holder, String cvc) {
        this.number = number;
        this.month = month;
        this.year = year;
        this.holder = holder;
        this.cvc = cvc;
    }
}
