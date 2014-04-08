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

public class Length {

    /**
     * array with all the formules to convert between the different lengths
     * formules in order of numbers
     */

    public static double getLength(double in, int fromLength, int toLength) {
        double[][] length_formula = {
                {in, in * 1000, in * 10000, in * 100000, in * 1000000,
                        in * 10000000, in * 100000000, in * 0.621371192,
                        in * 1093.6133, in * 3280.8399, in * 39370.0787, in * 0.54},
                {in * 0.001, in, in * 10, in * 100, in * 1000, in * 10000,
                        in * 100000, in * 0.000621371192, in * 1.0936133,
                        in * 3.2808399, in * 39.3700787, in * 0.00054},
                {in * 0.0001, in * 0.1, in, in * 10, in * 100, in * 1000,
                        in * 10000, in * 0.0000621371192, in * 0.10936133,
                        in * 0.32808399, in * 3.93700787, in * 0.000054},
                {in * 0.00001, in * 0.01, in * 0.1, in, in * 10, in * 100,
                        in * 1000, in * 0.00000621371192, in * 0.010936133,
                        in * 0.032808399, in * 0.393700787, in * 0.0000054},
                {in * 0.000001, in * 0.001, in * 0.01, in * 0.1, in, in * 10,
                        in * 100, in * 0.000000621371192, in * 0.0010936133,
                        in * 0.0032808399, in * 0.0393700787, in * 0.00000054},
                {in * 0.0000001, in * 0.0001, in * 0.001, in * 0.01, in * 0.1,
                        in, in * 10, in * 0.0000000621371192,
                        in * 0.00010936133, in * 0.00032808399,
                        in * 0.00393700787, in * 0.000000054},
                {in * 0.00000001, in * 0.00001, in * 0.0001, in * 0.001,
                        in * 0.01, in * 0.1, in, in * 0.00000000621371192,
                        in * 0.000010936133, in * 0.000032808399,
                        in * 0.000393700787, in * 0.0000000054},
                {in * 1.609344, in * 1609.344, in * 16093.44, in * 160934.4,
                        in * 1609344, in * 16093440, in * 160934400, in,
                        in * 1760, in * 5280, in * 63360, in * 0.869},
                {in * 0.0009144, in * 0.9144, in * 9.144, in * 91.44,
                        in * 914.4, in * 9144, in * 91440, in * 0.000568181818,
                        in, in * 3, in * 36, in * 0.0004937},
                {in * 0.0003048, in * 0.3048, in * 3.048, in * 30.48,
                        in * 304.8, in * 3048, in * 30480, in * 0.000189393939,
                        in / 3, in, in * 12, in * 0.00016458},
                {in * 0.0000254, in * 0.0254, in * 0.254, in * 254, in * 2.54,
                        in * 25.4, in * 254, in * 0.0000157828283,
                        in * 0.0277777778, in * 0.0833333333, in, in * 0.000013714903},
                {in * 1.852, in * 1852, in * 18520, in * 185200, in * 1852000,
                        in * 18520000, in * 185200000, in * 1.151,
                        in * 2025.372, in * 6076.115, in * 72913.386, in}

        };
        return length_formula[fromLength][toLength];
    }
}