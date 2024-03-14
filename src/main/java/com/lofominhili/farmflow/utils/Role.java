package com.lofominhili.farmflow.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN("ADMIN"),

    USER("USER");

    final String name;

}
