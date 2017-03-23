package com.shtainyky.converterlab.models.modelRetrofit.currency;

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

public class CurrencyDeserializer implements JsonDeserializer<List<CurrencyMap>> {

    @Override
    public List<CurrencyMap> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        final List<CurrencyMap> currencies = new ArrayList<>();

        final Type type = new TypeToken<Map<String, String>>() {
        }.getType();

        final Map<String, String> currencyMap =
                Collections.checkedMap(new Gson().<Map<String, String>>fromJson(json, type),
                        String.class, String.class);

        if (currencyMap != null && !currencyMap.isEmpty()) {
            for (final String key : currencyMap.keySet()) {
                final CurrencyMap currency = new CurrencyMap();
                currency.setId(key);
                currency.setCurrencyTitle(currencyMap.get(key));
                currencies.add(currency);
            }
        }

        return currencies;
    }
}

