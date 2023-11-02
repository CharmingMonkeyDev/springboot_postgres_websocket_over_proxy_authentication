package com.sunshineoxygen.inhome.enums;

public enum Status {

    PASSIVE, ACTIVE, TEMPORARY;

    public static Status fromString(String text) {
        if (text != null) {
            for (Status b : Status.values()) {
                if (text.equalsIgnoreCase(b.toString())) {
                    return b;
                }
            }
        }
        return null;
    }

    public static Status fromCount(int text) {

        int i = 0;
        for (Status b : Status.values()) {

            if (i == text) {
                return b;
            }
        }

        return null;
    }

    public static int fromCount(String text) {

        int i = 0;
        for (Status b : Status.values()) {

            if (text.equalsIgnoreCase(b.toString())) {
                return i;
            }
            i++;
        }

        return 0;
    }

}