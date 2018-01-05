package vsossella.com.needspermission;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vsossella on 05/01/18.
 */

public class PermissionUtils {
    private static final int SMS_REQUEST_CODE = 100;
    public static final int LOCATION_REQUEST_CODE = 101;
    private static final int GET_ACCOUNTS_REQUEST_CODE = 102;
    public static final int READ_CONTACTS_REQUEST_CODE = 103;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    private static final int USE_FINGERPRINT_REQUEST_CODE = 105;
    public static final int CAMERA_REQUEST_CODE = 106;
    public static final int REQUEST_HARDWARE_PERMISSION_CODE = 1007;
    public static final int REQUEST_CODE_SETTINGS = 1008;

    public static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(Fragment fragment, String[] permissions) {
        if (permissions != null && permissions.length > 0) {
            FragmentCompat.requestPermissions(fragment,
                    permissions,
                    REQUEST_HARDWARE_PERMISSION_CODE);
        }

    }

    public static void requestPermissions(Activity activity, String[] permissions) {
        if (permissions != null && permissions.length > 0) {
            ActivityCompat.requestPermissions(activity,
                    permissions,
                    REQUEST_HARDWARE_PERMISSION_CODE);
        }
    }

    public static boolean thereIsPermissionCheckedAsNeverAskAgain(Fragment fragment, String[] permissions) {
        String[] permissionsNotGrantedYet = filterPermissionsNotGranted(fragment.getActivity(), permissions);
        if (permissionsNotGrantedYet.length > 0) {

            for (String permission :
                    permissionsNotGrantedYet) {
                if (!FragmentCompat.shouldShowRequestPermissionRationale(fragment, permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void openPermissionSettings(Activity activity) {
        Intent intent = getIntentSettings(activity);
        activity.startActivityForResult(intent, REQUEST_CODE_SETTINGS);
    }

    public static void openPermissionSettings(Fragment fragment) {
        Intent intent = getIntentSettings(fragment.getActivity());
        fragment.startActivityForResult(intent, REQUEST_CODE_SETTINGS);
    }

    @NonNull
    private static Intent getIntentSettings(Activity activity) {
        return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.getPackageName(), null));
    }

    public static String[] filterPermissionsNotGranted(Activity activity, String[] permissions) {
        List<String> permissionsNotGrantedYet = new ArrayList<>();

        for (String permission : permissions) {
            if (!isPermissionGranted(activity, permission)) {
                permissionsNotGrantedYet.add(permission);
            }
        }

        return permissionsNotGrantedYet.toArray(new String[permissionsNotGrantedYet.size()]);
    }

    public static void requestLocationPermissions(Activity activity) {
        if (!isPermissionGranted(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
    }

    public static void requestOpenAccountPermissions(Activity activity) {
        if (!isPermissionGranted(activity, Manifest.permission.RECEIVE_SMS)
                || !isPermissionGranted(activity, Manifest.permission.CAMERA)
                || !isPermissionGranted(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                || !isPermissionGranted(activity, Manifest.permission.READ_CONTACTS)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_CONTACTS},
                    SMS_REQUEST_CODE);
        }
    }

    public static void requestCameraPermissions(Activity activity) {
        if (!isCameraPermissionGranted(activity)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
        }
    }

    public static boolean isCameraPermissionGranted(Activity activity) {
        return isPermissionGranted(activity, Manifest.permission.CAMERA)
                && isPermissionGranted(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void requestSMSPermission(Activity activity) {
        if (!isPermissionGranted(activity, Manifest.permission.RECEIVE_SMS)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS},
                    SMS_REQUEST_CODE);
        }
    }

    public static void requestAccountsPermission(Activity activity) {
        if (hasAccountsPermission(activity)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.GET_ACCOUNTS}, GET_ACCOUNTS_REQUEST_CODE);
        }
    }

    public static void requestWritePermission(Activity activity) {
        if (!isPermissionGranted(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    public static void requestReadContactsPermission(Activity activity) {
        if (!isPermissionGranted(activity, Manifest.permission.READ_CONTACTS)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_REQUEST_CODE);
        }
    }

    public static void requestFingerPermission(Activity activity) {
        if (!isPermissionGranted(activity, Manifest.permission.USE_FINGERPRINT)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.USE_FINGERPRINT}, USE_FINGERPRINT_REQUEST_CODE);
        }
    }

    private static boolean hasAccountsPermission(Activity activity) {
        return isPermissionGranted(activity, Manifest.permission.GET_ACCOUNTS);
    }

    public static boolean hasPermissionGranted(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}