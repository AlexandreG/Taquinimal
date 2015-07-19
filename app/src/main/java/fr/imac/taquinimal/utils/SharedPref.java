package fr.imac.taquinimal.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import fr.imac.taquinimal.App;

/**
 * Created by AG on 19/07/2015.
 */
public class SharedPref {
    private static final String PREF_FILE_KEY = "taquinimal";

    private static final String BOARD_SIZE = "board_size";

    public static int fetchBoardSize() {
        return getInt(BOARD_SIZE, -1);
    }

    public static boolean updateBoardSize(int boardSize) {
        return setInt(BOARD_SIZE, boardSize);
    }


    @SuppressWarnings("unused")
    private static int getInt(String key, int defaultValue) {
        return App.getInstance().getContext()
                .getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE)
                .getInt(key, defaultValue);
    }

    @SuppressWarnings("unused")
    private static long getLong(String key, long defaultValue) {
        return App.getInstance().getContext()
                .getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE)
                .getLong(key, defaultValue);
    }

    static String getString(String key, String defaultValue) {
        return App.getInstance().getContext()
                .getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE)
                .getString(key, defaultValue);
    }

    @SuppressWarnings("unused")
    private static boolean getBoolean(String key, boolean defaultValue) {
        return App.getInstance().getContext()
                .getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE)
                .getBoolean(key, defaultValue);
    }

    @SuppressWarnings("unused")
    private static byte[] getBytes(String key) {
        return Base64.decode(getString(key, "").getBytes(), Base64.DEFAULT);
    }

    @SuppressWarnings("unused")
    private static boolean setInt(String key, int value) {
        SharedPreferences.Editor editor = App.getInstance().getContext()
                .getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE)
                .edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    @SuppressWarnings("unused")
    private static boolean setLong(String key, long value) {
        SharedPreferences.Editor editor = App.getInstance().getContext()
                .getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE)
                .edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    private static boolean setString(String key, String value) {
        SharedPreferences.Editor editor = App.getInstance().getContext()
                .getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE)
                .edit();
        editor.putString(key, value);
        return editor.commit();
    }

    @SuppressWarnings("unused")
    private static boolean setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = App.getInstance().getContext()
                .getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE)
                .edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    @SuppressWarnings("unused")
    private static boolean setBytes(String key, byte[] value) {
        return setString(key, new String(Base64.encode(value, Base64.DEFAULT)));
    }

    /**
     * Clear all preferences
     */
    public static void clearAllPreferences() {
        SharedPreferences.Editor editor = App.getInstance().getContext()
                .getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE)
                .edit();
        editor.clear();
        editor.commit();
    }
}
