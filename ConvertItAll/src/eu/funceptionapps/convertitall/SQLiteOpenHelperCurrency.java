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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteOpenHelperCurrency extends SQLiteOpenHelper {

    public static final String TABLE_CURRENCIES = "currencies";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CURRENCY = "currency";
    public static final String COLUMN_CONVERSION = "conversion";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";

    private static final String DATABASE_NAME = "currencies.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    public static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_CURRENCIES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CURRENCY
            + " VARCHAR(8), " + COLUMN_CONVERSION + " FLOAT(20), "
            + COLUMN_DATE + " VARCHAR(10), " + COLUMN_TIME + " VARCHAR(7));";

    public SQLiteOpenHelperCurrency(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
