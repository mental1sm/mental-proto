package com.mentalism.shared;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Mapper extends ObjectMapper {

    private static Mapper instance;

    private Mapper() {}

    public static Mapper getInstance() {
        if (instance == null) {
            instance = new Mapper();
        }
        return instance;
    }
}
