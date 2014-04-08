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
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.ClipboardManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import eu.funceptionapps.convertitall.Converter;
import eu.funceptionapps.convertitall.R;
import eu.funceptionapps.convertitall.Unit;
import eu.funceptionapps.convertitall.units.Currency;
import eu.funceptionapps.convertitall.units.Currency.LoadCurrenciesDBTask;
import uk.co.androidalliance.edgeeffectoverride.EdgeEffectListView;


public class ConverterInterface extends ActionBarActivity {

    // access things the static way <---- baaaaaad
    public static Context thisContext;
    public static Intent MainIntent;
    public static Activity mainActivity;
    public static FragmentManager mFragmentManager;
    public static boolean isCurrency = true;
    public static Intent sendIntent;
    public static int settingsRequestCode = 1000;
    final String RECENT_PAGE_ID = "recent_page_id";
    final String RECENT_FROM_ITEM_CURRENCY = "recent_from_currency_item";
    final String RECENT_TO_ITEM_CURRENCY = "recent_to_currency_item";
    final String RECENT_FROM_ITEM_LENGTH = "recent_from_length_item";
    final String RECENT_TO_ITEM_LENGTH = "recent_to_length_item";
    final String RECENT_FROM_ITEM_TEMPERATURE = "recent_from_temperature_item";
    final String RECENT_TO_ITEM_TEMPERATURE = "recent_to_temperature_item";
    final String RECENT_FROM_ITEM_BYTE = "recent_from_byte_item";
    final String RECENT_TO_ITEM_BYTE = "recent_to_byte_item";
    final String RECENT_FROM_ITEM_NUMERICAL_SYSTEM = "recent_from_numerical_system_item";
    final String RECENT_TO_ITEM_NUMERICAL_SYSTEM = "recent_to_numerical_system_item";
    final String RECENT_FROM_ITEM_FORCE = "recent_from_force_item";
    final String RECENT_TO_ITEM_FORCE = "recent_to_force_item";
    private final Handler drawerHandler = new Handler();
    TextView oldSelectedListItem;
    private DrawerLayout mDrawerLayout;
    private EdgeEffectListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<String> mUnitCategories;
    private int currentPageId;
    private SharedPreferences prefs;
    private boolean COMES_FROM_SETTINGS = false;
    public static String sharingMsg;
    private ShareActionProvider mShareActionProvider;

    /*
     * for monkeys who don't know how to use stuff
     */
    public static void dumbassProove() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(thisContext);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setTitle(thisContext.getString(R.string.title_novalue_dialog));
        dialogBuilder.setMessage(thisContext.getString(R.string.msg_novalue_dialog));

        dialogBuilder.setPositiveButton("OK", null);

