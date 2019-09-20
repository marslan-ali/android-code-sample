package co.appdev.invited.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import co.appdev.invited.R;

import static co.appdev.invited.InvitedApplication.getContext;

public final class ViewUtil {

    public static int[] imgArray = new int[]{R.drawable.icon_blue_person, R.drawable.icon_green_person, R.drawable.icon_red_person};
    public static String token = "";

    public static String getToken() {
        return token;
    }

    public static void setToken(String t) {
        token = t;
    }

    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    public static int dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static int getRandomIcon() {
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(ViewUtil.imgArray.length);
        return imgArray[index];
    }

    public static String formatDate(String dateStr) {

        SimpleDateFormat curFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");
        Date dateObj = null;
        Date dateObj1 = null;
        Date timeObj = null;
        try {
            Log.d("matchhhhh", String.valueOf(dateStr.matches("\\d{2}:\\d{2}:\\d{2}")));
            if (dateStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                dateObj = curFormater1.parse(dateStr);
                SimpleDateFormat curFormater2 = new SimpleDateFormat("dd-MMM-yyyy 'at' hh:mm a", Locale.US);
                return curFormater2.format(dateObj);
            }
            if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")){
                dateObj1 = dateOnly.parse(dateStr);
                SimpleDateFormat dateOnly2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                return dateOnly2.format(dateObj1);
            }
            if (dateStr.matches("\\d{2}:\\d{2}:\\d{2}")){
                timeObj = timeOnly.parse(dateStr);
                SimpleDateFormat timeOnly1 = new SimpleDateFormat("hh:mm a", Locale.US);
                return timeOnly1.format(timeObj);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return String.valueOf("");

    }

    public static String onlyFormatDate(String dateStr) {


        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");

        Date dateObj1 = null;
        try {

            if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")){
                dateObj1 = dateOnly.parse(dateStr);
                SimpleDateFormat dateOnly2 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                return dateOnly2.format(dateObj1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return String.valueOf("");

    }

    public static String onlyFormatDateSql(String dateStr) {


        SimpleDateFormat dateOnly = new SimpleDateFormat("dd-MM-yyyy");

        Date dateObj1 = null;
        try {

            if (dateStr.matches("\\d{2}-\\d{2}-\\d{4}")){
                dateObj1 = dateOnly.parse(dateStr);
                SimpleDateFormat dateOnly2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                return dateOnly2.format(dateObj1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return String.valueOf("");

    }


    public static String formatTime(int hourOfDay, int minute) {

        int hour = hourOfDay;
        int minutes = minute;
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String min = "";
        if (minutes < 10)
            min = "0" + minutes;
        else
            min = String.valueOf(minutes);

        String aTime = new StringBuilder().append(hour).append(':')
                .append(min).append(" ").append(timeSet).toString();

        return aTime;

    }
    // Search phone number from device contact list
    public static String contactExists(Context context, String number) {

        String permission = Manifest.permission.READ_CONTACTS;
        int res = getContext().checkCallingOrSelfPermission(permission);
        // number is the phone number
        if (res == PackageManager.PERMISSION_GRANTED) {
            Uri lookupUri = Uri.withAppendedPath(
                    ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                    Uri.encode(number));
            String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
            Cursor cursor = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
            try {
                if (cursor.moveToFirst()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Log.d("name", name);
                    return name;
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return String.valueOf("");
        }
        return permission;
    }
}
