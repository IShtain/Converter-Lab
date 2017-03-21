package com.shtainyky.converterlab.activities.fragments;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.shtainyky.converterlab.R;
import com.shtainyky.converterlab.activities.db.storedata.StoreData;
import com.shtainyky.converterlab.activities.models.modelUI.OrganizationUI;
import com.shtainyky.converterlab.activities.util.Util;
import com.shtainyky.converterlab.activities.util.logger.LogManager;
import com.shtainyky.converterlab.activities.util.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareDialogFragment extends DialogFragment {
    private static final String TAG = "ShareDialogFragment";
    private static Logger logger;
    private static final String ARG_ORGANIZATION_ID = "organization_id";
    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;

    private static final int HEADER_SIZE = 200;
    private static final int ONE_ITEM_SIZE = 60;
    private static final int TEXT_SIZE_BIG = 30;
    private static final int TEXT_SIZE_SMALL = 25;
    private static final int X_TEXT_LEFT_SMALL = 10;
    private static final int X_TEXT_MARGIN_BIG = 30;

    private OrganizationUI mOrganizationUI;
    private Paint mPaint;
    private Bitmap mBitmap;
    private SharedPreferences mPermissionStatus;

    @BindView(R.id.bt_share)
    Button shareButton;
    @BindView( R.id.iv)
    ImageView imageView;

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
        ButterKnife.bind(this, view);
        logger = LogManager.getLogger();
        getBundle();
        mBitmap = getBitmap();
        imageView.setImageBitmap(mBitmap);
        logger.d(TAG, "onCreateView ********");
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
        List<OrganizationUI.CurrencyUI> currencyUIs = mOrganizationUI.getCurrencies();
        int heightBitmap = HEADER_SIZE + ONE_ITEM_SIZE * currencyUIs.size();
        int widthBitmap = getBitmapWidth();
        logger.d(TAG, "widthBitmap) ==== >" + widthBitmap);
        Bitmap bitmap = Bitmap.createBitmap(widthBitmap, heightBitmap, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        drawHeaderBitmap(canvas);
        drawCurrencies(canvas, currencyUIs);
        return bitmap;
    }

    private void drawHeaderBitmap(Canvas canvas) {
        setColorAndSizeForBoldPaint(R.color.colorPrimaryDark, TEXT_SIZE_BIG);
        canvas.drawText(mOrganizationUI.getName(), X_TEXT_LEFT_SMALL, 60, mPaint);
        setColorAndSizeForNormalPaint(R.color.dark_grey, TEXT_SIZE_SMALL);
        canvas.drawText(mOrganizationUI.getRegionName(), X_TEXT_LEFT_SMALL, 95, mPaint);
        canvas.drawText(mOrganizationUI.getCityName(), X_TEXT_LEFT_SMALL, 130, mPaint);
    }

    private void drawCurrencies(Canvas canvas, List<OrganizationUI.CurrencyUI> currencyUIs) {
        for (int i = 0; i < currencyUIs.size(); i++) {
            setColorAndSizeForBoldPaint(R.color.dark_purple, TEXT_SIZE_BIG);
            canvas.drawText(currencyUIs.get(i).getCurrencyId(), X_TEXT_MARGIN_BIG, HEADER_SIZE + i * ONE_ITEM_SIZE, mPaint);
            setColorAndSizeForNormalPaint(R.color.dark_grey, TEXT_SIZE_BIG);
            String text = getTextForCurrencyAskBid(currencyUIs.get(i));
            Rect bounds = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawText(text, getBitmapWidth() - bounds.width() - X_TEXT_MARGIN_BIG, HEADER_SIZE + i * ONE_ITEM_SIZE, mPaint);
        }
    }

    private void saveAndShareBitmap(String fileName) throws Exception {
        FileOutputStream fOut = null;
        try {
            File path = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "ConverterLab");
            if (!path.mkdir()) {
                logger.d(TAG, "!path.mkdir() ==== >" + !path.mkdir());
            }
            File file = new File(path, fileName + System.currentTimeMillis() + ".png");
            fOut = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            shareFromFile(file);
        } finally {
            if (fOut != null)
                fOut.close();
        }
    }

    private void shareFromFile(File file) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        sharingIntent.setType("image/png");
        Intent shareIntent = Intent.createChooser(sharingIntent, "Share image using");
        if (Util.isIntentSafe(getActivity(), shareIntent))
            startActivity(shareIntent);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logger.d(TAG, "onActivityCreated ********");
        mPermissionStatus = getActivity().getSharedPreferences("mPermissionStatus", Context.MODE_PRIVATE);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    logger.d(TAG, "onActivityCreated checkSelfPermission");
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        logger.d(TAG, "onActivityCreated shouldShowRequestPermissionRationale");
                        //Show Information about why you need the permission
                        showDialogWithExplanation();
                    } else
                        if (mPermissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                        //Previously Permission Request was cancelled with 'Dont Ask Again',
                        // Redirect to Settings after showing Information about why you need the permission
                            logger.d(TAG, "onActivityCreated showDialogWithExplanationAfterDontAskAgainPressed");
                        showDialogWithExplanationAfterDontAskAgainPressed();
                    } else {
                        //just request the permission
                            logger.d(TAG, "onActivityCreated just request the permission");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_CALLBACK_CONSTANT);
                        }
                    }
                    savePermissionStatus();
                } else {
                    proceedAfterPermission();
                }
            }
        });
    }

    private void proceedAfterPermission() {
        try {
            logger.d(TAG, "proceedAfterPermission");
            saveAndShareBitmap(mOrganizationUI.getName());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), R.string.image_saving_problem, Toast.LENGTH_LONG).show();
        }
    }

    private void savePermissionStatus(){
        logger.d(TAG, "savePermissionStatus");
        SharedPreferences.Editor editor = mPermissionStatus.edit();
        editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
        editor.apply();
    }

    private void showDialogWithExplanationAfterDontAskAgainPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_need_permission);
        builder.setMessage(R.string.message_need_permission);
        builder.setPositiveButton(R.string.answer_grant, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                logger.d(TAG, "showDialogWithExplanationAfterDontAskAgainPressed ok");
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                Toast.makeText(getActivity(), R.string.message_path_to_permisions, Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.answer_cancel, null);
        builder.show();
    }

    private void showDialogWithExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_need_permission);
        builder.setMessage(R.string.message_need_permission);
        builder.setPositiveButton(R.string.answer_grant, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                logger.d(TAG, "showDialogWithExplanation ok");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_CALLBACK_CONSTANT);
                }
            }
        });
        builder.setNegativeButton(R.string.answer_cancel, null);
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)  {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            logger.d(TAG, "showDialogWithExplanation requestCode" + requestCode);
            boolean allGranted = false;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    allGranted = true;
                } else {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showDialogWithExplanation();
            } else {
                Toast.makeText(getActivity(), R.string.message_no_permission, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                logger.d(TAG, "onActivityResult requestCode" + requestCode);
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

}
