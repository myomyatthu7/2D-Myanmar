package com.android.google.mmt.twodmyanmar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LiveData {

    public interface DocumentCallback {
        void onDocumentReceived(Document document);

        void onError(Exception e);
    }
    public void fetchData(String url, DocumentCallback callback) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();
                    callback.onDocumentReceived(doc);
                } catch (IOException e) {
                    callback.onError(e);
                }
            }
        });
    }
}
