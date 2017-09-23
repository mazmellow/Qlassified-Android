package com.q42.qlassified_example;

import android.app.Application;
import android.preference.PreferenceManager;

import com.q42.qlassified.EncryptedPreferrences;
import com.q42.qlassified.Logger;

/**
 * Created by maz on 9/23/2017 AD.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.setLogEnable(true);
        EncryptedPreferrences.init(this, PreferenceManager.getDefaultSharedPreferences(this));
    }
}
