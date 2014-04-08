/*
 * Copyright (C) 2014 Nils Strelow
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
package eu.funceptionapps.convertitall.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import eu.funceptionapps.convertitall.Converter;
import eu.funceptionapps.convertitall.R;
import eu.funceptionapps.convertitall.Unit;

public class SettingsInterface extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {

    private ListPreference mThemeSetting;
    private Preference mChangelog;
    private Preference mVersionPreference;
    //private Preference mXDAThread;

    public static final String THEME_SETTING_PREF = "pref_theme_setting";
    public static final String HAS_THEME_CHANGED_EXTRA = "has_theme_changed";
    public static final String HAS_FAVORITES_CHANGED_EXTRA = "has_favorites_changed";
    public static final String CHANGELOG_PREF = "pref_changelog";
    //public static final String XDA_THREAD_PREF = "pref_xda_thread";
    public static final String VERSION_PREF = "pref_version";

    public static final String SELECTED_CURRENCIES_PREF = "pref_selected_currencies";
    public static final String SELECTED_LENGTHS_PREF = "pref_selected_lengths";
    public static final String SELECTED_TEMPERATURES_PREF = "pref_selected_temperatures";
    public static final String SELECTED_BYTES_PREF = "pref_selected_bytes";
    public static final String SELECTED_NUMSYS_PREF = "pref_selected_numerical_systems";
    public static final String SELECTED_FORCES_PREF = "pref_selected_forces";

    private Preference mSelectedCurrencies;
    private Preference mSelectedLengths;
    private Preference mSelectedTemperatures;
    private Preference mSelectedBytes;
    private Preference mSelectedNumSys;
    private Preference mSelectedForces;

    SharedPreferences prefs = PreferenceManager
            .getDefaultSharedPreferences(ConverterInterface.thisContext);

    String[] themes = ConverterInterface.thisContext.getResources()
            .getStringArray(R.array.themes);

    String themeSum = themes[Integer.valueOf(prefs.getString(
            THEME_SETTING_PREF, "4"))];

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        InitView.setTheme(prefs, this);

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mSelectedCurrencies = prefSet.findPreference(SELECTED_CURRENCIES_PREF);
        mSelectedLengths = prefSet.findPreference(SELECTED_LENGTHS_PREF);
        mSelectedTemperatures = prefSet.findPreference(SELECTED_TEMPERATURES_PREF);
        mSelectedBytes = prefSet.findPreference(SELECTED_BYTES_PREF);
        mSelectedNumSys = prefSet.findPreference(SELECTED_NUMSYS_PREF);
        mSelectedForces = prefSet.findPreference(SELECTED_FORCES_PREF);

        mThemeSetting = (ListPreference) prefSet.findPreference(THEME_SETTING_PREF);
        mChangelog = prefSet.findPreference(CHANGELOG_PREF);
        mVersionPreference = prefSet.findPreference(VERSION_PREF);
        //mXDAThread = prefSet.findPreference(XDA_THREAD_PREF);

        mThemeSetting.setSummary(themeSum);

        mThemeSetting.setValueIndex(Integer.valueOf(prefs.getString(
                THEME_SETTING_PREF, "0")));

        mThemeSetting.setOnPreferenceChangeListener(this);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            mVersionPreference.setSummary(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        setResult(Activity.RESULT_CANCELED);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {

        if (preference == mChangelog) {
            Intent intent = new Intent(this, Changelog.class);
            startActivity(intent);
        }

       /* if (preference == mXDAThread) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri
                    .parse("http://forum.xda-developers.com/showthread.php?t=1960295"));
            startActivity(i);
        }*/

        if (preference == mSelectedCurrencies) {
            makeFavoriteDialog(Converter.CURRENCY);
        }

        if (preference == mSelectedLengths) {
            makeFavoriteDialog(Converter.LENGTH);
        }

        if (preference == mSelectedTemperatures) {
            makeFavoriteDialog(Converter.TEMPERATURE);
        }

        if (preference == mSelectedBytes) {
            makeFavoriteDialog(Converter.BYTE);
        }

        if (preference == mSelectedNumSys) {
            makeFavoriteDialog(Converter.NUMSYS);
        }

        if (preference == mSelectedForces) {
            makeFavoriteDialog(Converter.FORCE);
        }


        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference == mThemeSetting) {

            String val = newValue.toString();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(THEME_SETTING_PREF, val);
            editor.commit();
            mThemeSetting.setValueIndex(Integer.valueOf(val));

            mThemeSetting.setSummary(themes[Integer.valueOf(prefs
                    .getString(THEME_SETTING_PREF, "0"))]);

            /**
             * theme has changed, so answer that to the ConverterInterface
             */
            Intent data = new Intent();
            data.putExtra(HAS_THEME_CHANGED_EXTRA, true);
            setResult(Activity.RESULT_OK, data);
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void makeFavoriteDialog(int unitId) {

        Intent data = new Intent();
        data.putExtra(HAS_FAVORITES_CHANGED_EXTRA, true);
        setResult(Activity.RESULT_OK, data);

        final int unit = unitId;

        int selectedConverterId = 0;
        boolean[] selectedUnities = null;

        switch (unit) {
            case Converter.CURRENCY:
                Unit.loadSelectedCurrencies(prefs);
                selectedConverterId = R.array.currencies;
                selectedUnities = Unit.selectedCurrencies;
                break;
            case Converter.LENGTH:
                Unit.loadSelectedLengths(prefs);
                selectedConverterId = R.array.lengths;
                selectedUnities = Unit.selectedLengths;
                break;
            case Converter.TEMPERATURE:
                Unit.loadSelectedTemperatures(prefs);
                selectedConverterId = R.array.temperatures;
                selectedUnities = Unit.selectedTemperatures;
                break;
            case Converter.BYTE:
                Unit.loadSelectedBytes(prefs);
                selectedConverterId = R.array.bytes;
                selectedUnities = Unit.selectedBytes;
                break;
            case Converter.NUMSYS:
                Unit.loadSelectedNumSys(prefs);
                selectedConverterId = R.array.numerical_systems;
                selectedUnities = Unit.selectedNumSys;
                break;
            case Converter.FORCE:
                Unit.loadSelectedForces(prefs);
                selectedConverterId = R.array.forces;
                selectedUnities = Unit.selectedForces;
                break;
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(true).setTitle(this.getString(R.string.title_favorites_dialog)).setMultiChoiceItems(
                selectedConverterId, selectedUnities, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                switch (unit) {
                    case Converter.CURRENCY:
                        Unit.changeSelectedCurrency(which, isChecked);
                        break;
                    case Converter.LENGTH:
                        Unit.changeSelectedLength(which, isChecked);
                        break;
                    case Converter.TEMPERATURE:
                        Unit.changeSelectedTemperature(which, isChecked);
                        break;
                    case Converter.BYTE:
                        Unit.changeSelectedBytes(which, isChecked);
                        break;
                    case Converter.NUMSYS:
                        Unit.changeSelectedNumSys(which, isChecked);
                        break;
                }

            }
        });

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog favoriteUnitDialog = dialogBuilder.create();
        favoriteUnitDialog.show();
    }

}