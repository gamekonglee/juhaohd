package bocang.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * @author Jun
 * @version 1.0
 * @time 2016/11/10 15:37
 * @des 跳转工具类
 */
public class IntentUtil {

    public static void startActivity(Context context, Class clzz, boolean flag) {
        Intent intent = new Intent(context, clzz);
        ((Activity) context).startActivity(intent);
        if (flag) {
            ((Activity) context).finish();
        }
    }
    public static void startActivity(Activity activity, Class clzz, boolean flag) {
        Intent intent = new Intent(activity, clzz);
        (activity).startActivity(intent);
        if (flag) {
            (activity).finish();
        }
    }
    /**
     * 进行页面跳转
     *
     * @param clzz
     */
    public static void showIntent(Activity context, Class<?> clzz, String[] keys, String[] values, boolean flag) {
        Intent intent = new Intent(context, clzz);
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                if (!TextUtils.isEmpty(keys[i]) && !TextUtils.isEmpty(values[i])) {
                    intent.putExtra(keys[i], values[i]);
                }
            }
        }

        context.startActivity(intent);
        if (flag)
            context.finish();
    }

    public static void showIntent(Activity context, Class<?> clzz) {
        showIntent(context, clzz, null, null, false);
    }

}
