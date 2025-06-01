package com.org.candoit.domain.subgoal.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Color {

    COLOR_1("0x4091EE"),
    COLOR_2("0x70CCB1"),
    COLOR_3("0x7BBFF9"),
    COLOR_4("0x8C80DD"),
    COLOR_5("0xB1D854"),
    COLOR_6("0xEB4335"),
    COLOR_7("0xF09725"),
    COLOR_8("0xF7D04D");

    private final String hexValue;

    public static Color getColor(int index) {
        Color[] colors = values();
        return colors[index];
    }

    public String getHexValue() {
        return hexValue;
    }
}
