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

import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import eu.funceptionapps.convertitall.ui.ConverterInterface;
import eu.funceptionapps.convertitall.units.Bytes;
import eu.funceptionapps.convertitall.units.Currency;
import eu.funceptionapps.convertitall.units.Force;
import eu.funceptionapps.convertitall.units.Length;
import eu.funceptionapps.convertitall.units.NumericalSystem;
import eu.funceptionapps.convertitall.units.Temperature;

import static eu.funceptionapps.convertitall.ui.ConverterInterface.dumbassProove;
import static eu.funceptionapps.convertitall.ui.ConverterInterface.makeShare;
import static eu.funceptionapps.convertitall.ui.ConverterInterface.makeUpdateDialog;
import static eu.funceptionapps.convertitall.ui.ConverterInterface.thisContext;

public class Converter {

    public final static int CURRENCY = 0;
    public final static int LENGTH = 1;
    public final static int TEMPERATURE = 2;
    public final static int BYTE = 3;
    public final static int NUMSYS = 4;
    public final static int FORCE = 5;
    /**
     * different temperature units
     */
    public static final int CELSIUS = 0;
    public static final int FAHRENHEIT = 1;
    public static final int KELVIN = 2;
    public static final int REAUMUR = 3;
    public static final int RANKINE = 4;
    public static final int NEWTON = 5;
    public static final int DELISLE = 6;
    public static final int ROMER = 7;
    /**
     * different numerical system units
     */
    public static final int DECIMAL = 0;
    public static final int BINARY = 1;
    public static final int HEXADECIMAL = 2;
    public static final int OCTAL = 3;
    /**
     * different force units
     */
    public static final int NEWTON_FORCE = 0;
    public static final int DYNE = 1;
    public static final int KILOGRAM_FORCE = 2;
    public static final int POUND_FORCE = 3;
    //    public static final int KILOMETER = 0;
//    public static final int METER = 1;
//    public static final int DECIMETER = 2;
//    public static final int CENTIMETER = 3;
//    public static final int MILLIMETER = 4;
//    public static final int MIRCOMETER = 5;
//    public static final int NANOMETER = 6;
//    public static final int MILES = 7;
//    public static final int YARD = 8;
//    public static final int FEET = 9;
//    public static final int INCH = 10;
    public static File CurrencyDatabase = thisContext
            .getApplicationContext().getDatabasePath("currencies.db");

