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
package eu.funceptionapps.convertitall;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.funceptionapps.convertitall.ObjectSerializer;
import eu.funceptionapps.convertitall.ui.ConverterInterface;
import eu.funceptionapps.convertitall.ui.SettingsInterface;
import eu.funceptionapps.convertitall.R;

public class Unit {

    public static boolean[] selectedCurrencies;
    public static boolean[] selectedLengths;
    public static boolean[] selectedTemperatures;
    public static boolean[] selectedBytes;
    public static boolean[] selectedNumSys;
    public static boolean[] selectedForces;

    private static void saveFavoriteString(boolean[] favoritesSet, SharedPreferences sharedPrefs, String pref) {
        List<String> favoritesStringList = new ArrayList<String>();

        for (int i = 0; i < favoritesSet.length; i++) {
            if (favoritesSet[i]) {
                favoritesStringList.add(String.valueOf(i));
            }
        }
        setFavoritesList(sharedPrefs, pref, favoritesStringList);
    }

    public static void loadSelectedCurrencies(SharedPreferences prefs) {

        int length = ConverterInterface.thisContext.getResources().getIntArray(R.array.currencies_id).length;

        selectedCurrencies = new boolean[length];

        final ArrayList<String> defValuesList = new ArrayList<String>(Arrays.asList(ConverterInterface.thisContext.getResources().getStringArray(
                R.array.default_currencies)));
        List<String> favoritesList = getFavoritesList(prefs, SettingsInterface.SELECTED_CURRENCIES_PREF, defValuesList);

        if (favoritesList == null) {
            favoritesList = Arrays.asList(ConverterInterface.thisContext.getResources().getStringArray(R.array.default_currencies));
        }

        String[] prefStrings = favoritesList.toArray(new String[favoritesList.size()]);

        for (String prefString : prefStrings) {
            selectedCurrencies[Integer.valueOf(prefString)] = true;
        }
    }

    public static List<String> getFavoritesList(SharedPreferences prefs, String pref, ArrayList<String> defValuesList) {
        List<String> favoritesList;
        try {
            final String defValue = ObjectSerializer.serialize(defValuesList);
            final String string = prefs.getString(pref, defValue);
            final Object deserialize = ObjectSerializer.deserialize(string);
            favoritesList = (ArrayList<String>) deserialize;
        } catch (IOException e) {
            favoritesList = defValuesList;
            e.printStackTrace();
        }
        return favoritesList;
    }

