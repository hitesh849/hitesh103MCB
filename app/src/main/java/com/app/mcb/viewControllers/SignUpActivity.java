package com.app.mcb.viewControllers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.model.UserAuthenticationModel;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import rebus.permissionutils.FullCallback;
import rebus.permissionutils.PermissionEnum;
import retrofit.RetrofitError;

/**
 * Created by Hitesh on 9/16/2016.
 */
public class SignUpActivity extends AbstractFragmentActivity implements View.OnClickListener, FullCallback {

    private LinearLayout llSignUpMain;
    private TextView txtRagisterSignUp;
    private AppHeaderView appHeaderView;
    private EditText etextFirstName;
    private EditText etextLastName;
    private EditText etextEmail;
    private EditText etextPassword;
    private EditText etextConfirmPassword;
    private CircularImageView imgEditProfileRegister;
    private Uri captured_image_uri;
    private Dialog attachmentDialog;
    private UserInfoData userInfoData = new UserInfoData();
    private UserAuthenticationModel userAuthenticationModel = new UserAuthenticationModel();

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {

        setContentView(R.layout.sign_up);
        init();
    }

    private void init() {
        llSignUpMain = (LinearLayout) findViewById(R.id.llSignUpMain);
        txtRagisterSignUp = (TextView) findViewById(R.id.txtRagisterSignUp);
        appHeaderView = (AppHeaderView) findViewById(R.id.appHeaderView);
        etextFirstName = (EditText) findViewById(R.id.etextFirstName);
        etextLastName = (EditText) findViewById(R.id.etextLastName);
        etextEmail = (EditText) findViewById(R.id.etextEmail);
        etextPassword = (EditText) findViewById(R.id.etextPassword);
        imgEditProfileRegister = (CircularImageView) findViewById(R.id.imgEditProfileRegister);
        ImageView imgCameraRegister = (ImageView) findViewById(R.id.imgCameraRegister);
        etextConfirmPassword = (EditText) findViewById(R.id.etextConfirmPassword);
        appHeaderView.txtHeaderNamecenter.setText(getResources().getString(R.string.sign_up));
        txtRagisterSignUp.setOnClickListener(this);
        imgCameraRegister.setOnClickListener(this);
    }

    @Override
    protected BasicModel getModel() {
        return userAuthenticationModel;
    }


