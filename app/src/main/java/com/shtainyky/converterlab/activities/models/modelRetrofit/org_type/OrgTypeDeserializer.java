package com.shtainyky.converterlab.activities.models.modelRetrofit.org_type;

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

public class OrgTypeDeserializer implements JsonDeserializer<List<OrgTypeMap>> {

    @Override
    public List<OrgTypeMap> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final List<OrgTypeMap> orgTypes = new ArrayList<>();

        final Type type = new TypeToken<Map<String, String>>() {
        }.getType();

        final Map<String, String> orgTypeMap =
                Collections.checkedMap(new Gson().<Map<String, String>>fromJson(json, type),
                        String.class, String.class);
        if (orgTypeMap != null && !orgTypeMap.isEmpty()) {
            for (final String key : orgTypeMap.keySet()) {
                final OrgTypeMap orgType = new OrgTypeMap();
                orgType.setId(key);
                orgType.setType(orgTypeMap.get(key));
                orgTypes.add(orgType);
            }
        }

        return orgTypes;

    }
}
