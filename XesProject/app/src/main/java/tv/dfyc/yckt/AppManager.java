package tv.dfyc.yckt;

import android.app.ActivityManager;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.Stack;

public class AppManager {
    static final String TAG = AppManager.class.getName();

    private static Stack<Activity> activityStack = new Stack<>();;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
        Log.d(TAG, "addActivity: " + activity.getClass().getName());
        Log.d(TAG, activityStack.toString());
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (!activity.isFinishing())
                activity.finish();
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null)
            activityStack.remove(activity);
        Log.d(TAG, "removerActivity: " + activity.getClass().getName());
        Log.d(TAG, activityStack.toString());
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

/**
 * 查找指定类名的Activity
 */
public Activity getActivity(Class<?> cls) {
    for (Activity activity : activityStack) {
        if (activity.getClass().equals(cls)) {
            return activity;
        }
    }
    return null;
}

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}  
