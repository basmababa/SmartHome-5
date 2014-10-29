package com.asep.embedded;

import android.os.Looper;
import com.asep.embedded.BuildConfig;

public class ThreadPreconditions {
	
	public static void checkOnMainThread() {
        if (BuildConfig.DEBUG) {
            if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
                throw new IllegalStateException("This method should be called from the Main Thread");
            }
        }
    }


}
