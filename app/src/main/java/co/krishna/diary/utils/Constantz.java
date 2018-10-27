package co.krishna.diary.utils;

import android.graphics.Color;
import android.text.TextUtils;

import java.util.Random;

/**
 * Created by srijan on 28/2/18.
 */

public class Constantz {
    public static final String PASSWORD = "password";
    public static final String LIST_UPDATED = "list_updated";
    public static final String DIARY_LIST_ID = "diary_list_id";

    public static boolean isEmpty(String string) {
        return TextUtils.isEmpty(string.trim());
    }

    public static int getRandomColor() {
        Random rand = new Random();
        return Color.argb(100, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }
}
