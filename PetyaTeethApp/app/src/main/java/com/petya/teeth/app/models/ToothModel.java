package com.petya.teeth.app.models;

import com.petya.teeth.app.R;
import com.petya.teeth.app.enums.ToothJawPosition;
import com.petya.teeth.app.enums.ToothState;

import java.io.Serializable;

public class ToothModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int uid;
    private final ToothPositionModel toothPosition;
    private ToothState toothState;
    private int toothImage;

    public ToothModel(int uid, ToothPositionModel position, ToothState state) {
        this.uid = uid;
        this.toothPosition = position;
        this.toothState = state;
        if(this.toothPosition.getJaw() == ToothJawPosition.UPPER) {
            this.toothImage = R.drawable.tooth_upper;
        }
        else {
            this.toothImage = R.drawable.tooth_lower;
        }
    }

    public ToothPositionModel getToothPosition() {
        return toothPosition;
    }

    public ToothState getToothState() {
        return toothState;
    }

    public int getToothImage() {
        return toothImage;
    }

    public int getUid() {
        return uid;
    }
}

