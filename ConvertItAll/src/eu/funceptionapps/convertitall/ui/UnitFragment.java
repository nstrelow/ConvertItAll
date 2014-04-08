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

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import eu.funceptionapps.convertitall.Converter;
import eu.funceptionapps.convertitall.R;
import eu.funceptionapps.convertitall.ui.listeners.OnSpinnerItemChangedListener;
import eu.funceptionapps.convertitall.ui.listeners.OnSwitchClickListener;
import eu.funceptionapps.convertitall.ui.listeners.OnTextChangedListener;

public class UnitFragment extends Fragment {

    public ArrayAdapter<String> unitFromAdapter;
    public ArrayAdapter<String> unitToAdapter;
    public ArrayList<String> SpinnerList;
    private int recentFromItemId;
    private int recentToItemId;
    private int converter;
    private String[] units;
    private String[] units_wiki;
    private boolean[] selectedUnits;
    private String RECENT_FROM_ITEM;
    private String RECENT_TO_ITEM;
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private SharedPreferences prefs;

    public UnitFragment(int recentFromItemId, int recentToItemId, String[] units, String[] units_wiki, boolean[] selectedUnits, int converter, String recentFromItem, String recentToItem) {
        this.recentFromItemId = recentFromItemId;
        this.recentToItemId = recentToItemId;
        this.units = units;
        this.units_wiki = units_wiki;
        this.selectedUnits = selectedUnits;
        this.converter = converter;
        this.RECENT_FROM_ITEM = recentFromItem;
        this.RECENT_TO_ITEM = recentToItem;
    }

    @Override
    public void onStop() {
        if (fromSpinner.getSelectedItem() != null
                && toSpinner.getSelectedItem() != null) {
            String fromVal = String.valueOf(fromSpinner.getSelectedItemId());
            String toVal = String.valueOf(toSpinner.getSelectedItemId());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(RECENT_FROM_ITEM, fromVal);
            editor.putString(RECENT_TO_ITEM, toVal);
            editor.commit();
        }
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        prefs = PreferenceManager
                .getDefaultSharedPreferences(ConverterInterface.thisContext);

        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.unit_fragment, container, false);

        ImageButton switchButton = (ImageButton) layout.findViewById(R.id.bt_switch);

        switchButton.setImageResource(R.drawable.ic_switch_button_holo_light);

        EditText edUnitIn = (EditText) layout.findViewById(R.id.ed_unitIn);
        EditText edUnitOut = (EditText) layout.findViewById(R.id.ed_unitOut);

        SpinnerList = new ArrayList<String>();
        for (int i = 0; i < selectedUnits.length; i++) {
            if (selectedUnits[i]) {
                SpinnerList.add(units[i]);
            }
        }

        fromSpinner = (Spinner) layout.findViewById(R.id.sp_from_unit);

        toSpinner = (Spinner) layout.findViewById(R.id.sp_to_unit);

        if (converter == Converter.CURRENCY) {
            unitFromAdapter = new CurrencyAdapter(ConverterInterface.thisContext, R.layout.spinner_row,
                    SpinnerList);
        } else {

            unitFromAdapter = new ArrayAdapter<String>(
                    ConverterInterface.thisContext,
                    android.R.layout.simple_spinner_item, SpinnerList);
        }

        unitFromAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        unitToAdapter = unitFromAdapter;

        unitToAdapter.notifyDataSetChanged();

        fromSpinner.setAdapter(unitFromAdapter);
        if (SpinnerList.size() > recentFromItemId)
            fromSpinner.setSelection(recentFromItemId);
        toSpinner.setAdapter(unitToAdapter);
        if (SpinnerList.size() > recentToItemId)
            toSpinner.setSelection(recentToItemId);

        OnLongClickListener wikiLongClickListenerFrom = new OnLongClickListener() {

            public boolean onLongClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(units_wiki[fromSpinner
                        .getSelectedItemPosition()]));
                startActivity(i);
                return false;
            }

        };

        OnLongClickListener wikiLongClickListenerTo = new
                OnLongClickListener() {

                    public boolean onLongClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(units_wiki[toSpinner
                                .getSelectedItemPosition()]));
                        startActivity(i);
                        return false;
                    }

                };

        fromSpinner.setOnLongClickListener(wikiLongClickListenerFrom);
        toSpinner.setOnLongClickListener(wikiLongClickListenerTo);

        switchButton.setOnClickListener(new OnSwitchClickListener(fromSpinner,
                toSpinner, edUnitIn, edUnitOut, converter));

        edUnitIn.addTextChangedListener(new OnTextChangedListener(fromSpinner, toSpinner, edUnitIn, edUnitOut, converter));
        fromSpinner.setOnItemSelectedListener(new OnSpinnerItemChangedListener(fromSpinner, toSpinner, edUnitIn, edUnitOut, converter));
        toSpinner.setOnItemSelectedListener(new OnSpinnerItemChangedListener(fromSpinner, toSpinner, edUnitIn, edUnitOut, converter));

        return layout;
    }

}
