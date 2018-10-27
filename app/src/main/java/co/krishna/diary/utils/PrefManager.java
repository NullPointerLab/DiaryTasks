package co.krishna.diary.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by srijan on 23/2/18.
 */

public class PrefManager {
    private static SharedPreferences sharedPrefs;
    private static final String PREF_NAME = "PrefManager";

    public static Boolean clearPrefrences(Context context) {
        sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public static void setPrefValue(Context context, String key, String value) {
        sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPrefs.edit().putString(key, value).apply();

    }

    public static String getPrefValue(Context context, String key) {
        sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getString(key, "");
    }


    public static void setBooleanPrefValue(Context context, String key, Boolean value) {
        sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPrefs.edit().putBoolean(key, value).apply();

    }

    public static Boolean getBooleanPrefValue(Context context, String key) {
        sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(key, false);
    }

}
