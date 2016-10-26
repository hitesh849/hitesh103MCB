package com.app.mcb.Utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mcb.R;
import com.squareup.picasso.Picasso;

import org.byteclues.lib.init.Env;
import org.byteclues.lib.view.AbstractFragment;
import org.byteclues.lib.view.AbstractFragmentActivity;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by u on 9/15/2016.
 */
public class Util {
    public static final String VAL_OK = "OK";
    private static ProgressDialog progressDialog = null;
    private static AlertDialog alertDialog = null;
    private static Snackbar retrySnackbar = null;
    public static String FLD_STATUS = "status";
    public static final String STATUS_CODE_USER_LOGOUT = "UserLogout";
    public static final String ACTION_USER_LOGOUT = "userLogout";
    public static final String KEY_USER_TOKEN = "user_token";
    public static final String KEY_LOGOUT_MESSAGE = "logout_message";


    public static void showProDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }
    }

    public static void dimissProDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void showCenteredToast(Context ctx, String msg) {
        Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static boolean isDeviceOnline() {
        boolean isDeviceOnLine = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) Env.appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            isDeviceOnLine = netInfo != null && netInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDeviceOnLine;
    }

    public static boolean checkValueForKey(JSONObject jsonObject, String key) {
        boolean b = false;
        if (jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key)) {
            b = true;
        }
        return b;
    }

    public static AlertDialog showAlertDialog(DialogInterface.OnClickListener okClicklistener, String msg) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Env.currentActivity);
        DialogInterface.OnClickListener clickListener = okClicklistener != null ? okClicklistener : new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                alertDialog = null;
            }
        };
        builder.setPositiveButton("Ok", clickListener);
        builder.setMessage(msg);
        alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void writeDBfileOnSdcard(Context context, String databaseFileName) {
        try {
            File f = new File(context.getDatabasePath(databaseFileName).getAbsolutePath());
            FileInputStream fis = null;
            FileOutputStream fos = null;

            try {
                fis = new FileInputStream(f);
                fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + databaseFileName + ".sqlite");
                while (true) {
                    int i = fis.read();
                    if (i != -1) {
                        fos.write(i);
                    } else {
                        break;
                    }
                }
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Context context, ImageView imgView, String url, int defaultResource) {
        try {
            Picasso.with(context).load(url).fit().centerCrop().error(defaultResource).fit().placeholder(defaultResource).fit().into(imgView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) Env.currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void replaceFragment(Context context, int container, AbstractFragment abstractFragment) {
        ((AbstractFragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(container, abstractFragment).commit();
    }

    public static void addFragment(Context context, int container, AbstractFragment abstractFragment) {
        ((AbstractFragmentActivity) context).getSupportFragmentManager().beginTransaction().add(container, abstractFragment).commit();
    }

    public static void showOKSnakBar(android.view.View view, String msg) {
        try {
            if (view != null && msg != null) {
                retrySnackbar = Snackbar
                        .make(view, msg, Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                retrySnackbar.dismiss();
                            }
                        });
                ViewGroup group = (ViewGroup) retrySnackbar.getView();
                group.setBackgroundColor(Env.appContext.getResources().getColor(R.color.trans_blue));
                retrySnackbar.setActionTextColor(Env.appContext.getResources().getColor(R.color.primary_blue));
                View sbView = retrySnackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);

                textView.setTextColor(Env.appContext.getResources().getColor(R.color.primary_blue));
                retrySnackbar.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void showSnakBar(android.view.View view, String msg) {
        try {
            if (view != null && msg != null) {
                retrySnackbar = Snackbar
                        .make(view, msg, Snackbar.LENGTH_SHORT);
                ViewGroup group = (ViewGroup) retrySnackbar.getView();
                group.setBackgroundColor(Env.appContext.getResources().getColor(R.color.trans_blue));
                View sbView = retrySnackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);

                textView.setTextColor(Env.appContext.getResources().getColor(R.color.primary_blue));
                retrySnackbar.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static String getCurrentDate() {
        DateTime now = new DateTime();
        LocalDate today = now.toLocalDate();
        return today.toString().replaceAll("-", "/");
    }

    public static String getDateFromDateTimeFormat(String dateTime) {

        try {

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime obj = formatter.parseDateTime(dateTime).toDateTime();
            DateTimeFormatter localDateFormat = DateTimeFormat.forPattern("dd MMM yyyy");
            obj.toString(localDateFormat);
            return obj.toString(localDateFormat);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getDateDDMMYY(String YYMMDD) {

        try {

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime obj = formatter.parseDateTime(YYMMDD).toDateTime();
            DateTimeFormatter localDateFormat = DateTimeFormat.forPattern("dd/MM/yyyy");
            obj.toString(localDateFormat);
            return obj.toString(localDateFormat);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getTimeFromDateTimeFormat(String dateTime) {

        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime obj = formatter.parseDateTime(dateTime).toDateTime();
            DateTimeFormatter localDateFormat = DateTimeFormat.forPattern("HH:mm a");
            obj.toString(localDateFormat);
            return obj.toString(localDateFormat);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getFirstName(String str) {
        String code[] = str.split(" ");
        return code[0];
    }

    public static String getDDMMYYYYFormat(String dateTime, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date dateObj = sdf.parse(dateTime);
            return new SimpleDateFormat("dd MMM yyyy").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getParcelType(String type) {
        if ("E".equals(type))
            return Constants.ENVELOPE;
        else if ("B".equals(type))
            return Constants.BOX;
        else if ("P".equals(type))
            return Constants.PACKET;
        return null;
    }

    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();

        return output;
    }

    public static Dialog showAttachmentDialog(View.OnClickListener clickListener, Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.attatchment_dialog);
        TextView txtCameraAttachment = (TextView) dialog.findViewById(R.id.txtCameraAttachment);
        TextView txtAttachmentsDialog = (TextView) dialog.findViewById(R.id.txtAttachmentsDialog);
        TextView txtCancelAttachments = (TextView) dialog.findViewById(R.id.txtCancelAttachments);
        txtCameraAttachment.setOnClickListener(clickListener);
        txtAttachmentsDialog.setOnClickListener(clickListener);
        txtCancelAttachments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        return dialog;
    }

    public static Intent captureImage() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String path = Environment.getExternalStorageDirectory() + File.separator + Constants.APP_FOLDER_NAME + File.separator + Constants.ATTACHMENTS_FOLDER_NAME;
            File mediapath = new File(path);
            if (!mediapath.exists()) {
                mediapath.mkdirs();
            }
            Uri captured_image_uri = Uri.fromFile(new File(mediapath.getPath(), "Image" + System.currentTimeMillis() + ".jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, captured_image_uri);
            return intent;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getPath(Context context, Uri uri) {
        String filePath = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String uri_authority = uri.getAuthority();
                String selection = "_id=?";
                if (uri_authority.equals("com.android.providers.media.documents")) {
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                } else if (uri_authority.equals("media")) {
                    String docId = uri.getLastPathSegment();
                    String[] proj = {MediaStore.Images.Media.DATA};
                    String[] selectionArgs = new String[]{docId};
                    return getDataColumn(context, uri, selection, selectionArgs);
                } else if (uri_authority.equals("com.android.providers.downloads.documents")) {
                    String id = DocumentsContract.getDocumentId(uri);
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                } else if (uri_authority.equals("com.google.android.apps.docs.storage")) {
                    Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
                    returnCursor.moveToFirst();
                    String file_name = returnCursor.getString(returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                    FileInputStream in = (FileInputStream) context.getContentResolver().openInputStream(uri);
                    String path = Environment.getExternalStorageDirectory() + File.separator + Constants.APP_FOLDER_NAME + File.separator + Constants.ATTACHMENTS_FOLDER_NAME;
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(new File(path, file_name));
                    FileChannel inChannel = in.getChannel();
                    FileChannel outChannel = out.getChannel();
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                    in.close();
                    out.close();
                    return path + File.separator + file_name;

                } else if ("content".equals(uri.getScheme())) {

                    Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
                    returnCursor.moveToFirst();
                    String file_name = returnCursor.getString(returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    long file_size = returnCursor.getLong(returnCursor.getColumnIndex(OpenableColumns.SIZE));

                    FileInputStream in = (FileInputStream) context.getContentResolver().openInputStream(uri);
                    String path = Environment.getExternalStorageDirectory() + File.separator + Constants.APP_FOLDER_NAME + File.separator + Constants.ATTACHMENTS_FOLDER_NAME;
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(new File(path, file_name));
                    FileChannel inChannel = in.getChannel();
                    FileChannel outChannel = out.getChannel();
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                    in.close();
                    out.close();
                    return path + File.separator + file_name;

                } else {
                    return uri.getPath();
                }


            } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                String[] proj = {MediaStore.Images.Media.DATA};
                String result = null;
                CursorLoader cursorLoader = new CursorLoader(context, uri, proj, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();

                if (cursor != null) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    result = cursor.getString(column_index);
                }
                return result;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static Bitmap loadBitmap(String url) {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }

    public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

    public static Bitmap getDecode64ImageStringFromBitmap(String strBase64) {
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }


    public static String getTripStatus(int status, Context context) {
        String statusStr = "-";
        switch (status) {
            case 0:
                statusStr = context.getResources().getString(R.string.pending);
                break;
            case 1:
                statusStr = context.getResources().getString(R.string.approved);
                break;
            case 2:
                statusStr = context.getResources().getString(R.string.booking_request_sent);
                break;
            case 3:
                statusStr = context.getResources().getString(R.string.booked);
                break;
            case 5:
                statusStr = context.getResources().getString(R.string.rejected);
                break;
            case 6:
                statusStr = context.getResources().getString(R.string.delivered);
                break;
            case 7:
                statusStr = context.getResources().getString(R.string.complete);
                break;
            case 9:
                statusStr = context.getResources().getString(R.string.on_hold);
                break;
        }
        return statusStr;
    }

    public static String getParcelStatus(int status, Context context) {
        String statusStr = "-";
        switch (status) {
            case 0:
                statusStr = context.getResources().getString(R.string.parcel_idcreated);
                break;

            case 1:
                statusStr = context.getResources().getString(R.string.created_payment_due);
                break;

            case 2:
                statusStr = context.getResources().getString(R.string.booking_with_tr);
                break;

            case 3:
                statusStr = context.getResources().getString(R.string.parcel_collected);
                break;
            case 4:
                statusStr = context.getResources().getString(R.string.parcel_delivered);
                break;
            case 5:
                statusStr = context.getResources().getString(R.string.delivery_completed);
                break;
            case 6:
                statusStr = context.getResources().getString(R.string.cancelled);
                break;

            case 9:
                statusStr = context.getResources().getString(R.string.rejected_by_tr);
                break;
        }
        return statusStr;
    }

}
