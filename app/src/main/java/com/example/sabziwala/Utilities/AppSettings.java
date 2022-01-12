package com.example.sabziwala.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSettings {

    private static SharedPreferences getPrefs(Context paramContext) {
        return PreferenceManager.getDefaultSharedPreferences(paramContext);
    }

    public static void clearPrefs(Context paramContext) {
        SharedPreferences preferences = getPrefs(paramContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static boolean isLogin(Context paramContext) {
        return getPrefs(paramContext).getBoolean("isLogin", false);
    }

    public static void setLogin(Context paramContext, boolean islogin) {
        getPrefs(paramContext).edit().putBoolean("isLogin", islogin).commit();
    }

    public static boolean isSetProfile(Context paramContext) {
        return getPrefs(paramContext).getBoolean("isSetProfile", false);
    }

    public static void setProfileOnce(Context paramContext, boolean isSetProfile) {
        getPrefs(paramContext).edit().putBoolean("isSetProfile", isSetProfile).commit();
    }


    public static String getUId(Context context) {
        return getPrefs(context).getString("UId", "");
    }

    public static void setUId(Context context, String paramString) {
        getPrefs(context).edit().putString("UId", paramString).commit();
    }
    public static String getSessionKey(Context context) {
        return getPrefs(context).getString("SessionKey", "");
    }

    public static void setSessionKey(Context context, String paramString) {
        getPrefs(context).edit().putString("SessionKey", paramString).commit();
    }


    public static String getFirstName(Context context) {
        return getPrefs(context).getString("firstName", null);
    }

    public static void setFirstName(Context context, String paramString) {
        getPrefs(context).edit().putString("firstName", paramString).commit();
    }


    public static String getLastName(Context context) {
        return getPrefs(context).getString("lastName", null);
    }

    public static void setLastName(Context context, String paramString) {
        getPrefs(context).edit().putString("lastName", paramString).commit();
    }


    public static String getPhone(Context context) {
        return getPrefs(context).getString("phone", null);
    }

    public static void setPhone(Context context, String paramString) {
        getPrefs(context).edit().putString("phone", paramString).commit();
    }

    public static String getUserPic(Context context) {
        return getPrefs(context).getString("userPic", null);
    }

    public static void setUserPic(Context context, String paramString) {
        getPrefs(context).edit().putString("userPic", paramString).commit();
    }

    public static String getUserEmail(Context context) {
        return getPrefs(context).getString("userEmail", null);
    }

    public static void setUserEmail(Context context, String paramString) {
        getPrefs(context).edit().putString("userEmail", paramString).commit();
    }

    public static String getUserName(Context context) {
        return getPrefs(context).getString("username", null);
    }

    public static void setUserName(Context context, String paramString) {
        getPrefs(context).edit().putString("username", paramString).commit();
    }


    public static void setUserToken(Context context, String paramString) {
        getPrefs(context).edit().putString("usertoken", paramString).commit();
    }

    public static String getPurchaseToken(Context context) {
        return getPrefs(context).getString("planId", "");
    }

    public static void setPurchaseToken(Context context, String paramString) {
        getPrefs(context).edit().putString("planId", paramString).commit();
    }
}
