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
package eu.funceptionapps.convertitall.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import eu.funceptionapps.convertitall.R;

public class UpdateDialog {

    private static int onlineVersionCode;
    private static int localVersionCode;
    private static Boolean isStartUp = false;

    private static ProgressDialog pbarUpdate;
    public static String updatedChangelog = "";

    private static final String VERSION_CODE_URL = "http://db.tt/LRBxESk4";
    private static final String CHANGELOG_URL = "http://db.tt/ORXRm0IP";
    private static final String UPDATE_URL_BODY = "http://holoconverter.googlecode.com/files/ConvertItAll_";
    private static final String APK_EXTENSION = ".apk";
    private static final String XDAURL = "http://forum.xda-developers.com/showthread.php?t=1960295";

    public static class UpdateCheckTask extends
            AsyncTask<Boolean, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... params) {

            // check if it's on start up, so user don't get annoyed by the Toast
            // everytime they start the application

            isStartUp = params[0];

            try {
                onlineVersionCode = downloadVersionCode();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (onlineVersionCode > localVersionCode) {
                try {
                    updatedChangelog = getUrlContent(CHANGELOG_URL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            localVersionCode = getVersionCode(ConverterInterface.thisContext);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                setUpDialog(ConverterInterface.thisContext, updatedChangelog);
            } else {
                if (!isStartUp) {
                    Toast.makeText(ConverterInterface.thisContext,
                            "You already have the newest version",
                            Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    public static void setUpDialog(Context context, String updatedChangelog) {
        Dialog updateDialog = new Dialog(context);
        updateDialog.setContentView(R.layout.new_version_dialog);
        updateDialog.setTitle(ConverterInterface.thisContext
                .getString(R.string.title_update_dialog));
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);

        TextView changelog = (TextView) updateDialog
                .findViewById(R.id.txt_new_changelog);
        changelog.setText(updatedChangelog);

        Button btUpdate = (Button) updateDialog.findViewById(R.id.bt_update);

        btUpdate.setOnClickListener(OnUpdateClick);

        Button btThread = (Button) updateDialog.findViewById(R.id.bt_thread);

        btThread.setOnClickListener(OnThreadClick);

        updateDialog.show();
    }

    public static OnClickListener OnUpdateClick = new OnClickListener() {

        public void onClick(android.view.View v) {
            pbarUpdate = new ProgressDialog(ConverterInterface.thisContext);
            pbarUpdate.setIndeterminate(false);
            pbarUpdate.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pbarUpdate.setMessage("Downloading new version...");
            pbarUpdate.setMax(100);

            String updateUrl = UPDATE_URL_BODY + onlineVersionCode
                    + APK_EXTENSION;
            new DownloadNewVersion().execute(updateUrl);

        }
    };

    public static OnClickListener OnThreadClick = new OnClickListener() {

        public void onClick(android.view.View v) {
            Uri url = Uri.parse(XDAURL);
            Intent openThread = new Intent(Intent.ACTION_VIEW, url);
            ConverterInterface.thisContext.startActivity(openThread);
        }
    };

    public static String getUrlContent(String sUrl) throws IOException {
        String changelog = new String();
        URL url = new URL(sUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);
        connection.connect();

        InputStream in = connection.getInputStream();

        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = rd.readLine()) != null) {
            changelog = changelog + line + "\n";
        }
        return changelog;

    }

    private static class DownloadNewVersion extends
            AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... sUrl) {
            URL url = null;
            try {
                url = new URL(sUrl[0]);
            } catch (MalformedURLException e4) {
                e4.printStackTrace();
            }
            URLConnection connection = null;
            try {
                connection = url.openConnection();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            try {
                connection.connect();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            // this will be useful so that you can show a typical 0-100%
            // progress bar
            int fileLength = connection.getContentLength();

            // download the file
            InputStream input = null;
            try {
                input = new BufferedInputStream(url.openStream());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            String path = Environment.getExternalStorageDirectory()
                    + "/ConvertItAll/" + "ConvertItAll_" + onlineVersionCode
                    + APK_EXTENSION;

            OutputStream output = null;
            try {
                output = new FileOutputStream(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            try {
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                output.flush();
                output.close();
                input.close();
            } catch (Exception e5) {

            }

            pbarUpdate.dismiss();
            return path;
        }

        @SuppressLint("NewApi")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Environment.getExternalStoragePublicDirectory("ConvertItAll")
                    .mkdirs();
            pbarUpdate.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            pbarUpdate.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Intent promptInstall = new Intent(Intent.ACTION_VIEW);
            File convertitall = new File(result);

            promptInstall.setDataAndType(Uri.fromFile(convertitall),
                    "application/vnd.android.package-archive");
            ConverterInterface.thisContext.startActivity(promptInstall);
            super.onPostExecute(result);
        }

    }

    public static int downloadVersionCode() throws IOException {
        String str = new String();
        URL url = new URL(VERSION_CODE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);
        connection.connect();

        InputStream in = connection.getInputStream();

        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = rd.readLine()) != null) {
            str += line;
        }

        str = str.replace("\n", "");
        return Integer.valueOf(str);
    }

    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException ex) {
        }
        return 0;
    }
}
