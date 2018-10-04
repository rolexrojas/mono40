package com.tpago.movil.util;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.util.Size;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by solucionesgbh on 5/9/18.
 */

public final class Memory {
    public Memory() {

    }

    /**
     * Determine if there are enough megabyte in ram to be used
     * @param limitInMBs size to be used
     * @param context context of the app
     * @return True if the free space is bigger thant the size to be used
     */
    public static Boolean hasEnoughFreeSpace(long limitInMBs, Context context) {
        // Get the available memory
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        // Convert the memory to megabytes
        double availableMegs = mi.availMem / 0x100000L;

        return availableMegs > limitInMBs;
    }

    /**
     * Determine if the images can be displayed
     * @param context context of the app
     * @return True if the images can be displayed
     */
    public static Boolean canDisplayImageAnimation(Context context) {

        long neededSpace = 0;
        float density = context.getResources().getDisplayMetrics().density;
        if (density == 1.0) {
            neededSpace = SizeOfImage.MDPI.size();
        } else if(density == 1.5) {
            neededSpace = SizeOfImage.HDPI.size();
        } else if(density == 2.0) {
            neededSpace = SizeOfImage.XHDPI.size();
        } else if(density == 3.0) {
            neededSpace = SizeOfImage.XXHDPI.size();
        } else {
            neededSpace = SizeOfImage.XXXHDPI.size();
        }

        return hasEnoughFreeSpace(neededSpace, context);
    }

    /**
     * Sizes of images depending on the Dimension of the screen
     */
    public enum SizeOfImage {
        HDPI(420*315),
        MDPI(280*210),
        XHDPI(560*420),
        XXHDPI(840*630),
        XXXHDPI(1120*840);

        private long size;
        private final int RGBA = 4;
        private final int NUMBER_OF_IMAGES = 82;

        SizeOfImage(long size) {
            this.size = size;
        }

        public long size() {
            return (size * RGBA * NUMBER_OF_IMAGES) / (1024 * 1024);
        }
    }
}
