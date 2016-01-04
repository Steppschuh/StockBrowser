package net.steppschuh.stockbrowser.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public final class ImageHelper {

    public static final int RADIUS_SMALL = 2;
    public static final int RADIUS_DEFAULT = 4;
    public static final int RADIUS_HIGH = 6;

    /**
     * Returns a blurred bitmap using renderscript
     */
    public static Bitmap blurBitmap (Bitmap originalBitmap, int radius, Context context) {
        // Create another bitmap that will hold the results of the filter.
        Bitmap blurredBitmap;
        blurredBitmap = Bitmap.createBitmap(originalBitmap);

        // Create the Renderscript instance that will do the work.
        RenderScript rs = RenderScript.create(context);

        // Allocate memory for Renderscript to work with
        Allocation input = Allocation.createFromBitmap(rs, originalBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SCRIPT);
        Allocation output = Allocation.createTyped(rs, input.getType());

        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setInput(input);

        // Set the blur radius
        script.setRadius(radius);

        // Start the ScriptIntrinsicBlur
        script.forEach(output);

        // Copy the output to the blurred bitmap
        output.copyTo(blurredBitmap);

        return blurredBitmap;
    }

    /**
     * Returns a resized bitmap, maintaining the original aspect ratio
     */
    public static Bitmap getResizedBitmap(Bitmap originalBitmap, int newWidth) {
        float aspectRatio = originalBitmap.getWidth() / (float) originalBitmap.getHeight();
        int newHeight = Math.round(newWidth / aspectRatio);
        return getResizedBitmap(originalBitmap, newWidth, newHeight);
    }

    /**
     * Returns a resized bitmap
     */
    public static Bitmap getResizedBitmap(Bitmap originalBitmap, int newWidth, int newHeight) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

}
