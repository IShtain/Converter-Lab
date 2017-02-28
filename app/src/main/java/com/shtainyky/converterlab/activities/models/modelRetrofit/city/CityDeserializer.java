package com.shtainyky.converterlab.activities.models.modelRetrofit.city;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CityDeserializer implements JsonDeserializer<List<CityMap>> {
    @Override
    public List<CityMap> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        final List<CityMap> cities = new ArrayList<>();

        final Type type = new TypeToken<Map<String, String>>() {
        }.getType();

        final Map<String, String> cityMap =
                Collections.checkedMap(new Gson().<Map<String, String>>fromJson(json, type),
                        String.class, String.class);

        if (cityMap != null && !cityMap.isEmpty()) {
            for (final String key : cityMap.keySet()) {
                final CityMap city = new CityMap();
                city.setId(key);
                city.setCityName(cityMap.get(key));
                cities.add(city);
            }
        }

        return cities;
    }

}
