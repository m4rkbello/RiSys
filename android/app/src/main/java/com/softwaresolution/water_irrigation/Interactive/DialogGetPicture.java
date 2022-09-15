package com.softwaresolution.water_irrigation.Interactive;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.softwaresolution.water_irrigation.Utils.BitmapUtils;
import com.softwaresolution.water_irrigation.Utils.Permission;
import com.softwaresolution.water_irrigation.Utils.SavePicture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class DialogGetPicture extends DialogFragment {
    private  String TAG = "DialogGetPicture";
    private Permission permission;
    private  IDialogGetPicture iDialogGetPicture;

    private static final int CAMERA_REQUEST = 98;
    private static final int RESULT_LOAD_IMG = 778;

    private View v;


    public void setOnGetPicture(IDialogGetPicture iDialogGetPicture) {
        this.iDialogGetPicture = null;
        this.iDialogGetPicture = iDialogGetPicture;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        permission = new Permission(getContext(),getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please provide your picture");
        builder.setPositiveButton("Camera",null);
        builder.setNegativeButton("Gallery",null);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positive = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    Boolean wantToCloseDialog = false;
//                    //Do stuff, possibly set wantToCloseDialog to true then...
//                    if(wantToCloseDialog)
//                        d.dismiss();
                    onCamera();
                }
            });
            Button negative = (Button) d.getButton(Dialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    Boolean wantToCloseDialog = false;
//                    //Do stuff, possibly set wantToCloseDialog to true then...
//                    if(wantToCloseDialog)
//                        d.dismiss();
                    onGallery();
                }
            });
        }
    }

    private String currentPhotoPath = "";
    public void onCamera() {

        if (!permission.checkCamera()){
            permission.reqCamera();
        }else{
//            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(cameraIntent, CAMERA_REQUEST);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                File photoFile = null;
                try {
                    photoFile = SavePicture.
                            createImageFile(getContext(),
                                    "defaultImg");
                } catch (IOException ex) {
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
                            "com.softwaresolution.water_irrigation",
                            photoFile);
                    Log.d(TAG,String.valueOf(photoFile));
                    currentPhotoPath = String.valueOf(photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                }
            }
        }
    }

    public void onGallery() {
        if (!permission.checkReadExternal()){
            permission.reqReadExternal();
        }else{
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        }
    }

    private boolean isClose= false;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iDialogGetPicture = null;
    }

    public interface IDialogGetPicture {
        void onGetPicture(String decodeBitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Camera Request
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            File file = new File(currentPhotoPath);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media
                        .getBitmap(getContext().getContentResolver(), Uri.fromFile(file));
                if (bitmap != null) {
                    iDialogGetPicture.onGetPicture(getRightAngleImage(BitmapUtils.encodeTobase64(
                            BitmapUtils.getResizedBitmap(bitmap,500))));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            isClose = true;
            dismiss();
        }

        //Gallery Request
        if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK)
        {
            final Uri imageUri = data.getData();
            Bitmap bitmap =
                    null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),
                        imageUri);
                 iDialogGetPicture.onGetPicture(getRightAngleImage(
                         BitmapUtils.encodeTobase64(BitmapUtils.getResizedBitmap(bitmap,500))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            isClose = true;
            dismiss();
        }
    }
    private String getRightAngleImage(String photoPath) {
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degree = 0;

            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
                default:
                    degree = 90;
            }

            return rotateImage(degree,photoPath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return photoPath;
    }
    private String rotateImage(int degree, String imagePath){

        if(degree<=0){
            return imagePath;
        }
        try{
            Bitmap b= BitmapFactory.decodeFile(imagePath);

            Matrix matrix = new Matrix();
            if(b.getWidth()>b.getHeight()){
                matrix.setRotate(degree);
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                        matrix, true);
            }

            FileOutputStream fOut = new FileOutputStream(imagePath);
            String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            String imageType = imageName.substring(imageName.lastIndexOf(".") + 1);

            FileOutputStream out = new FileOutputStream(imagePath);
            if (imageType.equalsIgnoreCase("png")) {
                b.compress(Bitmap.CompressFormat.PNG, 100, out);
            }else if (imageType.equalsIgnoreCase("jpeg")|| imageType.equalsIgnoreCase("jpg")) {
                b.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            fOut.flush();
            fOut.close();

            b.recycle();
        }catch (Exception e){
            e.printStackTrace();
        }
        return imagePath;
    }
}
