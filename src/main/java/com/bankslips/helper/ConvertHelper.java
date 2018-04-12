package com.bankslips.helper;

import com.bankslips.domain.BaseEntity;
import com.bankslips.dto.BaseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConvertHelper {

    @Autowired
    ObjectMapper objectMapper;

    public <I, O> O convert(I input, Class<O> outputClass) {
        return input == null ? null : objectMapper.convertValue(input, outputClass);
    }
}