    private boolean validation() {
        userInfoData.firstName = etextFirstName.getText().toString();
        userInfoData.lastName = etextLastName.getText().toString();
        userInfoData.email = etextEmail.getText().toString();
        userInfoData.password = etextPassword.getText().toString();

        if (TextUtils.isEmpty(userInfoData.firstName.trim())) {
            etextFirstName.setError(getString(R.string.can_not_be_empty));
            etextFirstName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.lastName.trim())) {
            etextLastName.setError(getString(R.string.can_not_be_empty));
            etextLastName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.email.trim())) {
            etextEmail.setError(getString(R.string.can_not_be_empty));
            etextEmail.requestFocus();
            return false;
        }
        if (!Util.emailValidation(this, etextEmail, userInfoData.email.trim()))
        {
            return false;
        }

        if (TextUtils.isEmpty(userInfoData.password)) {
            etextPassword.setError(getString(R.string.can_not_be_empty));
            etextPassword.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etextConfirmPassword.getText().toString())) {
            etextConfirmPassword.setError(getString(R.string.can_not_be_empty));
            etextConfirmPassword.requestFocus();
            return false;
        }
        if (!userInfoData.password.equals(etextConfirmPassword.getText().toString())) {
            etextConfirmPassword.setError(getResources().getString(R.string.pass_do_not_match_error_msg));
            etextConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txtRagisterSignUp) {
            if (validation()) {
                registerUser();
            }
        } else if (id == R.id.llBackHeader) {
            onBackPressed();
        } else if (id == R.id.imgCameraRegister) {
            if (Util.isCallPermissionGranted(this)) {
                attachmentDialog = Util.showAttachmentDialog(this, this);
            } else {
                Util.permission(this,this);
            }
        } else if (id == R.id.txtCameraAttachment) {
            attachmentDialog.dismiss();
            captureImage();
        } else if (id == R.id.txtAttachmentsDialog) {
            attachmentDialog.dismiss();
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, Constants.PICK_Existing_Image_REQCODE);
        }
    }

    public void captureImage() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String path = Environment.getExternalStorageDirectory() + File.separator + Constants.APP_FOLDER_NAME + File.separator + Constants.ATTACHMENTS_FOLDER_NAME;
            File mediapath = new File(path);
            if (!mediapath.exists()) {
                mediapath.mkdirs();
            }
            captured_image_uri = Uri.fromFile(new File(mediapath.getPath(), "Image" + System.currentTimeMillis() + ".jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, captured_image_uri);
            startActivityForResult(intent, Constants.PICK_FILE_FROM_CAMERA);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerUser() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                userAuthenticationModel.registerUser(userInfoData);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object data) {
        try {
            Util.dimissProDialog();
            if (data != null && data instanceof UserInfoData) {
                UserInfoData userInfoData = (UserInfoData) data;
                if (userInfoData.status.equals("success")) {
                    if (userInfoData.response != null) {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                } else if (userInfoData.status.equals("Error")) {
                    Util.showOKSnakBar(llSignUpMain, userInfoData.errorMessage);
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llSignUpMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.PICK_FILE_FROM_CAMERA && resultCode == RESULT_OK) {
                try {

                    if (captured_image_uri != null) {
                        ExifInterface exifInterface = new ExifInterface(captured_image_uri.getPath());
                        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Matrix matrix = new Matrix();
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90: {
                                matrix.postRotate(90);
                                break;
                            }
                            case ExifInterface.ORIENTATION_ROTATE_180: {
                                matrix.postRotate(180);
                                break;
                            }
                            case ExifInterface.ORIENTATION_ROTATE_270: {
                                matrix.postRotate(270);
                                break;
                            }
                        }
                        FileInputStream fis = new FileInputStream(captured_image_uri.getPath());
                        Bitmap bmp = BitmapFactory.decodeStream(fis);
                        Bitmap rotated = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                        FileOutputStream fos = new FileOutputStream(captured_image_uri.getPath());
                        rotated.compress(Bitmap.CompressFormat.JPEG, 65, fos);
                        imgEditProfileRegister.setImageBitmap(bmp);
                        userInfoData.image = Util.getEncoded64ImageStringFromBitmap(bmp);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == Constants.PICK_Existing_Image_REQCODE && resultCode == RESULT_OK && null != data) {
                Uri pickedImage = data.getData();
                String imagePath = Util.getPath(this, pickedImage);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pickedImage);
                imgEditProfileRegister.setImageBitmap(bitmap);
                userInfoData.image = Util.getEncoded64ImageStringFromBitmap(bitmap);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void result(ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied, ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked) {
        List<String> msg = new ArrayList<>();
        for (PermissionEnum permissionEnum : permissionsGranted) {
            msg.add(permissionEnum.toString() + " [Granted]");
        }
        for (PermissionEnum permissionEnum : permissionsDenied) {
            msg.add(permissionEnum.toString() + " [Denied]");
        }
        for (PermissionEnum permissionEnum : permissionsDeniedForever) {
            msg.add(permissionEnum.toString() + " [DeniedForever]");
        }
        /*for (PermissionEnum permissionEnum : permissionsAsked) {
            msg.add(permissionEnum.toString() + " [Asked]");
        }*/
        String[] items = msg.toArray(new String[msg.size()]);

        ContextThemeWrapper cw = new ContextThemeWrapper(this, R.style.AlertDialogTheme);
        new AlertDialog.Builder(cw)
                .setTitle("Permission result")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

}
