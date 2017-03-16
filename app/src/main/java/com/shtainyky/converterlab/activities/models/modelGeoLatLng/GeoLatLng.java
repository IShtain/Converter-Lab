package com.shtainyky.converterlab.activities.models.modelGeoLatLng;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.shtainyky.converterlab.activities.logger.LogManager;
import com.shtainyky.converterlab.activities.logger.Logger;
import com.shtainyky.converterlab.activities.service.serverconection.GeoHttpManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class GeoLatLng {
    private static final String TAG = "GeoLatLng";
    private static Logger logger = LogManager.getLogger();

    private double lat;
    private double lng;

    public double getLat() {
        return lat;
    }

    private void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    private void setLng(double lng) {
        this.lng = lng;
    }

    public interface GeoPlaceListener {
        void onSuccess(GeoLatLng geoLatLng);
        void onFailure();

    }

    public static void getLatLng(Context context, String address, final GeoPlaceListener listener) {

        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            logger.d(TAG, "Geocoder addresses = " + e.getMessage());
        }
        if (addresses != null && addresses.size() > 0) {
            GeoLatLng geoLatLng = new GeoLatLng();
            logger.d(TAG, "Geocoder ");
            geoLatLng.setLat(addresses.get(0).getLatitude());
            geoLatLng.setLng(addresses.get(0).getLongitude());
            listener.onSuccess(geoLatLng);
        } else {
            logger.d(TAG, "GeoHttpManager");
            GeoHttpManager manager = GeoHttpManager.getInstance();
            manager.initGeoService();
            manager.getResponse(address, new GeoHttpManager.OnGeoResponseListener() {
                @Override
                public void onSuccess(String srtResponse) {
                    logger.d(TAG, "response = " + srtResponse);
                    GeoLatLng geoLatLngFromJson = getFromJson(srtResponse);
                    if (geoLatLngFromJson != null) {
                        GeoLatLng geoLatLng = new GeoLatLng();
                        geoLatLng.setLat(geoLatLngFromJson.getLat());
                        geoLatLng.setLng(geoLatLngFromJson.getLng());
                        logger.d(TAG, "lng = " + geoLatLngFromJson.getLat());
                        logger.d(TAG, "lat = " + geoLatLngFromJson.getLng());
                        listener.onSuccess(geoLatLng);
                    }
                    else listener.onFailure();
                }

                @Override
                public void onError(String message) {
                    logger.d(TAG, "onError = " + message);
                    listener.onFailure();
                }
            });
        }
    }

    private static GeoLatLng getFromJson(String strJson) {
        JSONObject jsonObject;
        GeoLatLng geoLatLng = null;
        try {
            jsonObject = new JSONObject(strJson);

            double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            geoLatLng = new GeoLatLng();
            geoLatLng.setLat(lat);
            geoLatLng.setLng(lng);

        } catch (JSONException e) {
            e.printStackTrace();
            logger.d(TAG, "JSONException = " + e.getMessage());
        }
        return geoLatLng;
    }
}
