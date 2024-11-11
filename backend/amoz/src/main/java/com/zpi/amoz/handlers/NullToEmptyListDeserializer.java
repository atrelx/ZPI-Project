package com.zpi.amoz.handlers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class NullToEmptyListDeserializer extends JsonDeserializer<List<UUID>> {
    @Override
    public List<UUID> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.isExpectedStartArrayToken()) {
            return context.readValue(parser, List.class);
        } else {
            return List.of();
        }
    }
}

