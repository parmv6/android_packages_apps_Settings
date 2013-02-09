/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.cyanogenmod;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class PowerMenu extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static final String PM_SCREENSHOT_SOUND = "pm_screenshot_sound";
    private static final String PM_SCREENSHOT_DELAY = "pm_screenshot_delay";

    private CheckBoxPreference mScreenshotSound;
    private ListPreference mScreenshotDelay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.power_menu);

        PreferenceScreen prefSet = getPreferenceScreen();

        mScreenshotSound = (CheckBoxPreference) prefSet.findPreference(PM_SCREENSHOT_SOUND);
        mScreenshotDelay = (ListPreference) prefSet.findPreference(PM_SCREENSHOT_DELAY);

        mScreenshotSound.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.SCREENSHOT_SOUND, 1) == 1));

        int screenshotDelay = Settings.System.getInt(getContentResolver(),
                Settings.System.SCREENSHOT_DELAY, 1000);
        mScreenshotDelay.setValue(String.valueOf(screenshotDelay));
        mScreenshotDelay.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mScreenshotSound) {
            value = mScreenshotSound.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.SCREENSHOT_SOUND, value ? 1 : 0);
            return true;
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mScreenshotDelay) {
            int screenshotDelay = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.SCREENSHOT_DELAY, screenshotDelay);
            return true;
        }

        return false;
    }


}
