package com.petya.teeth.app.models;

import com.petya.teeth.app.enums.ToothJawPosition;

import java.io.Serializable;

public class ToothPositionModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private ToothJawPosition jaw;
    private int number;

    public ToothPositionModel(ToothJawPosition jaw, int number) {
        this.jaw = jaw;
        this.number = number;
    }

    public ToothJawPosition getJaw() {
        return jaw;
    }

    public int getNumber() {
        return number;
    }
}
