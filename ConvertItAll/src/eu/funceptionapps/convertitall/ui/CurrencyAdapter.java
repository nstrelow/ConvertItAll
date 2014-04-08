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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import eu.funceptionapps.convertitall.R;
import eu.funceptionapps.convertitall.Unit;

/**
 * Created by djnilse on 11.11.13.
 */
public class CurrencyAdapter extends ArrayAdapter<String> {

    private final int flags[] = {R.drawable.australia, R.drawable.belarus,
            R.drawable.canada, R.drawable.switzerland, R.drawable.china,
            R.drawable.czech_republic, R.drawable.denmark,
            R.drawable.european_union, R.drawable.united_kingdom_great_britain,
            R.drawable.croatia, R.drawable.hungary, R.drawable.india,
            R.drawable.iceland, R.drawable.japan, R.drawable.latvia,
            R.drawable.norway, R.drawable.new_zealand, R.drawable.poland,
            R.drawable.romania, R.drawable.russia, R.drawable.sweden,
            R.drawable.singapore, R.drawable.turkey, R.drawable.ukraine,
            R.drawable.united_states_of_america_usa, R.drawable.viet_nam,
            R.drawable.south_africa};

    private String[] currencies = ConverterInterface.thisContext
            .getResources().getStringArray(R.array.currencies);

    public CurrencyAdapter(Context context, int textViewResourceId,
                           List<String> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent, this.getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, this.getContext());
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent, Context context) {
        // return super.getView(position, convertView, parent);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.spinner_row, parent, false);

        TextView label = (TextView) row.findViewById(R.id.spinnerTextView);

        ImageView icon = (ImageView) row.findViewById(R.id.image);

        int k = -1;
        for (int i = 0; i < Unit.selectedCurrencies.length; i++) {
            if (Unit.selectedCurrencies[i]) {
                k++;
                if (k == position) {
                    label.setText(currencies[i]);
                    icon.setImageResource(flags[i]);
                }
            }
        }

        return row;
    }

}