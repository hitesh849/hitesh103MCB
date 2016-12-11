package com.app.mcb.viewControllers.dashboardFragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.model.UserProfileModel;
import com.app.mcb.retrointerface.TryAgainInterface;
import com.app.mcb.sharedPreferences.Config;
import com.app.mcb.viewControllers.LoginActivity;
import com.hbb20.CountryCodePicker;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

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
 * Created by u on 9/15/2016.
 */
public class MyProfileFragment extends AbstractFragment implements View.OnClickListener, FullCallback {

    private EditText etNameMyProfile;
    private EditText etMemberIdMyProfile;
    private EditText etEmailIdMyProfile;
    private EditText etMobileNumberMyProfile;
    private EditText etLandLineMyProfile;
    private EditText etCardNumberMyProfile;
    private EditText etAddressMyProfile;
    private EditText etxtPinCode;
    private LinearLayout llEditProfile;
    private LinearLayout llMyProfileMain;
    private TextView txtEditProfile;
    private CircularImageView imgEditUserProfile;
    private CountryCodePicker ccpMobile;
    private Dialog attachmentDialog;
    private Uri captured_image_uri;
    private UserInfoData userInfoData;
    private UserProfileModel userProfileModel = new UserProfileModel();

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_profile, container, false);
        init(view);
        setValues();

        return view;
    }


    private void init(View view) {
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.my_profile));
        llMyProfileMain = (LinearLayout) view.findViewById(R.id.llMyProfileMain);
        txtEditProfile = (TextView) view.findViewById(R.id.txtEditProfile);
        etNameMyProfile = (EditText) view.findViewById(R.id.etNameMyProfile);
        etMemberIdMyProfile = (EditText) view.findViewById(R.id.etMemberIdMyProfile);
        etEmailIdMyProfile = (EditText) view.findViewById(R.id.etEmailIdMyProfile);
        etMobileNumberMyProfile = (EditText) view.findViewById(R.id.etMobileNumberMyProfile);
        etLandLineMyProfile = (EditText) view.findViewById(R.id.etLandLineMyProfile);
        etCardNumberMyProfile = (EditText) view.findViewById(R.id.etCardNumberMyProfile);
        etAddressMyProfile = (EditText) view.findViewById(R.id.etAddressMyProfile);
        etxtPinCode = (EditText) view.findViewById(R.id.etxtPinCode);
        imgEditUserProfile = (CircularImageView) view.findViewById(R.id.imgEditUserProfile);
        ccpMobile = (CountryCodePicker) view.findViewById(R.id.ccpMobile);
        txtEditProfile.setOnClickListener(this);
        imgEditUserProfile.setOnClickListener(this);
    }

    private void setValues() {
        etNameMyProfile.setText(Config.getUserFirstName() + " " + Config.getUserLastName());
        etMemberIdMyProfile.setText(Constants.BEGIN_WITH_USER_ID+Config.getUserId());
        etEmailIdMyProfile.setText(Config.getUserName());
        etMobileNumberMyProfile.setText(Config.getUserMobile());
        etLandLineMyProfile.setText(Config.getUserPhone());
        etCardNumberMyProfile.setText(Config.getUserPassportNumber());
        etAddressMyProfile.setText(Config.getUserAddress());
        etxtPinCode.setText(Config.getUserPinCode());
        if(!TextUtils.isEmpty(Config.getUserCountryCode()))
        ccpMobile.setCountryForPhoneCode(Integer.parseInt(Config.getUserCountryCode()));
        if (Config.getUserImageURl() != null && !TextUtils.isEmpty(Config.getUserImageURl()))
            imgEditUserProfile.setImageBitmap(Util.getDecode64ImageStringFromBitmap(Config.getUserImageURl()));
        etNameMyProfile.setFocusable(false);
        etMemberIdMyProfile.setFocusable(false);
        etMobileNumberMyProfile.setFocusable(false);
        etLandLineMyProfile.setFocusable(false);
        etCardNumberMyProfile.setFocusable(false);
        etAddressMyProfile.setFocusable(false);
        etEmailIdMyProfile.setFocusable(false);
        imgEditUserProfile.setClickable(false);
        etxtPinCode.setFocusable(false);
    }

    private void setFocus() {
        etNameMyProfile.setFocusableInTouchMode(true);
        etNameMyProfile.setFocusable(true);
        etMobileNumberMyProfile.setFocusableInTouchMode(true);
        etMobileNumberMyProfile.setFocusable(true);
        etLandLineMyProfile.setFocusableInTouchMode(true);
        etLandLineMyProfile.setFocusable(true);
        etCardNumberMyProfile.setFocusableInTouchMode(true);
        etCardNumberMyProfile.setFocusable(true);
        etAddressMyProfile.setFocusableInTouchMode(true);
        etAddressMyProfile.setFocusable(true);
        imgEditUserProfile.setClickable(true);
        etxtPinCode.setFocusableInTouchMode(true);
        etxtPinCode.setFocusable(true);

    }

    private UserInfoData getValue() {
        UserInfoData userInfoData = new UserInfoData();
        userInfoData.name = etNameMyProfile.getText().toString();
        userInfoData.id = etMemberIdMyProfile.getText().toString();
        userInfoData.email = etEmailIdMyProfile.getText().toString();
        userInfoData.mobile = etMobileNumberMyProfile.getText().toString();
        userInfoData.phone = etLandLineMyProfile.getText().toString();
        userInfoData.passportno = etCardNumberMyProfile.getText().toString();
        userInfoData.address = etAddressMyProfile.getText().toString();
        userInfoData.pinCode = etxtPinCode.getText().toString();
        userInfoData.country_code =ccpMobile.getSelectedCountryCode();
        Bitmap bitmap = ((BitmapDrawable) imgEditUserProfile.getDrawable()).getBitmap();
        userInfoData.photo = Util.getEncoded64ImageStringFromBitmap(bitmap);
        return userInfoData;
    }

    private boolean validation(UserInfoData userInfoData) {

        if (TextUtils.isEmpty(userInfoData.name)) {
            etNameMyProfile.setError(getResources().getString(R.string.can_not_be_empty));
            etNameMyProfile.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.mobile)) {
            etMobileNumberMyProfile.setError(getResources().getString(R.string.can_not_be_empty));
            etMobileNumberMyProfile.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.phone)) {
            etLandLineMyProfile.setError(getResources().getString(R.string.can_not_be_empty));
            etLandLineMyProfile.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.passportno)) {
            etCardNumberMyProfile.setError(getResources().getString(R.string.can_not_be_empty));
            etCardNumberMyProfile.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.address)) {
            etAddressMyProfile.setError(getResources().getString(R.string.can_not_be_empty));
            etAddressMyProfile.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.pinCode)) {
            etxtPinCode.setError(getResources().getString(R.string.please_enter_pin_code));
            etxtPinCode.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected BasicModel getModel() {
        return userProfileModel;
    }

    @Override
    public void update(Observable o, Object data) {
        try {
            Util.dimissProDialog();
            if (data != null && data instanceof UserInfoData) {
                UserInfoData userInfoData = (UserInfoData) data;
                if (userInfoData.status.equals("success")) {
                    if (userInfoData.response != null) {
                        userInfoData = userInfoData.response.get(0);
                        LoginActivity.saveUserData(userInfoData);
                        Util.replaceFragment(getActivity(), R.id.fmHomeContainer, new MyProfileFragment());
                    }
                } else if (userInfoData.status.equals("Error")) {
                    Util.showOKSnakBar(llMyProfileMain, userInfoData.errorMessage);
                    llMyProfileMain.addView(Util.getViewDataNotFound(getActivity(), llMyProfileMain, userInfoData.errorMessage));
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llMyProfileMain, getResources().getString(R.string.pls_try_again));
                llMyProfileMain.addView(Util.getViewServerNotResponding(getActivity(), llMyProfileMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        updateProfile(userInfoData);
                    }
                }));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.txtEditProfile) {
            if (getResources().getString(R.string.edit).equals(txtEditProfile.getText())) {
                txtEditProfile.setText(getResources().getString(R.string.update));
                setFocus();
            } else if (getResources().getString(R.string.update).equals(txtEditProfile.getText())) {
                txtEditProfile.setText(getResources().getString(R.string.edit));
                 userInfoData = getValue();
                if (validation(userInfoData)) {
                    updateProfile(userInfoData);
                }
            }
        } else if (id == R.id.txtCameraAttachment) {
            attachmentDialog.dismiss();

            captureImage();
        } else if (id == R.id.txtAttachmentsDialog) {
            attachmentDialog.dismiss();
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, Constants.PICK_Existing_Image_REQCODE);
        } else if (id == R.id.imgEditUserProfile) {
            if (Util.isCallPermissionGranted(getActivity())) {
                attachmentDialog = Util.showAttachmentDialog(this, getActivity());
            } else {
                Util.permission(getActivity(),this);
            }
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

    private void updateProfile(final UserInfoData userInfoData) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                userProfileModel.updateProfile(userInfoData);
            } else {
                llMyProfileMain.addView(Util.getViewInternetNotFound(getActivity(), llMyProfileMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        updateProfile(userInfoData);
                    }
                }));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.PICK_FILE_FROM_CAMERA && resultCode == getActivity().RESULT_OK) {
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
                        imgEditUserProfile.setImageBitmap(bmp);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == Constants.PICK_Existing_Image_REQCODE && resultCode == getActivity().RESULT_OK && null != data) {
                Uri pickedImage = data.getData();
                String imagePath = Util.getPath(getActivity(), pickedImage);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), pickedImage);
                imgEditUserProfile.setImageBitmap(bitmap);
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

        ContextThemeWrapper cw = new ContextThemeWrapper(getActivity(), R.style.AlertDialogTheme);
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
