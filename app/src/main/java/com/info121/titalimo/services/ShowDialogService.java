package com.info121.titalimo.services;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.info121.titalimo.R;
import com.info121.titalimo.activities.DialogActivity;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class ShowDialogService extends Service {
    public static final String JOB_NO = "JOB_NO";
    public static final String NAME = "NAME";
    public static final String PHONE = "PHONE";
    public static final String MESSAGE = "MESSAGE";


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createDialog(intent);
        stopSelf();


        return super.onStartCommand(intent, flags, startId);
    }

    private void createDialog(Intent intent) {

        final Dialog dialog = new Dialog(this, R.style.Theme_AppCompat);

        final Spinner mPhones;
        Button mDismiss, mCall, mConfirm;
        TextView mMessage;

        dialog.setContentView(R.layout.dialog_prominent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = dialog.getWindow();

        dialog.setTitle("New Jobs");
        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);

        //adding dialog animation sliding up and down
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();

        mCall = (Button) dialog.findViewById(R.id.btn_call);
        mConfirm = (Button) dialog.findViewById(R.id.btn_confirm);
        mDismiss = (Button) dialog.findViewById(R.id.btn_remind_later);
        mMessage = (TextView) dialog.findViewById(R.id.message);
        mPhones = (Spinner) dialog.findViewById(R.id.phone_spinner);


        mDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "Dismiss Click");
                dialog.dismiss();
            }
        });


        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneCall(mPhones.getSelectedItem().toString());
                dialog.dismiss();
            }
        });

        // Display Message
        Bundle bundle = intent.getExtras();

        String phones = intent.getExtras().getString(PHONE);
        String message = intent.getExtras().getString(MESSAGE);


//        // for testing purpose only
//        String phones = "09965042219/095137664";
//        String message = "Hi askldfalk adsf asdf adsfj alsfaklsf klasdfj aldsf aldfs afsl";


        // Fill the data
        mMessage.setText(message);
        mPhones.setAdapter(fillPhoneNumbers(phones));

        // Wake Screen
        PowerManager.WakeLock screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();

        screenLock.release();


    }

    private ArrayAdapter<String> fillPhoneNumbers(String phones) {
        String p[] = phones.split("/");

        List<String> phoneList = new ArrayList<String>();

        for (String s : p) {
            phoneList.add(s.trim());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, phoneList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }

    public void phoneCall(String phoneNo) {

        Uri number = Uri.parse("tel:" + phoneNo);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);

        Toast.makeText(getApplicationContext(), "Phone Call .... to  " + phoneNo, Toast.LENGTH_SHORT).show();
    }

    private void createDialogActivity() {
        Intent dialogIntent = new Intent(this, DialogActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);

    }


}
