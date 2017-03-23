package com.shtainyky.converterlab.models.modelRetrofit.region;

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

public class RegionDeserializer implements JsonDeserializer<List<RegionMap>> {

    @Override
    public List<RegionMap> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        final List<RegionMap> regions = new ArrayList<>();

        final Type type = new TypeToken<Map<String, String>>() {
        }.getType();

        final Map<String, String> regionMap =
                Collections.checkedMap(new Gson().<Map<String, String>>fromJson(json, type),
                        String.class, String.class);

        if (regionMap != null && !regionMap.isEmpty()) {
            for (final String key : regionMap.keySet()) {
                final RegionMap region = new RegionMap();
                region.setId(key);
                region.setRegionName(regionMap.get(key));
                regions.add(region);
            }
        }

        return regions;
    }

}