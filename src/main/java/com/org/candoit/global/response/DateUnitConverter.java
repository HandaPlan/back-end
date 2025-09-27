package com.org.candoit.global.response;

import com.org.candoit.domain.subprogress.dto.DateUnit;
import com.org.candoit.domain.subprogress.dto.Direction;
import java.util.Arrays;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DateUnitConverter implements Converter<String, DateUnit> {

    @Override
    public DateUnit convert(String source) {
        try {
            return Arrays.stream(DateUnit.values())
                .filter(v -> v.name().equalsIgnoreCase(source))
                .findFirst()
                .orElseThrow();
        } catch (Exception cause) {
            throw new ConversionFailedException(
                TypeDescriptor.valueOf(String.class),
                TypeDescriptor.valueOf(Direction.class),
                source,
                cause
            );
        }
    }
}