    public static void convert(Spinner fromSpinner, Spinner toSpinner,
                               EditText edUnitIn, EditText edUnitOut, int convertTo) {
        final int fromChoosenUnit = (int) fromSpinner.getSelectedItemId();
        final int toChoosenUnit = (int) toSpinner.getSelectedItemId();

        String value = edUnitIn.getText().toString();

        //String sharingMsg = "";
        if (!value.equals("")) {

            double inputValue = 0.0;
            try {
                inputValue = Double.valueOf(value);
            } catch (NumberFormatException ne) {
                //dumbassProove();

            }

            String result = null;

            switch (convertTo) {
                case Converter.CURRENCY:

                    try {
                        result = Converter.convertCurrency(inputValue,
                                Unit.getSelectedCurrencies(fromChoosenUnit), Unit.getSelectedCurrencies(toChoosenUnit));
                        edUnitOut.setText(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String[] currencyLetters = thisContext
                            .getResources().getStringArray(R.array.currencies);

                    // set up sharing msg
                    ConverterInterface.sharingMsg =
                            makeShare(currencyLetters[fromChoosenUnit],
                                    edUnitIn.getText().toString(),
                                    currencyLetters[toChoosenUnit], result);
                    break;
                case Converter.LENGTH:

                    result = Converter.convertLength(inputValue, Unit.getSelectedLengths(fromChoosenUnit),
                            Unit.getSelectedLengths(toChoosenUnit));
                    edUnitOut.setText(result);

                    String[] lengthUnits = thisContext
                            .getResources().getStringArray(R.array.lengths);

                    // set up sharing msg
                    ConverterInterface.sharingMsg =
                            makeShare(lengthUnits[fromChoosenUnit], edUnitIn
                                    .getText().toString(),
                                    lengthUnits[toChoosenUnit], result);

                    break;
                case Converter.TEMPERATURE:

                    result = String.valueOf(Converter.convertTemperature(
                            inputValue, Unit.getSelectedTemperatures(fromChoosenUnit), Unit.getSelectedTemperatures(toChoosenUnit)));
                    if (result.endsWith(".0")) {
                        result = result.replace(".0", "");
                    }
                    edUnitOut.setText(result);

                    String[] tempUnits = thisContext
                            .getResources()
                            .getStringArray(R.array.temperatures);

                    // set up sharing msg
                    ConverterInterface.sharingMsg =
                            makeShare(tempUnits[fromChoosenUnit], edUnitIn
                                    .getText().toString(),
                                    tempUnits[toChoosenUnit], result);

                    break;
                case Converter.BYTE:

                    result = Converter.convertByte(inputValue, Unit.getSelectedBytes(fromChoosenUnit),
                            Unit.getSelectedBytes(toChoosenUnit));

                    edUnitOut.setText(result);

                    String[] byteUnits = thisContext
                            .getResources().getStringArray(R.array.bytes);

                    // set up sharing msg
                    ConverterInterface.sharingMsg =
                            makeShare(byteUnits[fromChoosenUnit], edUnitIn
                                    .getText().toString(),
                                    byteUnits[toChoosenUnit], result);

                    break;
                case Converter.NUMSYS:
                    // give it the uncasted value
                    result = Converter.convertNumericalSystem(value, Unit.getSelectedNumSys(fromChoosenUnit),
                            Unit.getSelectedNumSys(toChoosenUnit));

                    edUnitOut.setText(result);

                    String[] numSysUnits = thisContext
                            .getResources().getStringArray(R.array.numerical_systems);

                    // set up sharing msg
                    ConverterInterface.sharingMsg =
                            makeShare(numSysUnits[fromChoosenUnit], edUnitIn
                                    .getText().toString(),
                                    numSysUnits[toChoosenUnit], result);

                    break;
                case Converter.FORCE:

                    result = Converter.convertForce(inputValue, Unit.getSelectedForce(fromChoosenUnit),
                            Unit.getSelectedForce(toChoosenUnit));

                    edUnitOut.setText(result);

                    String[] units = thisContext
                            .getResources().getStringArray(R.array.forces);

                    // set up sharing msg
                    ConverterInterface.sharingMsg =
                            makeShare(units[fromChoosenUnit], edUnitIn
                                    .getText().toString(),
                                    units[toChoosenUnit], result);

                    break;
            }
        }
    }

    public static String convertLength(double lengthIn, int fromLength,
                                       int toLength) {
        return NumberFormat.getInstance().format(
                Length.getLength(lengthIn, fromLength, toLength));
    }

    public static double convertTemperature(double temperatureIn, int fromTemperature, int toTemperature) {
        double temperature = temperatureIn;

        switch (fromTemperature) {

            case CELSIUS:
                temperature = Temperature.celciusToKelvin(temperatureIn);
                break;
            case FAHRENHEIT:
                temperature = Temperature.fahrenheitToKelvin(temperatureIn);
                break;
            case REAUMUR:
                temperature = Temperature.reaumurToKelvin(temperatureIn);
                break;
            case RANKINE:
                temperature = Temperature.rankineToKelvin(temperatureIn);
                break;
            case NEWTON:
                temperature = Temperature.newtonToKelvin(temperatureIn);
                break;
            case DELISLE:
                temperature = Temperature.delisleToKelvin(temperatureIn);
                break;
            case ROMER:
                temperature = Temperature.romerToKelvin(temperatureIn);
                break;
            // doesn't need to convert to Kelvin since it's already Kelvin
        }

        // temparature is now Kelvin, so we can convert from it now
        switch (toTemperature) {

            case CELSIUS:
                return Math.round(Temperature.kelvinToCelcius(temperature));
            case FAHRENHEIT:
                return Math.round(Temperature.kelvinToFahrenheit(temperature));
            case REAUMUR:
                return Math.round(Temperature.kelvinToReaumur(temperature));
            case RANKINE:
                return Math.round(Temperature.kelvinToRankine(temperature));
            case NEWTON:
                return Math.round(Temperature.kelvinToNewton(temperature));
            case DELISLE:
                return Math.round(Temperature.kelvinToDelisle(temperature));
            case ROMER:
                return Math.round(Temperature.kelvinToRomer(temperature));

            // doesn't need to convert to Kelvin since it's already done in the
            // switch case above
        }

        return Math.round(temperature);

    }

    public static String convertNumericalSystem(String numberIn, int fromNumSys, int toNumSys) {
        String numberOut = "";

        try {
            switch (fromNumSys) {
                case DECIMAL:
                    switch (toNumSys) {
                        case DECIMAL:
                            numberOut = numberIn;
                            break;
                        case BINARY:
                            numberOut = NumericalSystem.convertDecimalToBinary(Long.valueOf(numberIn));
                            break;
                        case HEXADECIMAL:
                            numberOut = NumericalSystem.convertDecimalToHexadecimal(Long.valueOf(numberIn));
                            break;
                        case OCTAL:
                            numberOut = NumericalSystem.convertDecimalToOctal(Long.valueOf(numberIn));
                            break;
                    }
                    break;
                case BINARY:
                    switch (toNumSys) {
                        case DECIMAL:
                            numberOut = NumericalSystem.convertBinaryToDecimal(numberIn);
                            break;
                        case BINARY:
                            numberOut = numberIn;
                            break;
                        case HEXADECIMAL:
                            numberOut = NumericalSystem.convertBinaryToHexadecimal(numberIn);
                            break;
                        case OCTAL:
                            numberOut = NumericalSystem.convertBinaryToOctal(numberIn);
                            break;
                    }
                    break;
                case HEXADECIMAL:
                    switch (toNumSys) {
                        case DECIMAL:
                            numberOut = NumericalSystem.convertHexadecimalToDecimal(numberIn);
                            break;
                        case BINARY:
                            numberOut = NumericalSystem.convertHexadecimalToBinary(numberIn);
                            break;
                        case HEXADECIMAL:
                            numberOut = numberIn;
                            break;
                        case OCTAL:
                            numberOut = NumericalSystem.convertHexadecimalToOctal(numberIn);
                            break;
                    }
                    break;
                case OCTAL:
                    switch (toNumSys) {
                        case DECIMAL:
                            numberOut = NumericalSystem.convertOctalToDecimal(numberIn);
                            break;
                        case BINARY:
                            numberOut = NumericalSystem.convertOctalToBinary(numberIn);
                            break;
                        case HEXADECIMAL:
                            numberOut = NumericalSystem.convertOctalToHexadecimal(numberIn);
                            break;
                        case OCTAL:
                            numberOut = numberIn;
                            break;
                    }
                    break;
            }
        } catch (NumberFormatException e) {
            dumbassProove();
        }

        return numberOut;
    }

    public static String convertForce(double numberIn, int fromNumSys, int toNumSys) {
        double numberOut = numberIn;

        try {
            switch (fromNumSys) {
                case NEWTON_FORCE:
                    switch (toNumSys) {
                        case DYNE:
                            numberOut = Force.convertNewtonToDyne(numberIn);
                            break;
                        case KILOGRAM_FORCE:
                            numberOut = Force.convertNewtonToKilogramForce(numberIn);
                            break;
                        case POUND_FORCE:
                            numberOut = Force.convertNewtonToPoundForce(numberIn);
                            break;
                    }
                    break;
                case DYNE:
                    switch (toNumSys) {
                        case NEWTON_FORCE:
                            numberOut = Force.convertDyneToNewton(numberIn);
                            break;
                        case KILOGRAM_FORCE:
                            numberOut = Force.convertDyneToKilogramForce(numberIn);
                            break;
                        case POUND_FORCE:
                            numberOut = Force.convertDyneToPoundForce(numberIn);
                            break;
                    }
                    break;
                case KILOGRAM_FORCE:
                    switch (toNumSys) {
                        case NEWTON_FORCE:
                            numberOut = Force.convertKilogramForceToNewton(numberIn);
                            break;
                        case DYNE:
                            numberOut = Force.convertKilogramForceToDyne(numberIn);
                            break;
                        case POUND_FORCE:
                            numberOut = Force.convertKilogramForceToPoundForce(numberIn);
                            break;
                    }
                    break;
                case POUND_FORCE:
                    switch (toNumSys) {
                        case NEWTON_FORCE:
                            numberOut = Force.convertPoundForceToNewton(numberIn);
                            break;
                        case DYNE:
                            numberOut = Force.convertPoundForceToDyne(numberIn);
                            break;
                        case KILOGRAM_FORCE:
                            numberOut = Force.convertPoundForceToKilogramForce(numberIn);
                            break;
                    }
                    break;
            }
        } catch (NumberFormatException e) {
            dumbassProove();
        }

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(22);
        return nf.format(numberOut);
    }

    public static String convertCurrency(double amount, int fromCurrency,
                                         int toCurrency) throws IOException {

        double result;

        if (!CurrencyDatabase.exists()) {
            try {
                makeUpdateDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        result = amount * Currency.currencies[fromCurrency][toCurrency];

        String[] locales = thisContext.getResources()
                .getStringArray(R.array.currency_locales);

        Locale locale = new Locale(locales[toCurrency]);

        return NumberFormat.getCurrencyInstance(locale).format(result);
    }

    public static String convertByte(double byteIn, int fromByte, int toByte) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(22);
        return nf.format(Bytes.getByte(byteIn, fromByte, toByte));
    }
}
