package com.shtainyky.converterlab.activities.fragments.listeners;


import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;

public interface OnOrganizationClickListener {

   void onCallClick(String organizationPhone);

   void onMapClick(String organizationAddress);

   void onLinkClick(String organizationLink);

   void onDetailClick(OrganizationUI organization);

   void onShareClick(OrganizationUI organization);

}