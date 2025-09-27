package com.org.candoit.domain.subprogress.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Direction {
    PREV, CURRENT, NEXT;

    @JsonValue
    public String getValue(){
        return this.getValue().toLowerCase();
    }
}
