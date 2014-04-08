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

import android.content.Context;
import android.content.SharedPreferences;

import eu.funceptionapps.convertitall.R;

public class InitView {


    public static final int THEME_BLUE = 0;
    public static final int THEME_HOLO_LIGHT = 1;
    public static final int THEME_GREEN = 2;
    public static final int THEME_LIGHT_GREEN = 3;
    public static final int THEME_ORANGE = 4;
    public static final int THEME_LIGHT_ORANGE = 5;
    public static final int THEME_RED = 6;
    public static final int THEME_LIGHT_RED = 7;
    public static final int THEME_VIOLET = 8;
    public static final int THEME_LIGHT_VIOLET = 9;

    private static int currentTheme = 4;

    public static void setTheme(SharedPreferences prefs, Context context) {

        int theme = Integer.valueOf(prefs.getString(
                SettingsInterface.THEME_SETTING_PREF, "4"));

        currentTheme = theme;

        switch (theme) {
            case THEME_BLUE:
                context.setTheme(R.style.Theme_Cia_blue);
                break;
            case THEME_HOLO_LIGHT:
                context.setTheme(R.style.Theme_AppCompat_Light);
                break;
            case THEME_GREEN:
                context.setTheme(R.style.Theme_Cia_green);
                break;
            case THEME_LIGHT_GREEN:
                context.setTheme(R.style.Theme_Cia_light_green);
                break;
            case THEME_ORANGE:
                context.setTheme(R.style.Theme_Cia_orange);
                break;
            case THEME_LIGHT_ORANGE:
                context.setTheme(R.style.Theme_Cia_light_orange);
                break;
            case THEME_RED:
                context.setTheme(R.style.Theme_Cia_red);
                break;
            case THEME_LIGHT_RED:
                context.setTheme(R.style.Theme_Cia_light_red);
                break;
            case THEME_VIOLET:
                context.setTheme(R.style.Theme_Cia_violet);
                break;
            case THEME_LIGHT_VIOLET:
                context.setTheme(R.style.Theme_Cia_light_violet);
                break;
        }
    }

    public static int getTheme() {
        return currentTheme;
    }

}
