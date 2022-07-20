package com.example.msharp;

import android.util.Log;

public class Logging {
    public static void error(String TAG, Exception e) {
        Log.e(TAG, e.getMessage());
        System.out.println(TAG + ": " + e.getMessage());
    }
}
