package com.org.candoit.domain.subgoal.entity;

import lombok.Getter;

@Getter
public enum Color {

    COLOR_4091EE(0x4091EE),
    COLOR_70CCB1(0x70CCB1),
    COLOR_7BBFF9(0x7BBFF9),
    COLOR_8C80DD(0x8C80DD),
    COLOR_B1D854(0xB1D854),
    COLOR_EB4335(0xEB4335),
    COLOR_F09725(0xF09725),
    COLOR_F7D04D(0xF7D04D);

    private final int hexValue;

    Color(int hexValue) {this.hexValue = hexValue;}
}
