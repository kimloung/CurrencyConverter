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

public class DAO_item {

    public List<String> get_items()
    {
        HttpGetTask hp = new HttpGetTask();

        try {
            return hp.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {

        private String textUrl = "https://ghs.fxexchangerate.com/rss.xml";

        public HttpGetTask() {

        }

        @Override
        protected List<String> doInBackground(Void... arg0) {
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

        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);

        }

    }

    private List<String> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        boolean isItem = false;
        List<String> items = new ArrayList<>();

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

                if (name.equalsIgnoreCase("title")) {
                    title = result.substring(19);
                }

                if (title != null) {
                    if (isItem) {

                        items.add(
                                title.substring(0,title.length()-5)+
                                        " - "+
                                        title.substring(title.length()-4,title.length()-1)
                                );
                    }

                    title = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }

    }
}
