package com.softwaresolution.water_irrigation.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SavePicture {
    public static File createImageFile(Context context,String filename) throws IOException {
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                filename,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }
    public static File saveImage(Context context,Bitmap myBitmap,String filename ,
                                 File filePath) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File storageDir = filePath;
        File f = null;
        try {
            String id = filename+".jpg";
            f = new File(storageDir, id);
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(context,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return f;
    }
}