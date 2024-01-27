package com.petya.teeth.app.enums;

import com.petya.teeth.app.R;

public enum ToothState {
    HEALTHY(R.string.healthy),
    FILLING(R.string.filling),
    ROOT_FILLING(R.string.root_filling),
    MISSING(R.string.missing),
    CROWN(R.string.crown),
    IMPLANT(R.string.implant),
    BRIDGE(R.string.bridge),
    BONDING(R.string.bonding),
    FACET(R.string.facet);

    private final int stringResId;

    ToothState(int stringResId) {
        this.stringResId = stringResId;
    }

    public int getStringResId() {
        return stringResId;
    }

    public static ToothState getByKey(int stringResId) {
        for (ToothState position : values()) {
            if (position.stringResId == stringResId) {
                return position;
            }
        }
        throw new IllegalArgumentException("No enum constant with key: " + stringResId);
    }
}
