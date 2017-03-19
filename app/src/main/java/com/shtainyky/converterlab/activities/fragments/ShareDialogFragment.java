package com.shtainyky.converterlab.activities.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.db.storedata.StoreData;
import com.shtainyky.converterlab.activities.util.logger.LogManager;
import com.shtainyky.converterlab.activities.util.logger.Logger;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;
import com.shtainyky.converterlab.activities.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.ButterKnife;

public class ShareDialogFragment extends DialogFragment {
    private static final String TAG = "DetailFragment";
    private static Logger logger;
    public static final String ARG_ORGANIZATION_ID = "organization_id";

    public static final int HEADER_SIZE = 200;
    public static final int ONE_ITEM_SIZE = 60;
    public static final int TEXT_SIZE_BIG = 30;
    public static final int TEXT_SIZE_SMALL = 25;
    public static final int X_TEXT_LEFT_SMALL = 10;
    public static final int X_TEXT_MARGIN_BIG = 30;

    private OrganizationUI mOrganizationUI;
    private Paint mPaint;
    private Bitmap mBitmap;

    public static ShareDialogFragment newInstance(String organizationID) {
        Bundle args = new Bundle();
        args.putString(ARG_ORGANIZATION_ID, organizationID);

        ShareDialogFragment fragment = new ShareDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        logger = LogManager.getLogger();
        getBundle();
        mBitmap = getBitmap();
        final ImageView imageView = ButterKnife.findById(view, R.id.iv);
        imageView.setImageBitmap(mBitmap);
        Button shareButton = ButterKnife.findById(view, R.id.bt_share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBitmap("currencies_photo");
            }
        });
        return view;
    }

    private void getBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String orgID = bundle.getString(ARG_ORGANIZATION_ID);
            mOrganizationUI = StoreData.getInstance().getOrganizationForID(orgID);
            logger.d(TAG, "savedInstanceState.getString(ARG_ORGANIZATION_ID) = " + orgID);
        }
    }

    private Bitmap getBitmap() {
        int widthBitmap = getBitmapWidth();
        logger.d(TAG, "widthBitmap) ==== >" + widthBitmap);
        List<OrganizationUI.CurrencyUI> currencyUIs = mOrganizationUI.getCurrencies();
        int currenciesSize = currencyUIs.size();
        int heightBitmap = HEADER_SIZE + ONE_ITEM_SIZE * currenciesSize;
        Bitmap bitmap = Bitmap.createBitmap(widthBitmap, heightBitmap, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        logger.d(TAG, "canvas.getWidth() ==== >" + canvas.getWidth());
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);

        setColorAndSizeForBoldPaint(R.color.colorPrimaryDark, TEXT_SIZE_BIG);
        canvas.drawText(mOrganizationUI.getName(), X_TEXT_LEFT_SMALL, 60, mPaint);
        setColorAndSizeForNormalPaint(R.color.dark_grey, TEXT_SIZE_SMALL);
        canvas.drawText(mOrganizationUI.getRegionName(), X_TEXT_LEFT_SMALL, 95, mPaint);
        canvas.drawText(mOrganizationUI.getCityName(), X_TEXT_LEFT_SMALL, 130, mPaint);

        for (int i = 0; i < currenciesSize; i++) {
            setColorAndSizeForBoldPaint(R.color.dark_purple, TEXT_SIZE_BIG);
            canvas.drawText(currencyUIs.get(i).getCurrencyId(), X_TEXT_MARGIN_BIG, HEADER_SIZE + i * ONE_ITEM_SIZE, mPaint);
            setColorAndSizeForNormalPaint(R.color.dark_grey, TEXT_SIZE_BIG);
            String text = getTextForCurrencyAskBid(currencyUIs.get(i));
            Rect bounds = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawText(text, widthBitmap - bounds.width() - X_TEXT_MARGIN_BIG, HEADER_SIZE + i * ONE_ITEM_SIZE, mPaint);
        }
        return bitmap;
    }

    private void shareBitmap(String fileName) {
        try {
            File file = new File(getActivity().getCacheDir(), fileName + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            sharingIntent.setType("image/png");
            Intent shareIntent = Intent.createChooser(sharingIntent, "Share image using");
            if (Util.isIntentSafe(getActivity(), shareIntent))
                startActivity(shareIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getBitmapWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        (getActivity()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        if (width < height)
            return width - 150;
        else
            return height - 150;
    }

    private String getTextForCurrencyAskBid(OrganizationUI.CurrencyUI currencyUI) {
        DecimalFormat format = new DecimalFormat("00.00");
        return format.format(currencyUI.getBid()) +
                "/" + format.format(currencyUI.getAsk());
    }

    private void setColorAndSizeForBoldPaint(int colorForPaint, int size) {
        mPaint.setColor(ContextCompat.getColor(getActivity(), colorForPaint));
        mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        mPaint.setTextSize(size);
    }

    private void setColorAndSizeForNormalPaint(int colorForPaint, int size) {
        mPaint.setColor(ContextCompat.getColor(getActivity(), colorForPaint));
        mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        mPaint.setTextSize(size);
    }


}
