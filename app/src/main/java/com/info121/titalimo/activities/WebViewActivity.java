package com.info121.titalimo.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.info121.titalimo.AbstractActivity;
import com.info121.titalimo.App;
import com.info121.titalimo.MainActivity;
import com.info121.titalimo.R;
import com.info121.titalimo.models.LoginRes;
import com.info121.titalimo.models.SaveShowPicRes;
import com.info121.titalimo.utils.FtpHelper;
import com.info121.titalimo.utils.PrefDB;

import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class WebViewActivity extends AbstractActivity {
    private static final int REQUEST_CAMERA_IMAGE = 2001;


    private final String FTP_URL = "118.200.199.248";
    private final String FTP_USER = "ipos";
    private final String FTP_PASSWORD = "iposftp";
    private final String FTP_DIR = "limopics";

    WebView mWebView;
    Toolbar mToolbar;
    ProgressBar mProgressBar;

    String mFileName;
    String mJobNo;

    String url = "";

    PrefDB prefDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        initializeControls();
    }

    void initializeControls() {
        prefDB = new PrefDB(getApplicationContext());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Welcome " + App.userName);
        setSupportActionBar(mToolbar);

        mWebView = (WebView) findViewById(R.id.web_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        WebSettings webSettings = mWebView.getSettings();

        mWebView.setVisibility(View.GONE);

        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        mWebView.setInitialScale(100);
        mWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");

        url = String.format(App.CONST_URL_JOB_LIST, App.userName);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }
        });

        mWebView.loadUrl(url);

        App.targetContent = WebViewActivity.this;
    }

    public class JavaScriptInterface {
        private final WebViewActivity webViewActivity;

        JavaScriptInterface(WebViewActivity webViewActivity) {
            this.webViewActivity = webViewActivity;
        }

        @JavascriptInterface
        public void openCamera(final String fileName) {
            Log.e("Test : ", fileName);
        }

        @JavascriptInterface
        public void openCamera(final String fileName, final String jobNo) {

            mFileName = fileName;
            mJobNo = jobNo;

            webViewActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra("filename", fileName);
                    startActivityForResult(intent, REQUEST_CAMERA_IMAGE);
                }
            });
        }

        @JavascriptInterface
        public void phoneCall(String phoneNo) {

            Uri number = Uri.parse("tel:" + phoneNo);
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent);

            Toast.makeText(getApplicationContext(), "Phone Call .... to  " + phoneNo, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void sendSMS(String phoneNo) {
            String msg = "";
            Uri number = Uri.parse("sms:" + phoneNo);
            Intent smsIntent = new Intent(Intent.ACTION_VIEW, number);
            startActivity(smsIntent);

            Toast.makeText(getApplicationContext(), "Send SMS .... to  " + phoneNo, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void openFile(String url) {
            Uri uri = Uri.parse(url);

            // create an intent builder
            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

            // Begin customizing
            // set toolbar colors
            intentBuilder.setToolbarColor(ContextCompat.getColor(WebViewActivity.this, R.color.colorPrimary));
            intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(WebViewActivity.this, R.color.colorPrimaryDark));

            // set start and exit animations
            //            intentBuilder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
            //            intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left,
            //                    android.R.anim.slide_out_right);

            // build custom tabs intent
            CustomTabsIntent customTabsIntent = intentBuilder.build();

            // launch the url
            customTabsIntent.launchUrl(WebViewActivity.this, uri);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 0, Menu.NONE, "Refresh")
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        mProgressBar.setVisibility(View.VISIBLE);
                        mWebView.loadUrl(url);
                        return true;
                    }
                })
                .setIcon(ContextCompat.getDrawable(WebViewActivity.this, R.mipmap.ic_autorenew_white_24dp))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add(Menu.NONE, 1, Menu.NONE, "Logout")
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        finish();
                        prefDB.remove(App.CONST_ALREADY_LOGIN);
                        App.userName = null;
                        return true;
                    }
                })
                .setIcon(ContextCompat.getDrawable(WebViewActivity.this, R.mipmap.ic_exit_to_app_white_24dp))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


//        menu.add(Menu.NONE, 2, Menu.NONE, "Camera")
//                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                        startActivityForResult(intent, REQUEST_CAMERA_IMAGE);
//                        return true;
//                    }
//                })
//                .setIcon(ContextCompat.getDrawable(WebViewActivity.this, R.mipmap.ic_exit_to_app_white_24dp))
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Subscribe
    public void onEvent(SaveShowPicRes res) {
        if (res.getSaveshowpicResult().equalsIgnoreCase("Success")) {
            Log.e("Save Show Pic : ", "Success... ");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA_IMAGE && resultCode == RESULT_OK) {
            try {

                Bundle extras = data.getExtras();

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));

                InputStream inputStream = getContentResolver().openInputStream(tempUri);

                FtpHelper.uploadTask async = new FtpHelper.uploadTask(WebViewActivity.this, inputStream);
                async.execute(FTP_URL, FTP_USER, FTP_PASSWORD, FTP_DIR, mFileName, mJobNo);   //Passing arguments to AsyncThread


            } catch (Exception e) {
                Log.e("Camera Error : ", e.getLocalizedMessage().toString());
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


}



