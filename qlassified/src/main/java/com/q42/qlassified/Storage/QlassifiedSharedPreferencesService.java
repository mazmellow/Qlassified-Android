package com.q42.qlassified.Storage;

import android.content.SharedPreferences;

import com.q42.qlassified.Entry.EncryptedEntry;
import com.q42.qlassified.Logger;

public class QlassifiedSharedPreferencesService extends QlassifiedStorageService {

    private final SharedPreferences preferences;

    public QlassifiedSharedPreferencesService(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public void onSaveRequest(EncryptedEntry encryptedEntry) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(encryptedEntry.getKey(), encryptedEntry.getEncryptedValue());
        editor.apply();
        Logger.d("Storage", String.format("Saved key: %s", encryptedEntry.getKey()));
        Logger.d("Storage", String.format("Saved encrypted value: %s", encryptedEntry.getEncryptedValue()));
    }

    @Override
    public EncryptedEntry onGetRequest(String key) {
        Logger.d("Storage", String.format("Get by key: %s", key));
        final String encryptedValue = this.preferences.getString(key, null);
        Logger.d("Storage", String.format("Got encrypted value: %s", encryptedValue));
        return new EncryptedEntry(key, encryptedValue);
    }
}