        AlertDialog dumbassProove = dialogBuilder.create();
        dumbassProove.show();
    }

    public static String makeShare(String fromConvertable, String fromValue, String toConvertable, String toValue) {

        return fromValue + " " + fromConvertable + " are " + toValue + " " + toConvertable + "\n"
                + "This was calculated using ConverItAll by FUNception Apps" + "\n";
    }

    public static void makeUpdateDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(thisContext);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setTitle(thisContext.getString(R.string.title_update_dialog));
        dialogBuilder.setMessage(thisContext.getString(R.string.msg_update_dialog));
        dialogBuilder.setIcon(R.drawable.ic_dialog_alert_holo_light);

        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                try {
                    Currency.updateCurrencies();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertInternetConnection = dialogBuilder.create();
        alertInternetConnection.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        PreferenceManager.setDefaultValues(this, "eu.djnilse.convertitall_preferences", Context.MODE_PRIVATE,
                R.xml.settings, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int recentPageId = Integer.valueOf(prefs.getString(RECENT_PAGE_ID, "0"));

        // delete history of ShareActionProvider
        deleteFile("share_history.xml");

        InitView.setTheme(prefs, this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.converter_interface);

        thisContext = this;
        MainIntent = getIntent();
        mainActivity = getParent();

        mTitle = mDrawerTitle = getTitle();

        mUnitCategories = new ArrayList<String>();
        String[] arr = getResources().getStringArray(R.array.unit_categories);
        Collections.addAll(mUnitCategories, arr);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (EdgeEffectListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // Set the adapter for the list view
        ArrayAdapter<String> mListAdapter = new ArrayAdapter<String>(this, R.layout.drawer_textview, mUnitCategories);
        mDrawerList.setAdapter(mListAdapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_icon));

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        int drawerIconId = R.drawable.ic_navigation_drawer;
        if (InitView.getTheme() == InitView.THEME_HOLO_LIGHT) {
            drawerIconId = R.drawable.ic_drawer_holo_dark;
        }
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                drawerIconId, /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description for accessibility */
                R.string.drawer_close /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()

            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(recentPageId);
            currentPageId = recentPageId;
        }

        if (Converter.CurrencyDatabase.exists()) {
            try {
                new LoadCurrenciesDBTask().execute("");
            } catch (CursorIndexOutOfBoundsException e) {
                Converter.CurrencyDatabase.delete();
            }
        }

        //new UpdateCheckTask().execute(true);

    }

    private void scheduleLaunchAndCloseDrawer(final int position) {
        drawerHandler.removeCallbacksAndMessages(null); // Clears any previously posted runnables, for double clicks

        drawerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectItem(position);
            }
        }, 250);
        // The millisecond delay is arbitrary and was arrived at through trial and error

        mDrawerLayout.closeDrawers();
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        // Create a new fragment and get the right one from getFragment
        Fragment fragment = getFragment(position);

        // set isCurrency for updateCurrencies MenuItem
        isCurrency = position == 0;

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        mFragmentManager = fragmentManager;
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mDrawerList.setItemChecked(position, true); //TODO make it work
        }
        //TODO: add bold text at startup


        setTitle(mUnitCategories.get(position));
    }

    public Fragment getFragment(int position) {

        int recentFromItemId;
        int recentToItemId;
        int converter = position;
        String[] units = null;
        String[] units_wiki = null;
        boolean[] selectedUnits = null;
        String recentFromItem = null;
        String recentToItem = null;

        switch (position) {
            case Converter.CURRENCY:
                recentFromItem = RECENT_FROM_ITEM_CURRENCY;
                recentToItem = RECENT_TO_ITEM_CURRENCY;
                units = ConverterInterface.thisContext.getResources().getStringArray(R.array.currencies);
                units_wiki = ConverterInterface.thisContext.getResources().getStringArray(R.array.currencies_wiki);
                Unit.loadSelectedCurrencies(prefs);
                selectedUnits = Unit.selectedCurrencies;
                break;
            case Converter.LENGTH:
                recentFromItem = RECENT_FROM_ITEM_LENGTH;
                recentToItem = RECENT_TO_ITEM_LENGTH;
                units = ConverterInterface.thisContext.getResources().getStringArray(R.array.lengths);
                units_wiki = ConverterInterface.thisContext.getResources().getStringArray(R.array.lengths_wiki);
                Unit.loadSelectedLengths(prefs);
                selectedUnits = Unit.selectedLengths;
                break;
            case Converter.TEMPERATURE:
                recentFromItem = RECENT_FROM_ITEM_TEMPERATURE;
                recentToItem = RECENT_TO_ITEM_TEMPERATURE;
                units = ConverterInterface.thisContext.getResources().getStringArray(R.array.temperatures);
                units_wiki = ConverterInterface.thisContext.getResources().getStringArray(R.array.temperatures_wiki);
                Unit.loadSelectedTemperatures(prefs);
                selectedUnits = Unit.selectedTemperatures;
                break;
            case Converter.BYTE:
                recentFromItem = RECENT_FROM_ITEM_BYTE;
                recentToItem = RECENT_TO_ITEM_BYTE;
                units = ConverterInterface.thisContext.getResources().getStringArray(R.array.bytes);
                units_wiki = ConverterInterface.thisContext.getResources().getStringArray(R.array.bytes_wiki);
                Unit.loadSelectedBytes(prefs);
                selectedUnits = Unit.selectedBytes;
                break;
            case Converter.NUMSYS:
                recentFromItem = RECENT_FROM_ITEM_NUMERICAL_SYSTEM;
                recentToItem = RECENT_TO_ITEM_NUMERICAL_SYSTEM;
                units = ConverterInterface.thisContext.getResources().getStringArray(R.array.numerical_systems);
                units_wiki = ConverterInterface.thisContext.getResources().getStringArray(R.array.numerical_systems_wiki);
                Unit.loadSelectedNumSys(prefs);
                selectedUnits = Unit.selectedNumSys;
                break;
            case Converter.FORCE:
                recentFromItem = RECENT_FROM_ITEM_FORCE;
                recentToItem = RECENT_TO_ITEM_FORCE;
                units = ConverterInterface.thisContext.getResources().getStringArray(R.array.forces);
                units_wiki = ConverterInterface.thisContext.getResources().getStringArray(R.array.forces_wiki);
                Unit.loadSelectedForces(prefs);
                selectedUnits = Unit.selectedForces;
        }
        recentFromItemId = Integer.valueOf(prefs.getString(recentFromItem, "0"));
        recentToItemId = Integer.valueOf(prefs.getString(recentToItem, "0"));
        return new UnitFragment(recentFromItemId, recentToItemId, units, units_wiki, selectedUnits, converter, recentFromItem, recentToItem);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        if (COMES_FROM_SETTINGS) {
            selectItem(currentPageId);
            COMES_FROM_SETTINGS = false;
        }
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        // save recentPageId
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(RECENT_PAGE_ID, String.valueOf(currentPageId));
        editor.commit();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.interface_menu, menu);

        MenuItem shareMenuItem = menu.findItem(R.id.menu_item_share);

        // Set up ShareActionProvider to not keep history and take unnecessary
        // place in the ActionBar
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
        //mShareActionProvider.setShareHistoryFileName(null); //doesn't work anymore :'(


        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final MenuItem item = menu.findItem(R.id.menu_item_update_currencies);
        if (item != null)
            item.setVisible(isCurrency);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }

        switch (item.getItemId()) {
            case R.id.homeAsUp:
                return true;

            case R.id.menu_item_copy:
                EditText edResult = (EditText) findViewById(R.id.ed_unitOut);

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(edResult.getText().toString());
                Toast.makeText(this, getResources().getString(R.string.toast_copy_to_clipboard), Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_item_favorite:
                makeFavoriteDialog();
                break;

            case R.id.menu_item_update_currencies:
                makeUpdateDialog();
                break;

            case R.id.menu_item_settings:
                Intent intent = new Intent(this, SettingsInterface.class);
                startActivityForResult(intent, settingsRequestCode);
                break;

            case R.id.menu_item_share:
                if (mShareActionProvider != null) {
                    sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sharingMsg);
                    sendIntent.setType("text/plain");
                    mShareActionProvider.setShareIntent(sendIntent);
                }
                break;

            case R.id.menu_item_feedback:
                Intent feedback = new Intent();
                feedback.setAction(Intent.ACTION_SEND);

                String gmail = getResources().getString(R.string.gmail);

                String[] recipients = new String[]{gmail, "",};

                feedback.putExtra(Intent.EXTRA_EMAIL, recipients);
                feedback.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                feedback.setType("message/rfc822");
                startActivity(feedback);
                break;
        }
        return true;
    }

    public void makeFavoriteDialog() {

        int selectedConverterId = 0;
        boolean[] selectedUnities = null;

        switch (currentPageId) {
            case Converter.CURRENCY:
                selectedConverterId = R.array.currencies;
                selectedUnities = Unit.selectedCurrencies;
                break;
            case Converter.LENGTH:
                selectedConverterId = R.array.lengths;
                selectedUnities = Unit.selectedLengths;
                break;
            case Converter.TEMPERATURE:
                selectedConverterId = R.array.temperatures;
                selectedUnities = Unit.selectedTemperatures;
                break;
            case Converter.BYTE:
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

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(thisContext);
        dialogBuilder.setCancelable(true).setTitle(thisContext.getString(R.string.title_favorites_dialog)).setMultiChoiceItems(
                selectedConverterId, selectedUnities, new OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                switch (currentPageId) {
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
                    case Converter.FORCE:
                        Unit.changeSelectedForce(which, isChecked);
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
        favoriteUnitDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                selectItem(currentPageId);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == settingsRequestCode && resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(SettingsInterface.HAS_THEME_CHANGED_EXTRA, false)) {
                restartInterface();
            }
            if (data.getBooleanExtra(SettingsInterface.HAS_FAVORITES_CHANGED_EXTRA, false)) {
                COMES_FROM_SETTINGS = true;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void restartInterface() {

        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            // first turn old into normal, then assign new view
            if (oldSelectedListItem != null) {
                //oldSelectedListItem.setTypeface(null, Typeface.NORMAL);
                oldSelectedListItem.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            }
            scheduleLaunchAndCloseDrawer(position);
            oldSelectedListItem = (TextView) view;
            //oldSelectedListItem.setTypeface(null, Typeface.BOLD);
            //TODO: do this with setItemChecked
            oldSelectedListItem.setBackgroundColor(getResources().getColor(R.color.pressed_cia_light));
            currentPageId = position;
        }
    }
}
