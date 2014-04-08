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

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import eu.funceptionapps.convertitall.Converter;
import eu.funceptionapps.convertitall.R;
import eu.funceptionapps.convertitall.SQLiteOpenHelperCurrency;
import eu.funceptionapps.convertitall.ui.ConverterInterface;

public class Currency {

    public final static String liveCurrencyUrl = "http://finance.yahoo.com/d/quotes.csv?e=.csv&f=sl1d1t1";
    public final static String currencyCodes[] = ConverterInterface.thisContext
            .getResources().getStringArray(R.array.currencies);

    public final static int allRates = currencyCodes.length
            * currencyCodes.length;

    public static double[][] currencies = new double[currencyCodes.length][currencyCodes.length];

    public static ProgressDialog pbarDialog;

    public static SQLiteOpenHelperCurrency sqliteHelper;
    public static SQLiteDatabase db;

    private static String[] allColumns = {SQLiteOpenHelperCurrency.COLUMN_ID,
            SQLiteOpenHelperCurrency.COLUMN_CURRENCY,
            SQLiteOpenHelperCurrency.COLUMN_CONVERSION,
            SQLiteOpenHelperCurrency.COLUMN_DATE,
            SQLiteOpenHelperCurrency.COLUMN_TIME};

    @SuppressLint("HandlerLeak")
    public static void updateCurrencies() throws Exception {

		/*
         * first checking if internet is available and NOT roaming
		 */
        if (InternetAvailable(ConverterInterface.thisContext)) {

            sqliteHelper = new SQLiteOpenHelperCurrency(
                    ConverterInterface.thisContext);

            db = sqliteHelper.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS "
                    + SQLiteOpenHelperCurrency.TABLE_CURRENCIES);
            db.execSQL(SQLiteOpenHelperCurrency.DATABASE_CREATE);

            db.execSQL("SELECT * FROM currencies");

            initProgressBarDialogUpdate();

            Thread createUpdatedCurrencies = new Thread(new Runnable() {

                public void run() {

                    Message msg;

                    String[] currencyCSV = new String[allRates];
                    ArrayList<String> list = new ArrayList<String>();
                    try {
                        list = getCurrencyCSV();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i <= list.size() - 1; i++) {
                        currencyCSV[i] = list.get(i);
                    }

                    Cursor cursor = null;
                    try {
                        for (int n = 0; n <= allRates - 1; n++) {

							/*
                             * sending handler an message to increment the
							 * ProgressBarDialogcontaining by 1
							 */

                            msg = new Message();

                            msg.obj = "1";
                            handler.sendMessage(msg);

                            String sqlFormatedCurrency[] = currencyCSV[n]
                                    .replace("\"", "").split(",");

                            ContentValues values = new ContentValues();
                            values.put(
                                    SQLiteOpenHelperCurrency.COLUMN_CONVERSION,
                                    sqlFormatedCurrency[0]);
                            values.put(
                                    SQLiteOpenHelperCurrency.COLUMN_CURRENCY,
                                    sqlFormatedCurrency[1]);
                            values.put(SQLiteOpenHelperCurrency.COLUMN_DATE,
                                    sqlFormatedCurrency[2]);
                            values.put(SQLiteOpenHelperCurrency.COLUMN_TIME,
                                    sqlFormatedCurrency[3]);
                            long insertId = db.insert(
                                    SQLiteOpenHelperCurrency.TABLE_CURRENCIES,
                                    null, values);
                            cursor = db.query(
                                    SQLiteOpenHelperCurrency.TABLE_CURRENCIES,
                                    allColumns,
                                    SQLiteOpenHelperCurrency.COLUMN_ID + " = "
                                            + insertId, null, null, null, null);
                            cursor.moveToFirst();

                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }

                    // db.close();
                    sqliteHelper.close();

                    msg = new Message();
                    msg.obj = "0";
                    handler.sendMessage(msg);

                }

                public Handler handler = new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                        int cases = Integer.valueOf(String.valueOf(msg.obj));

                        switch (cases) {
                            case 1:
                                pbarDialog.setMessage("Updating database...");
                                pbarDialog.incrementProgressBy(1);
                                break;
                            case 2:

                                break;
                            case 0:
                                pbarDialog.dismiss();
                                // load the currencies in the integer array
                                new LoadCurrenciesDBTask().execute("");
                                break;
                        }
                    }
                };
            });

