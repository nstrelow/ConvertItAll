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
 *
 * NOTE: This file has been modified by Sony Ericsson Mobile Communications AB.
 * Modifications are licensed under the License.
 */
package eu.funceptionapps.convertitall.ui.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import eu.funceptionapps.convertitall.Converter;


public class OnSpinnerItemChangedListener implements AdapterView.OnItemSelectedListener {

    private Spinner fromSpinner;
    private Spinner toSpinner;
    private EditText edUnitIn;
    private EditText edUnitOut;
    private int convertTo;

    public OnSpinnerItemChangedListener(Spinner fromSpinner, Spinner toSpinner,
                                        EditText edUnitIn, EditText edUnitOut, int convertTo) {
        this.fromSpinner = fromSpinner;
        this.toSpinner = toSpinner;
        this.edUnitIn = edUnitIn;
        this.edUnitOut = edUnitOut;
        this.convertTo = convertTo;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        Converter.convert(fromSpinner, toSpinner, edUnitIn, edUnitOut,
                convertTo);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
