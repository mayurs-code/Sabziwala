package com.example.sabziwala.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.sabziwala.Service.UserMap;
import com.example.sabziwala.Service.communicator.ISO8601;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import static android.content.Context.CONNECTIVITY_SERVICE;


public class Utils {


    public static SortedMap<Currency, Locale> currencyLocaleMap;

    static {
        currencyLocaleMap = new TreeMap<Currency, Locale>(new Comparator<Currency>() {
            public int compare(Currency c1, Currency c2) {
                return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
            }
        });
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                Currency currency = Currency.getInstance(locale);
                currencyLocaleMap.put(currency, locale);
            } catch (Exception e) {
            }
        }
    }

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    public static boolean isBlankOrNull(View v) {
        return v == null;
    }

    public static String getDate(Date d) {
        SimpleDateFormat date = new SimpleDateFormat("y-M-d");
        return date.format(d);
    }

    public static String getTime(Date d) {
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:SS");
        return time.format(d);
    }
    public static Date toDate(String d,String t) {
        SimpleDateFormat dt = new SimpleDateFormat("y-M-d HH:mm:SS");
        try {
            return dt.parse(d+" "+t);
        } catch (ParseException e) {
           e.printStackTrace();
            return new Date();
        }


    }
    public static long getDifference(Date date) {
        long diffInMillies = (new Date().getTime() - date.getTime());
        long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;


    }public static long getDifferenceSec(Date date) {
        long diffInMillies = (new Date().getTime() - date.getTime());
        long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;


    }public static long getDifferenceHour(Date date) {
        long diffInMillies = (new Date().getTime() - date.getTime());
        long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;


    }public static long getDifferenceDays(Date date) {
        long diffInMillies = (new Date().getTime() - date.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;


    }

    public static boolean isBlankOrNull(String s) {
        return s == null || s.equalsIgnoreCase("null") || s.trim().equals("");
    }

    public static int isIntNull(Integer s) {
        Integer value = s;
        if (s == null) {
            value = 0;
        }
        return value;
    }

    public static void p(String source, String message) {
        Log.i(source + " ", "--" + message + "--");
    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getTime(int hour, int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, cal.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND));
        return cal.getTime();
    }

    public static String printDate(String userDateFormat, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(userDateFormat);
        return dateFormat.format(date);
    }

    public static String printDateTime(String userDateFormat, String userTimeFormat, Date date) {
        if (Utils.isBlankOrNull(userTimeFormat)) {
            userTimeFormat = "hh:mm a";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(userDateFormat + " " + userTimeFormat);
        return dateFormat.format(date);
    }

    public static String printTime(String userTimeFormat, Date date) {
        if (Utils.isBlankOrNull(userTimeFormat)) {
            userTimeFormat = "hh:mm a";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(userTimeFormat);
        return dateFormat.format(date);
    }

    public static String printTime(String userTimeFormat, Date date, boolean IsUTC) {
        if (Utils.isBlankOrNull(userTimeFormat)) {
            userTimeFormat = "hh:mm a";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(userTimeFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    public static String getCurrentTime() {
        DateFormat df = new SimpleDateFormat("h:mm:ss a");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String getISODateTimeFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    public static String getISOAbsoluteDate(Date date) {
        // this is sueful for date fields
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return dateFormat.format(date);
    }

    public static int getAge(int year, int month, int day, int hourOfDay, int minite) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day, hourOfDay, minite);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

//        Integer ageInt = new Integer(age);
//        String ageS = ageInt.toString();

        return age;
    }

    public static long generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }

    public static String getCurrencySymbol(String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        System.out.println(currencyCode + ":-" + currency.getSymbol(currencyLocaleMap.get(currency)));
        return currency.getSymbol(currencyLocaleMap.get(currency));
    }

    public static Gson getGson() {
        JsonSerializer<Date> ser = new JsonSerializer<Date>() {
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                return src == null ? null : new JsonPrimitive(src.getTime());
            }
        };
        JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                try {
                    return json == null ? null : ISO8601.getDateInUTC(json.getAsString());
                } catch (ParseException e) {
                    return null;
                }
            }
        };

        UserDeserializer deserUserMap = new UserDeserializer();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, ser)
                .registerTypeAdapter(Date.class, deser)
                .registerTypeAdapter(UserMap.class, deserUserMap)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return gson;
    }

    private static Gson getPrivateGson() {
        JsonSerializer<Date> ser = new JsonSerializer<Date>() {
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                return src == null ? null : new JsonPrimitive(src.getTime());
            }
        };
        JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                try {
                    return json == null ? null : ISO8601.getDateInUTC(json.getAsString());
                } catch (ParseException e) {
                    return null;
                }
            }
        };
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, ser).registerTypeAdapter(Date.class, deser).setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return gson;
    }

    public static String formatsNumber(double number) {

        return new DecimalFormat("#,###.00").format(number);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isInternetAvailable(Context mContext) {

        if (mContext != null) {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);

            NetworkInfo wifiNetwork = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetwork != null && wifiNetwork.isConnected()) {
                return true;
            }

            NetworkInfo mobileNetwork = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileNetwork != null && mobileNetwork.isConnected()) {
                return true;
            }

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            //activeNetwork.getType() == ConnectivityManager.TYPE_WIFI
            if (activeNetwork != null && activeNetwork.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidMobile(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    public static String getTag() {
        String tag = "";
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        for (int i = 0; i < ste.length; i++) {
            if (ste[i].getMethodName().equals("getTag")) {
                tag = "(" + ste[i + 1].getFileName() + ":" + ste[i + 1].getLineNumber() + ")";
            }
        }
        return tag;
    }


    public static void ShowToast(Context context, String message) {
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
    }


    static class UserDeserializer implements JsonDeserializer<UserMap> {
        public UserMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                String val = json.getAsString();
                UserMap map = val == null ? null : (getPrivateGson().fromJson(val, UserMap.class));
                return map;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }


}