            createUpdatedCurrencies.start();
        }
    }

    /*
     * initialize the ProgressBarDialog showing the updated currencies progress
     */
    private static void initProgressBarDialogUpdate() {

        pbarDialog = new ProgressDialog(ConverterInterface.thisContext);

        pbarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pbarDialog.setCancelable(false);
        pbarDialog.setProgress(0);
        pbarDialog.setTitle("Updating Currencies...");
        pbarDialog.setMessage("Initializing update...");
        pbarDialog.setMax(allRates);
        pbarDialog.show();
    }

    private static void initProgressBarDialogLoad() {

        pbarDialog = new ProgressDialog(ConverterInterface.thisContext);
        pbarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pbarDialog.setCancelable(false);
        pbarDialog.setTitle("Loading Database...");
        pbarDialog.setMessage("Loading Currencies...");
        pbarDialog.show();
    }

    public static class LoadCurrenciesDBTask extends
            AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... sUrl) {
            sqliteHelper = new SQLiteOpenHelperCurrency(
                    ConverterInterface.thisContext);

            String[] code = new String[1];
            db = sqliteHelper.getReadableDatabase();
            Cursor cursor = null;
            try {
                for (int n = 0; n <= currencyCodes.length - 1; n++) {
                    for (int k = 0; k <= currencyCodes.length - 1; k++) {
                        code[0] = String.valueOf(n * (currencyCodes.length) + k
                                + 1);
                        cursor = db.rawQuery(
                                "SELECT currency from currencies WHERE _id=?",
                                code);
                        cursor.moveToFirst();
                        currencies[n][k] = Double.valueOf(cursor
                                .getString(cursor.getColumnIndex("currency")));
                        cursor.close();
                    }
                }
            } catch (CursorIndexOutOfBoundsException e) {
                Converter.CurrencyDatabase.delete();
            } finally {
                if (cursor != null)
                    cursor.close();
            }

            publishProgress(0);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            initProgressBarDialogLoad();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pbarDialog.dismiss();
        }

    }

    public static ArrayList<String> getCurrencyCSV() throws IOException {

        String url = liveCurrencyUrl;
        ArrayList<String> cvs = new ArrayList<String>();
        int i = 0;

        for (int n = 0; n <= currencyCodes.length - 1; n++) {
            for (int k = 0; k <= currencyCodes.length - 1; k++) {
                url += "&s=" + currencyCodes[n] + currencyCodes[k] + "=X";

                if (i == 5) {
                    cvs.addAll(getUrlContent(url));
                    cvs.trimToSize();
                    url = liveCurrencyUrl;
                    i = 0;
                }
            }
            i++;
        }
        cvs.addAll(getUrlContent(url));
        cvs.trimToSize();
        return cvs;

    }

    public static ArrayList<String> getUrlContent(String sUrl)
            throws IOException {
        ArrayList<String> list = new ArrayList<String>();
        URL url = new URL(sUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        connection.setDoOutput(true);
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);
        connection.connect();

        InputStream in = connection.getInputStream();
        if ("gzip".equals(connection.getContentEncoding()))
            in = new GZIPInputStream(in);

        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = rd.readLine()) != null) {
            list.add(line);
        }
        return list;

    }

    public static boolean InternetAvailable(Context context) {

        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();

        if (info == null || !info.isConnected()) {
            return false;
        }
        return !info.isRoaming();
    }

}
