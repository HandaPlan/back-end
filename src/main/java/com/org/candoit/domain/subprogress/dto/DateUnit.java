package com.org.candoit.domain.subprogress.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DateUnit {
    WEEK, MONTH;

    @JsonValue
    public String getValue(){
        return this.getValue().toLowerCase();
    }
}
