package com.info121.mylimo.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.adeel.library.easyFTP;
import com.info121.mylimo.App;
import com.info121.mylimo.R;
import com.info121.mylimo.api.APIClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by KZHTUN on 8/3/2017.
 */


public class FtpHelper {

    public static class uploadTask extends AsyncTask<String, String , String> {
        ProgressDialog prg;
        Context context;
        InputStream inputStream;
        String mJobNo;
        String mFileName;

        public uploadTask(Context context, InputStream inputStream) {
            this.context = context;
            this.inputStream = inputStream;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prg = new ProgressDialog(context);
            prg.setMessage("Initializing ...");
            prg.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                easyFTP ftp = new easyFTP();

                ftp.connect(params[0], params[1], params[2]);

                boolean status = false;

                if (!params[3].isEmpty()) {
                    status = ftp.setWorkingDirectory(params[3]); // if User say provided any Destination then Set it , otherwise
                }
                // Upload will be stored on Default /root level on server
                publishProgress("Uploading ...");
                ftp.uploadFile(inputStream, params[4]);

                publishProgress("Upload Successful ...");
                mFileName = params[4];
                mJobNo = params[5];

                return new String("Upload Successful");

            } catch (Exception e) {
                String t = "Failure : " + e.getLocalizedMessage();
                return t;
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            prg.setMessage(values[0]);
        }



        @Override
        protected void onPostExecute(String str) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    prg.dismiss();
                }
            }, 2000);

            APIClient.SaveShowPicture(App.userName, mJobNo, mFileName);

            // Toast.makeText(demo.this,str,Toast.LENGTH_LONG).show();
        }
    }

    public class downloadTask extends AsyncTask<String, Void, String> {
        ProgressDialog prg;
        Context context;
        InputStream inputStream;

        public downloadTask(Context context) {
            this.context = context;
            this.inputStream = inputStream;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             prg = new ProgressDialog(context);
            prg.setMessage("Downloading...");
            prg.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                easyFTP ftp = new easyFTP();
//
                ftp.connect(params[0], params[1], params[2]);
                ftp.downloadFile(params[3], params[4]);
                return new String("Download Successful");
            } catch (Exception e) {
                String t = "Failure : " + e.getLocalizedMessage();
                return t;
            }
        }

        @Override
        protected void onPostExecute(String str) {
            prg.dismiss();
            // Toast.makeText(demo.this,str,Toast.LENGTH_LONG).show();
        }
    }

}
