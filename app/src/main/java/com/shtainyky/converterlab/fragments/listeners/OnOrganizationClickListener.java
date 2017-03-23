package com.shtainyky.converterlab.fragments.listeners;


import com.shtainyky.converterlab.models.modelUI.OrganizationUI;

public interface OnOrganizationClickListener {

   void onCallClick(String organizationPhone);

   void onMapClick(String organizationAddress);

   void onLinkClick(String organizationLink);

   void onDetailClick(OrganizationUI organization);

   void onShareClick(OrganizationUI organization);

}