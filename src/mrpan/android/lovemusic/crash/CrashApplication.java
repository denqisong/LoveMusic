package mrpan.android.lovemusic.crash;

import com.baidu.frontia.FrontiaApplication;

import android.app.Application;

public class CrashApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		FrontiaApplication.initFrontiaApplication(getApplicationContext());
	}
}