    public static void setFavoritesList(SharedPreferences prefs, String pref, List<String> favoritesList) {

        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString(pref, ObjectSerializer.serialize(new ArrayList<String>(favoritesList)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    public static int getSelectedCurrencies(int selectedSpinnerId) {
        int selectedUnitId = 0;
        int k = -1;

        for (int i = 0; i < selectedCurrencies.length; i++) {
            if (selectedCurrencies[i]) {
                k++;
                if (k == selectedSpinnerId) {
                    selectedUnitId = i;
                    break;
                }
            }

        }
        return selectedUnitId;
    }

    public static void changeSelectedCurrency(int which, boolean isFavorite) {
        selectedCurrencies[which] = isFavorite;
        saveFavoriteString(selectedCurrencies, PreferenceManager.getDefaultSharedPreferences(ConverterInterface.thisContext), SettingsInterface.SELECTED_CURRENCIES_PREF);
    }

    public static void loadSelectedLengths(SharedPreferences prefs) {

        int length = ConverterInterface.thisContext.getResources().getIntArray(R.array.lengths_id).length;

        selectedLengths = new boolean[length];

        final ArrayList<String> defValuesList = new ArrayList<String>(Arrays.asList(ConverterInterface.thisContext.getResources().getStringArray(
                R.array.default_lengths)));
        List<String> favoritesList = getFavoritesList(prefs, SettingsInterface.SELECTED_LENGTHS_PREF, defValuesList);

        if (favoritesList == null) {
            favoritesList = Arrays.asList(ConverterInterface.thisContext.getResources().getStringArray(R.array.default_lengths));
        }

        String[] prefStrings = favoritesList.toArray(new String[favoritesList.size()]);

        for (String prefString : prefStrings) {
            selectedLengths[Integer.valueOf(prefString)] = true;
        }
    }

    public static int getSelectedLengths(int selectedSpinnerId) {
        int selectedUnitId = 0;
        int k = -1;

        for (int i = 0; i < selectedLengths.length; i++) {
            if (selectedLengths[i]) {
                k++;
                if (k == selectedSpinnerId) {
                    selectedUnitId = i;
                    break;
                }
            }

        }
        return selectedUnitId;
    }

    public static void changeSelectedLength(int which, boolean isFavorite) {
        selectedLengths[which] = isFavorite;
        saveFavoriteString(selectedLengths, PreferenceManager.getDefaultSharedPreferences(ConverterInterface.thisContext), SettingsInterface.SELECTED_LENGTHS_PREF);
    }

    public static void loadSelectedTemperatures(SharedPreferences prefs) {

        int length = ConverterInterface.thisContext.getResources().getIntArray(R.array.temperatures_id).length;

        selectedTemperatures = new boolean[length];

        final ArrayList<String> defValuesList = new ArrayList<String>(Arrays.asList(ConverterInterface.thisContext.getResources().getStringArray(
                R.array.default_temperatures)));
        List<String> favoritesList = getFavoritesList(prefs, SettingsInterface.SELECTED_TEMPERATURES_PREF, defValuesList);

        if (favoritesList == null) {
            favoritesList = Arrays.asList(ConverterInterface.thisContext.getResources().getStringArray(R.array.default_temperatures));
        }

        String[] prefStrings = favoritesList.toArray(new String[favoritesList.size()]);

        for (String prefString : prefStrings) {
            selectedTemperatures[Integer.valueOf(prefString)] = true;
        }
    }

    public static int getSelectedTemperatures(int selectedSpinnerId) {
        int selectedUnitId = 0;
        int k = -1;

        for (int i = 0; i < selectedTemperatures.length; i++) {
            if (selectedTemperatures[i]) {
                k++;
                if (k == selectedSpinnerId) {
                    selectedUnitId = i;
                    break;
                }
            }

        }
        return selectedUnitId;
    }

    public static void changeSelectedTemperature(int which, boolean isFavorite) {
        selectedTemperatures[which] = isFavorite;
        saveFavoriteString(selectedTemperatures, PreferenceManager.getDefaultSharedPreferences(ConverterInterface.thisContext), SettingsInterface.SELECTED_TEMPERATURES_PREF);
    }

    public static void loadSelectedBytes(SharedPreferences prefs) {

        int length = ConverterInterface.thisContext.getResources().getIntArray(R.array.bytes_id).length;

        selectedBytes = new boolean[length];

        final ArrayList<String> defValuesList = new ArrayList<String>(Arrays.asList(ConverterInterface.thisContext.getResources().getStringArray(
                R.array.default_bytes)));
        List<String> favoritesList = getFavoritesList(prefs, SettingsInterface.SELECTED_BYTES_PREF, defValuesList);

        if (favoritesList == null) {
            favoritesList = Arrays.asList(ConverterInterface.thisContext.getResources().getStringArray(R.array.default_bytes));
        }

        String[] prefStrings = favoritesList.toArray(new String[favoritesList.size()]);

        for (String prefString : prefStrings) {
            selectedBytes[Integer.valueOf(prefString)] = true;
        }
    }

    public static int getSelectedBytes(int selectedSpinnerId) {
        int selectedUnitId = 0;
        int k = -1;

        for (int i = 0; i < selectedBytes.length; i++) {
            if (selectedBytes[i]) {
                k++;
                if (k == selectedSpinnerId) {
                    selectedUnitId = i;
                    break;
                }
            }

        }
        return selectedUnitId;
    }

    public static void changeSelectedBytes(int which, boolean isFavorite) {
        selectedBytes[which] = isFavorite;
        saveFavoriteString(selectedBytes, PreferenceManager.getDefaultSharedPreferences(ConverterInterface.thisContext), SettingsInterface.SELECTED_BYTES_PREF);
    }

    public static void loadSelectedNumSys(SharedPreferences prefs) {

        int length = ConverterInterface.thisContext.getResources().getIntArray(R.array.numerical_systems_id).length;

        selectedNumSys = new boolean[length];

        final ArrayList<String> defValuesList = new ArrayList<String>(Arrays.asList(ConverterInterface.thisContext.getResources().getStringArray(
                R.array.numerical_systems_id)));
        List<String> favoritesList = getFavoritesList(prefs, SettingsInterface.SELECTED_NUMSYS_PREF, defValuesList);

        // use numerical_systems_id as default cause all numsys are default
        if (favoritesList == null) {
            favoritesList = Arrays.asList(ConverterInterface.thisContext.getResources().getStringArray(R.array.numerical_systems_id));
        }

        String[] prefStrings = favoritesList.toArray(new String[favoritesList.size()]);

        for (String prefString : prefStrings) {
            selectedNumSys[Integer.valueOf(prefString)] = true;
        }
    }

    public static int getSelectedNumSys(int selectedSpinnerId) {
        int selectedUnitId = 0;
        int k = -1;

        for (int i = 0; i < selectedNumSys.length; i++) {
            if (selectedNumSys[i]) {
                k++;
                if (k == selectedSpinnerId) {
                    selectedUnitId = i;
                    break;
                }
            }

        }
        return selectedUnitId;
    }

    public static void changeSelectedNumSys(int which, boolean isFavorite) {
        selectedNumSys[which] = isFavorite;
        saveFavoriteString(selectedNumSys, PreferenceManager.getDefaultSharedPreferences(ConverterInterface.thisContext), SettingsInterface.SELECTED_NUMSYS_PREF);
    }

    public static void loadSelectedForces(SharedPreferences prefs) {

        int length = ConverterInterface.thisContext.getResources().getIntArray(R.array.forces_id).length;

        selectedForces = new boolean[length];

        final ArrayList<String> defValuesList = new ArrayList<String>(Arrays.asList(ConverterInterface.thisContext.getResources().getStringArray(
                R.array.forces_id)));
        List<String> favoritesList = getFavoritesList(prefs, SettingsInterface.SELECTED_FORCES_PREF, defValuesList);

        // use numerical_systems_id as default cause all numsys are default
        if (favoritesList == null) {
            favoritesList = Arrays.asList(ConverterInterface.thisContext.getResources().getStringArray(R.array.forces_id));
        }

        String[] prefStrings = favoritesList.toArray(new String[favoritesList.size()]);

        for (String prefString : prefStrings) {
            selectedForces[Integer.valueOf(prefString)] = true;
        }
    }

    public static int getSelectedForce(int selectedSpinnerId) {
        int selectedUnitId = 0;
        int k = -1;

        for (int i = 0; i < selectedForces.length; i++) {
            if (selectedForces[i]) {
                k++;
                if (k == selectedSpinnerId) {
                    selectedUnitId = i;
                    break;
                }
            }

        }
        return selectedUnitId;
    }

    public static void changeSelectedForce(int which, boolean isFavorite) {
        selectedForces[which] = isFavorite;
        saveFavoriteString(selectedForces, PreferenceManager.getDefaultSharedPreferences(ConverterInterface.thisContext), SettingsInterface.SELECTED_FORCES_PREF);
    }
}
