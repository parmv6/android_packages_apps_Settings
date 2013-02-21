/*
 * Copyright (C) 2012 ParanoidAndroid Project
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

package com.android.settings.paranoid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class Lockscreen extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String KEY_ALLOW_ROTATION = "allow_rotation";
    private static final String KEY_SEE_TRHOUGH = "see_through";
    private static final String KEY_HOME_SCREEN_WIDGETS = "home_screen_widgets";
    private static final String KEY_MAXIMIZE_WIDGETS = "maximize_widgets";
    private static final String KEY_VOLBTN_MUSIC_CTRL = "music_controls";
    private static final String KEY_VOLUME_WAKE = "volume_wake";

    private CheckBoxPreference mAllowRotation;
    private CheckBoxPreference mSeeThrough;
    private CheckBoxPreference mHomeScreenWidgets;
    private CheckBoxPreference mMaximizeWidgets;
    private CheckBoxPreference mVolBtnMusicCtrl;
    private CheckBoxPreference mVolumeWake;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.lock_screen_settings);
        PreferenceScreen prefSet = getPreferenceScreen();
        mContext = getActivity();

        mAllowRotation = (CheckBoxPreference) prefSet.findPreference(KEY_ALLOW_ROTATION);
        mAllowRotation.setChecked(Settings.System.getInt(mContext.getContentResolver(),
                    Settings.System.LOCKSCREEN_ALLOW_ROTATION, 0) == 1);

        mSeeThrough = (CheckBoxPreference) prefSet.findPreference(KEY_SEE_TRHOUGH);
        mSeeThrough.setChecked(Settings.System.getInt(mContext.getContentResolver(),
                    Settings.System.LOCKSCREEN_SEE_THROUGH, 0) == 1);

        mMaximizeWidgets = (CheckBoxPreference) prefSet.findPreference(KEY_MAXIMIZE_WIDGETS);
        mMaximizeWidgets.setChecked(Settings.System.getInt(mContext.getContentResolver(),
                    Settings.System.LOCKSCREEN_MAXIMIZE_WIDGETS, 0) == 1);

        mHomeScreenWidgets = (CheckBoxPreference) prefSet.findPreference(KEY_HOME_SCREEN_WIDGETS);
        mHomeScreenWidgets.setChecked(Settings.System.getInt(mContext.getContentResolver(),
                    Settings.System.HOME_SCREEN_WIDGETS, 0) == 1);

        mVolumeWake = (CheckBoxPreference) findPreference(KEY_VOLUME_WAKE);
        mVolumeWake.setChecked(Settings.System.getInt(mContext.getContentResolver(),
                    Settings.System.VOLUME_WAKE_SCREEN, 0) == 1);

        mVolBtnMusicCtrl = (CheckBoxPreference) findPreference(KEY_VOLBTN_MUSIC_CTRL);
        mVolBtnMusicCtrl.setChecked(Settings.System.getInt(mContext.getContentResolver(),
                   Settings.System.VOLBTN_MUSIC_CONTROLS, 0) == 1);
                   
        if(Utils.getScreenType(mContext) == Utils.DEVICE_TABLET) {
            prefSet.removePreference(mAllowRotation);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mAllowRotation) {
            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.LOCKSCREEN_ALLOW_ROTATION, mAllowRotation.isChecked()
                    ? 1 : 0);
        } else if (preference == mSeeThrough) {
            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.LOCKSCREEN_SEE_THROUGH, mSeeThrough.isChecked()
                    ? 1 : 0);
        } else if (preference == mHomeScreenWidgets) {
            final boolean isChecked = mHomeScreenWidgets.isChecked();
            if(isChecked) {
                // Show warning
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.home_screen_widgets_warning_title);
                builder.setMessage(getResources().getString(R.string.home_screen_widgets_warning))
                        .setPositiveButton(com.android.internal.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Settings.System.putInt(mContext.getContentResolver(),
                                        Settings.System.HOME_SCREEN_WIDGETS,
                                        isChecked ? 1 : 0);
                            }
                        })
                        .setNegativeButton(com.android.internal.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mHomeScreenWidgets.setChecked(false);
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                Settings.System.putInt(mContext.getContentResolver(),
                        Settings.System.HOME_SCREEN_WIDGETS, 0);
            }
         } else if (preference == mMaximizeWidgets) {
            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.LOCKSCREEN_MAXIMIZE_WIDGETS, mMaximizeWidgets.isChecked() ? 1 : 0);
         } else if (preference == mVolumeWake) {
            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.VOLUME_WAKE_SCREEN, mVolumeWake.isChecked() ? 1 : 0);
         } else if (preference == mVolBtnMusicCtrl) {
            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.VOLBTN_MUSIC_CONTROLS, mVolBtnMusicCtrl.isChecked() ? 1 : 0);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }
}
