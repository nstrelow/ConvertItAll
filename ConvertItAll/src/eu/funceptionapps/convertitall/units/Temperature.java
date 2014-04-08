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
package eu.funceptionapps.convertitall.units;

public class Temperature {

    /**
     * methods to convert from any degree unit to Kelvin
     */

    // Method to convert from degrees Celcius to degrees Kelvin
    public static double celciusToKelvin(double degCelcius) {
        double degKelvin;
        degKelvin = degCelcius + 273.15;
        return degKelvin;
    }

    // Method to convert from degrees Fahrenheit to degrees Kelvin
    public static double fahrenheitToKelvin(double degFahrenheit) {
        double degKelvin;
        degKelvin = (degFahrenheit + 459.67) * (5.0 / 9.0);
        return degKelvin;
    }

    // Method to convert from degrees Réaumur to degrees Kelvin
    public static double reaumurToKelvin(double degReaumur) {
        double degKelvin;
        degKelvin = degReaumur * 1.25 + 273.15;
        return degKelvin;
    }

    // Method to convert from degrees Rankine to degrees Kelvin
    public static double rankineToKelvin(double degRankine) {
        double degKelvin;
        degKelvin = degRankine * (5.0 / 9.0);
        return degKelvin;
    }

    // Method to convert from degrees Newton to degrees Kelvin
    public static double newtonToKelvin(double degNewton) {
        double degKelvin;
        degKelvin = degNewton * (100.0 / 33.0) + 273.15;
        return degKelvin;
    }

    // Method to convert from degrees Delisle to degrees Kelvin
    public static double delisleToKelvin(double degDelisle) {
        double degKelvin;
        degKelvin = 373.15 - degDelisle * (2.0 / 3.0);
        return degKelvin;
    }

    // Method to convert from degrees Rømer to degrees Kelvin
    public static double romerToKelvin(double degRomer) {
        double degKelvin;
        degKelvin = (degRomer - 7.5) * (40.0 / 21.0) + 273.15;
        return degKelvin;
    }

    /**
     * methods to convert from Kelvin to any degree unit
     */

    // Method to convert from degrees Kelvin to degrees Celcius
    public static double kelvinToCelcius(double degKelvin) {
        double degCelcius;
        degCelcius = degKelvin - 273.15;
        return degCelcius;
    }

    // Method to convert from degrees Kelvin to degrees Fahrenheit
    public static double kelvinToFahrenheit(double degKelvin) {
        double degFahrenheit;
        degFahrenheit = (degKelvin * 1.8) - 459.67;
        return degFahrenheit;
    }

    // Method to convert from degrees Kelvin to degrees Réaumur
    public static double kelvinToReaumur(double degKelvin) {
        double degReaumur;
        degReaumur = (degKelvin - 273.15) * 0.8;
        return degReaumur;
    }

    // Method to convert from degrees Kelvin to degrees Rankine
    public static double kelvinToRankine(double degKelvin) {
        double degRankine;
        degRankine = degKelvin * 1.8;
        return degRankine;
    }

    // Method to convert from degrees Kelvin to degrees Newton
    public static double kelvinToNewton(double degKelvin) {
        double degNewton;
        degNewton = (degKelvin - 273.15) * 0.33;
        return degNewton;
    }

    // Method to convert from degrees Kelvin to degrees Delisle
    public static double kelvinToDelisle(double degKelvin) {
        double degDelisle;
        degDelisle = (373.15 - degKelvin) * 1.5;
        return degDelisle;
    }

    // Method to convert from degrees Kelvin to degrees Rømer
    public static double kelvinToRomer(double degKelvin) {
        double degRomer;
        degRomer = (degKelvin - 273.15) * (21.0 / 40.0) + 7.5;
        return degRomer;
    }

}
