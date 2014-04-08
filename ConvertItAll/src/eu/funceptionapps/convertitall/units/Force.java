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

import javax.measure.Measure;
import javax.measure.converter.UnitConverter;
import static javax.measure.unit.NonSI.*;
import static javax.measure.unit.SI.*;

/**
 * Created by djnilse on 11.11.13.
 */
public class Force {

    public static double convertNewtonToDyne(double value) {
        UnitConverter converter = NEWTON.getConverterTo(DYNE);
        double out = converter.convert(Measure.valueOf(value, NEWTON).doubleValue(NEWTON));
        return out;
    }

    public static double convertNewtonToPoundForce(double value) {
        UnitConverter converter = NEWTON.getConverterTo(POUND_FORCE);
        double out = converter.convert(Measure.valueOf(value, NEWTON).doubleValue(NEWTON));
        return out;
    }

    public static double convertNewtonToKilogramForce(double value) {
        UnitConverter converter = NEWTON.getConverterTo(KILOGRAM_FORCE);
        double out = converter.convert(Measure.valueOf(value, NEWTON).doubleValue(NEWTON));
        return out;
    }

    public static double convertDyneToNewton(double value) {
        UnitConverter converter = DYNE.getConverterTo(NEWTON);
        double out = converter.convert(Measure.valueOf(value, DYNE).doubleValue(DYNE));
        return out;
    }

    public static double convertDyneToPoundForce(double value) {
        UnitConverter converter = DYNE.getConverterTo(POUND_FORCE);
        double out = converter.convert(Measure.valueOf(value, DYNE).doubleValue(DYNE));
        return out;
    }

    public static double convertDyneToKilogramForce(double value) {
        UnitConverter converter = DYNE.getConverterTo(KILOGRAM_FORCE);
        double out = converter.convert(Measure.valueOf(value, DYNE).doubleValue(DYNE));
        return out;
    }

    public static double convertPoundForceToDyne(double value) {
        UnitConverter converter = POUND_FORCE.getConverterTo(DYNE);
        double out = converter.convert(Measure.valueOf(value, POUND_FORCE).doubleValue(POUND_FORCE));
        return out;
    }

    public static double convertPoundForceToKilogramForce(double value) {
        UnitConverter converter = POUND_FORCE.getConverterTo(KILOGRAM_FORCE);
        double out = converter.convert(Measure.valueOf(value, POUND_FORCE).doubleValue(POUND_FORCE));
        return out;
    }

    public static double convertPoundForceToNewton(double value) {
        UnitConverter converter = POUND_FORCE.getConverterTo(NEWTON);
        double out = converter.convert(Measure.valueOf(value, POUND_FORCE).doubleValue(POUND_FORCE));
        return out;
    }

    public static double convertKilogramForceToNewton(double value) {
        UnitConverter converter = KILOGRAM_FORCE.getConverterTo(NEWTON);
        double out = converter.convert(Measure.valueOf(value, KILOGRAM_FORCE).doubleValue(KILOGRAM_FORCE));
        return out;
    }

    public static double convertKilogramForceToDyne(double value) {
        UnitConverter converter = KILOGRAM_FORCE.getConverterTo(DYNE);
        double out = converter.convert(Measure.valueOf(value, KILOGRAM_FORCE).doubleValue(KILOGRAM_FORCE));
        return out;
    }

    public static double convertKilogramForceToPoundForce(double value) {
        UnitConverter converter = KILOGRAM_FORCE.getConverterTo(POUND_FORCE);
        double out = converter.convert(Measure.valueOf(value, KILOGRAM_FORCE).doubleValue(KILOGRAM_FORCE));
        return out;
    }

}
