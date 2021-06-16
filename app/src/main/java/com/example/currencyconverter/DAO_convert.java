package com.example.currencyconverter;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DAO_convert {


    public String getValue(String fr,String to)
    {
        HttpGetTaskValue hp=new HttpGetTaskValue();
        try {
            String tmp= hp.execute("https://"+fr+".fxexchangerate.com/"+to+".xml").get();

            return tmp;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class HttpGetTaskValue extends AsyncTask<String, Void, String> {

        public HttpGetTaskValue() {
        }

        @Override
        protected String doInBackground(String... str) {
            String textUrl = str[0];
            InputStream in = null;
            try {
                URL url = new URL(textUrl);
                in = url.openConnection().getInputStream();
                return parseFeed(in);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }

    }

    private String parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String description = null;
        boolean isItem = false;

        try {

            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }


                if (name.equalsIgnoreCase("description") && isItem) {

                    Log.i("oke" + isItem+"   "+result,"========>");
                    result.toLowerCase();
                    int sta=result.indexOf("=");
                    int en=result.indexOf("<br");
                    return result.substring(sta+1,en-3);

                }


            }

            return null;
        } finally {
            inputStream.close();
        }

    }
}
