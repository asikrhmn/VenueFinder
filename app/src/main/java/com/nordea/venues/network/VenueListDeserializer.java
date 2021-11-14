package com.nordea.venues.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nordea.venues.entities.Venue;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VenueListDeserializer implements JsonDeserializer<List<Venue>> {
    @Override
    public List<Venue> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Venue> items = new ArrayList<>();

        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonArray itemsJsonArray = jsonObject.getAsJsonObject("response").getAsJsonArray("venues");

        for (JsonElement itemsJsonElement : itemsJsonArray) {
            final JsonObject itemJsonObject = itemsJsonElement.getAsJsonObject();
            Venue venue = new Venue();
            final String name = itemJsonObject.get("name").getAsString();
            venue.setName(name);
            final JsonObject locationObject = itemJsonObject.getAsJsonObject("location");
            if (locationObject != null) {
                JsonElement distanceElement = locationObject.get("distance");
                venue.setDistance(distanceElement != null ? distanceElement.getAsInt() : 0);
                JsonElement addressElement = locationObject.get("formattedAddress");
                if (addressElement != null) {
                    JsonArray jsonArray = addressElement.getAsJsonArray();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (JsonElement location : jsonArray) {
                        if (stringBuilder.length() > 0) {
                            stringBuilder.append(", ");
                        }
                        stringBuilder.append(location.getAsString());
                    }
                    venue.setAddress(stringBuilder.toString());
                }
            }
            items.add(venue);
        }
        return items;
    }
}