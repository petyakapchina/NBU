package com.petya.teeth.app.enums;

public enum ToothJawPosition {
    UPPER(1),
    LOWER(2);

    private final int id;

    ToothJawPosition(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ToothJawPosition getById(int id) {
        for (ToothJawPosition position : values()) {
            if (position.id == id) {
                return position;
            }
        }
        throw new IllegalArgumentException("No enum constant with ID: " + id);
    }
}
