package co.appdev.invited.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import co.appdev.invited.R;


public final class DialogFactory {

    private static AlertDialog alert;
    private static AlertDialog subscriptionAlert;

    public static boolean isAlertShown = false;

    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createSimpleOkErrorDialog(Context context,
                                                   @StringRes int titleResource,
                                                   @StringRes int messageResource) {

        return createSimpleOkErrorDialog(context,
                context.getString(titleResource),
                context.getString(messageResource));
    }

    public static Dialog createGenericErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialog_error_title))
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createGenericErrorDialog(Context context, @StringRes int messageResource) {
        return createGenericErrorDialog(context, context.getString(messageResource));
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context,
                                                      @StringRes int messageResource) {
        return createProgressDialog(context, context.getString(messageResource));
    }


    public static boolean checkInternetConnection(AppCompatActivity activity) {
        boolean results;

        if (!(results = NetworkUtil.isNetworkConnected(activity))) {
            Toast.makeText(activity, "Please connect your internet", Toast.LENGTH_LONG).show();

        }

        return results;
    }

    public static ProgressDialog showProgressDialog(Context context, String title, String message) {
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setTitle(title);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(true);
        m_Dialog.show();
        return m_Dialog;
    }


    public static ProgressDialog showProgressDialog(Context context, String title, String message, boolean cancelable) {
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setTitle(title);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(cancelable);
        m_Dialog.show();
        return m_Dialog;
    }


}
