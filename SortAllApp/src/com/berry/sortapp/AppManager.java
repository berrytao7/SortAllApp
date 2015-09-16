package com.berry.sortapp;

import java.util.ArrayList;
import java.util.List;

import com.berry.sortapp.bean.AppInfo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

/**
 * App管理者,用来获取设备上的所有app
 * 
 * @author Administrator
 * 
 */
public class AppManager {
    
  //它是通过解析< Intent-filter>标签得到有
  //< action android:name=”android.intent.action.MAIN”/>　　
  //< action android:name=”android.intent.category.LAUNCHER”/>
 //这样的app,所以得到的要比第二种方法少(第二种方法结果包含那种service、previder等app)
	public static List<AppInfo> getAppList(Context context) {
		
		List<AppInfo> appList = new ArrayList<AppInfo>();
		Intent intent =  new Intent(Intent.ACTION_MAIN, null); 
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> mApps =context.getPackageManager().queryIntentActivities(intent, 0); 
		ResolveInfo info = null;
		for (int i = 0; i < mApps.size(); i++) {
		  info = mApps.get(i);
		  String appLabel = info.loadLabel(context.getPackageManager()).toString();  
	      String packagename = info.activityInfo.packageName;  
	      String appname = info.activityInfo.name;  
	      Drawable appIcon = info.activityInfo.loadIcon(context.getPackageManager());
	      boolean isUserApp = checkAppType(context,packagename)==1?true:false;
	      ComponentName cn = new ComponentName(packagename, appname);
          AppInfo appInfo = new AppInfo(appLabel, appIcon,cn,isUserApp);
          appList.add(appInfo);
		}
		return appList;
	}
	
	//通过解析AndroidManifest.xml的< application>标签中得到的，所以它能得到所有的app;未使用的原因是获取到的className,packageName有可能为空
	public static List<AppInfo> getAppList2(Context context) {
      List<PackageInfo> installedPackages = context.getPackageManager()
              .getInstalledPackages(0);
      
      List<AppInfo> appList = new ArrayList<AppInfo>();
      for (PackageInfo packageInfo : installedPackages) {
          ApplicationInfo applicationInfo = packageInfo.applicationInfo;
          if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
              // 系统app
          } else {
              String appName = applicationInfo.loadLabel(context.getPackageManager()).toString();
              String className = applicationInfo.className;
              String pkgName = applicationInfo.packageName;
              Drawable appIcon = applicationInfo.loadIcon(context.getPackageManager());
              ComponentName cn = new ComponentName(pkgName, className);
              boolean isUserApp = checkAppType(context,pkgName)==1?true:false;
              AppInfo appInfo = new AppInfo(appName, appIcon,cn,isUserApp);
              appList.add(appInfo);
          }
          
      }
      return appList;
  }
	
	//未知软件类型  
    public static final int UNKNOW_APP = 0;  
    //用户软件类型  
    public static final int USER_APP = 1;  
    //系统软件  
    public static final int SYSTEM_APP = 2;  
    //系统升级软件  
    public static final int SYSTEM_UPDATE_APP = 4;  
    //系统+升级软件  
    public static final int SYSTEM_REF_APP = SYSTEM_APP | SYSTEM_UPDATE_APP;  
	/** 
     * 检查app是否是系统rom集成的 
     * @param pname 
     * @return 
     */  
    private static int checkAppType(Context context,String pname) {  
        try {  
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(pname, 0);  
            // 是系统软件或者是系统软件更新  
            if (isSystemApp(pInfo) || isSystemUpdateApp(pInfo)) {  
                return SYSTEM_REF_APP;  
            } else {  
                return USER_APP;  
            }  
  
        } catch (NameNotFoundException e) {  
          return UNKNOW_APP; 
        }  
    }  
      
    /** 
     * 是否是系统软件或者是系统软件的更新软件 
     * @return 
     */  
    public static boolean isSystemApp(PackageInfo pInfo) {  
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);  
    }  
  
    public static boolean isSystemUpdateApp(PackageInfo pInfo) {  
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);  
    }  
  
    public boolean isUserApp(PackageInfo pInfo) {  
        return (!isSystemApp(pInfo) && !isSystemUpdateApp(pInfo));  
    }  
}
