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

public class NumericalSystem {

    public static String convertDecimalToBinary(long dec) {
        return Long.toBinaryString(dec);
    }

    public static String convertDecimalToOctal(long dec) {
        return Long.toOctalString(dec);
    }

    public static String convertDecimalToHexadecimal(long dec) {
        return Long.toHexString(dec);
    }

    public static String convertBinaryToDecimal(String number) {
        return String.valueOf(Integer.parseInt(number, 2));
    }

    public static String convertBinaryToOctal(String number) {
        return Integer.toOctalString(Integer.parseInt(number, 2));
    }

    public static String convertBinaryToHexadecimal(String number) {
        return Integer.toHexString(Integer.parseInt(number, 2));
    }

    public static String convertOctalToDecimal(String number) {
        return String.valueOf(Integer.parseInt(number, 8));
    }

    public static String convertOctalToBinary(String number) {
        return Integer.toBinaryString(Integer.parseInt(number, 8));
    }

    public static String convertOctalToHexadecimal(String number) {
        return Integer.toHexString(Integer.parseInt(number, 8));
    }

    public static String convertHexadecimalToDecimal(String number) {
        return String.valueOf(Integer.parseInt(number, 16));
    }

    public static String convertHexadecimalToBinary(String number) {
        return Integer.toBinaryString(Integer.parseInt(number, 16));
    }

    public static String convertHexadecimalToOctal(String number) {
        return Integer.toOctalString(Integer.parseInt(number, 16));
    }

}
