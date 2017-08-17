package com.info121.mylimo.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.info121.mylimo.R;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by KZHTUN on 7/20/2017.
 */

public class Util {

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getDeviceID(Context context){
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static boolean isNullOrEmpty(String string) {
        return (string == null || string.length()==0 || string.toString().trim()== "");
    }

    static String printStackTrace(@NonNull Throwable exception) {
        StringWriter trace = new StringWriter();
        exception.printStackTrace(new PrintWriter(trace));
        return trace.toString();
    }

    public static void copyToClipboard(Context context, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Copy To Clipboard", Toast.LENGTH_SHORT).show();
    }

    static String safeReplaceToAlphanum(@Nullable String string) {
        if (string == null) {
            return null;
        }
        return string.replaceAll("[^a-zA-Z0-9]", "");
    }
}
