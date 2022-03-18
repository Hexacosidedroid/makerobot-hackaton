package ru.cib.makerobot;

public enum Opertype {
    BUY("Покупка"),
    SELL("Продажа"),
    PRC("Проценты"),
    RED("Выкуп облигации");

    private final String description;

    Opertype(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
