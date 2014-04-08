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

public class Bytes {

    /**
     * array with all the formules to convert between the different bytes
     * formules in order of numbers
     */

    public static double getByte(double in, int fromByte, int toByte) {
        double[][] byte_powers_of_two = {
                {0, -3, -13, -23, -33, -43, -53, -63, -73},
                {3, 0, -10, -20, -30, -40, -50, -60, -70},
                {13, 10, 0, -10, -20, -30, -40, -50, -60},
                {23, 20, 10, 0, -10, -20, -30, -40, -50},
                {33, 30, 20, 10, 0, -10, -20, -30, -40},
                {43, 40, 30, 20, 10, 0, -10, -20, -30},
                {53, 50, 40, 30, 20, 10, 0, -10, -20},
                {63, 60, 50, 40, 30, 20, 10, 0, -10},
                {73, 70, 60, 50, 40, 30, 20, 10, 0}};
        return in * Math.pow(2, byte_powers_of_two[fromByte][toByte]);
    }
}