package com.webber.webber.general;

import android.content.Context;
import android.content.SharedPreferences;

import com.webber.webber.Constants;

import java.util.Date;

/**
 * Created by AndrewYao on 2014/9/2.
 */
public class PreferenceManager {

    public static boolean checkLastUpdate(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE);

        Date lastSyncDate = new Date(sp.getLong("PersonSyncTime", 0));
        Date now = new Date(System.currentTimeMillis());

        long diff = now.getTime() - lastSyncDate.getTime();

        if (diff / (60 * 1000) > 1) {
            return false;
        } else
            return true;
    }

    public static long getLastUpdate(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE);
        return sp.getLong("PersonSyncTime", 0);
    }

    public static void setLastUpdate(Context context, long thisUpdate) {
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("PersonSyncTime", thisUpdate);
        editor.commit();
    }

    public static void setCurrentUID(Context context, String uid) {
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("uid", uid);
        editor.commit();
    }

    public static String getCurrentUID(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE);
        return sp.getString("uid", null);
    }
}
