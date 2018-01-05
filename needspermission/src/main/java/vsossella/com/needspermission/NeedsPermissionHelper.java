package vsossella.com.needspermission;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vsossella on 05/01/18.
 */

public class NeedsPermissionHelper {

    public static final String KEY_BUNDLE_PERMISSIONS = "PERMISSIONS";

    public static void handlePermissionsNeeds(Activity activity) {
        Class<?> classWithPermissionNeeds = activity.getClass();

        if (classWithPermissionNeeds.isAnnotationPresent(NeedsPermission.class)) {
            NeedsPermission needsPermission = classWithPermissionNeeds.getAnnotation(NeedsPermission.class);

            String[] permissions = needsPermission.permissions();
            Class<?> activityClass = needsPermission.activityExplanation();

            if (permissions != null && permissions.length > 0) {
                String[] permissionsNotGrantedYet = PermissionUtils.filterPermissionsNotGranted(activity, permissions);
                if (permissionsNotGrantedYet.length > 0) {
                    if (activityClass != Nullable.class) {
                        Intent i = new Intent(activity, needsPermission.activityExplanation());
                        activity.startActivityForResult(i, PermissionUtils.REQUEST_HARDWARE_PERMISSION_CODE);
                    } else {
                        PermissionUtils.requestPermissions(activity, permissionsNotGrantedYet);
                    }
                }
//                else {
//                    activity.onNeedsPermissionResult(Arrays.asList(permissions));
//                }
            }
        }
    }


    public static void handlePermissionsNeeds(Fragment fragment) {
        Class<?> classWithPermissionNeeds = fragment.getClass();

        if (classWithPermissionNeeds.isAnnotationPresent(NeedsPermission.class)) {
            NeedsPermission needsPermission = classWithPermissionNeeds.getAnnotation(NeedsPermission.class);

            String[] permissions = needsPermission.permissions();
            Class<?> activity = needsPermission.activityExplanation();

            if (permissions != null && permissions.length > 0) {
                String[] permissionsNotGrantedYet = PermissionUtils.filterPermissionsNotGranted(fragment.getActivity(), permissions);
                if (permissionsNotGrantedYet.length > 0) {
                    if (activity != Nullable.class) {
                        Intent intent = new Intent(fragment.getActivity(), needsPermission.activityExplanation());
                        Bundle bundle = new Bundle();
                        bundle.putStringArray(KEY_BUNDLE_PERMISSIONS, permissions);
                        intent.putExtras(bundle);
                        fragment.startActivityForResult(intent, PermissionUtils.REQUEST_HARDWARE_PERMISSION_CODE);
                    } else {
                        PermissionUtils.requestPermissions(fragment, permissionsNotGrantedYet);
                    }
                }
//                else {
//                    fragment.onNeedsPermissionResult(Arrays.asList(permissions));
//                }
            }
        }
    }

    public static boolean allPermissionWereGranted(Fragment fragment) {
        Class<?> classWithPermissionNeeds = fragment.getClass();

        if (classWithPermissionNeeds.isAnnotationPresent(NeedsPermission.class)) {
            NeedsPermission needsPermission = classWithPermissionNeeds.getAnnotation(NeedsPermission.class);
            return PermissionUtils.filterPermissionsNotGranted(fragment.getActivity(), needsPermission.permissions()).length == 0;
        }
        return true;
    }

    public static String[] getPermissionNotGranted(Fragment fragment) {
        Class<?> classWithPermissionNeeds = fragment.getClass();

        if (classWithPermissionNeeds.isAnnotationPresent(NeedsPermission.class)) {
            NeedsPermission needsPermission = classWithPermissionNeeds.getAnnotation(NeedsPermission.class);
            return PermissionUtils.filterPermissionsNotGranted(fragment.getActivity(), needsPermission.permissions());
        }
        return null;
    }

    public static void handlePermissionsResult(Fragment fragment, String[] permissions, int[] grantResults) {
        try {
            HashMap<String, Boolean> permissionsHash = createPermissionsHasMap(permissions, grantResults);
            List<String> permissionsGranted = filterPermissions(permissionsHash, true);
//            if (!permissionsGranted.isEmpty()) {
//                fragment.onNeedsPermissionResult(permissionsGranted);
//            }

            List<String> permissionsNotGranted = filterPermissions(permissionsHash, false);
//            if (!permissionsNotGranted.isEmpty()) {
//                fragment.onNeedsPermissionResultNotGranted(permissionsNotGranted);
//            }
        } catch (Exception e) {
//            fragment.onHandlePermissionsResultError(e);
        }
    }

    private static HashMap<String, Boolean> createPermissionsHasMap(String[] permissions, int[] grantResults) {
        HashMap<String, Boolean> permissionsResult = new HashMap<>();

        for (int i = 0; i < permissions.length; i++) {
            permissionsResult.put(permissions[i], grantResults[i] > -1);
        }

        return permissionsResult;
    }

    private static List<String> filterPermissions(HashMap<String, Boolean> permissions, boolean granted) {
        List<String> permissionsResultFiltered = new ArrayList<>();

        for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
            if (entry.getValue() == granted) {
                permissionsResultFiltered.add(entry.getKey());
            }
        }

        return permissionsResultFiltered;
    }
}

