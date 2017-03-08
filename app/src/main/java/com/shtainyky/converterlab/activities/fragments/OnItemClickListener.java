package com.shtainyky.converterlab.activities.fragments;

import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;
public interface OnItemClickListener {

    void onCallClick(OrganizationUI organization);

    void onMapClick(OrganizationUI organization);

    void onLinkClick(OrganizationUI organization);

    void onDetailClick(OrganizationUI organization);
}

