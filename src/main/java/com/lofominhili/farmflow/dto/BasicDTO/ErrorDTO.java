package com.lofominhili.farmflow.dto.BasicDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ErrorDTO implements Serializable {

    private final Integer statusCode;

    private final String exception;

    private final String message;
}
