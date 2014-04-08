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
package eu.funceptionapps.convertitall.ui.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Spinner;

import eu.funceptionapps.convertitall.Converter;

public class OnTextChangedListener implements TextWatcher {

    private Spinner fromSpinner;
    private Spinner toSpinner;
    private EditText edUnitIn;
    private EditText edUnitOut;
    private int convertTo;

    public OnTextChangedListener(Spinner fromSpinner, Spinner toSpinner,
                                 EditText edUnitIn, EditText edUnitOut, int convertTo) {
        this.fromSpinner = fromSpinner;
        this.toSpinner = toSpinner;
        this.edUnitIn = edUnitIn;
        this.edUnitOut = edUnitOut;
        this.convertTo = convertTo;
    }

    @Override
    public void afterTextChanged(Editable s) {
        Converter.convert(fromSpinner, toSpinner, edUnitIn, edUnitOut, convertTo);
        if (s.toString().equals("")) {
            edUnitOut.setText("");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
