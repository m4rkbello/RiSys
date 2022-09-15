package com.softwaresolution.water_irrigation.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Permission {
    public Context context;
    public Activity activity;

    public Permission(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL);
        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkWriteExternal() {
        int result = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    public void reqWriteExternal() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(context)
                    .setCancelable(true)
                    .setTitle("Storage Permission")
                    .setMessage("Please allow storage permission in App Settings.")
                    .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
        }
    }
    public boolean checkReadExternal() {
        int result = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    public void reqReadExternal() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(context)
                    .setCancelable(true)
                    .setTitle("Storage Permission")
                    .setMessage("Please allow storage permission in App Settings.")
                    .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
        }
    }

    public boolean checkCamera() {
        int result = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    public void reqCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(context)
                    .setCancelable(true)
                    .setTitle("Storage Permission")
                    .setMessage("Please allow storage permission in App Settings.")
                    .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA}, 11111);
        }
    }


}
