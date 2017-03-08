package com.shtainyky.converterlab.activities.fragments;


public interface OnOrganizationClickListener {

    void onCallClick(String organizationPhone);

    void onMapClick(String organizationAddress);

    void onLinkClick(String organizationLink);

    void onDetailClick(String organizationID);

}