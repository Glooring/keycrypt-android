package com.example.keycrypt;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Toast toast;

    public static void showToast(Context context, String message) {
        // Cancel any existing Toast message
        if (toast != null) {
            toast.cancel();
        }

        // Create and show the new Toast with a shorter duration
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
