package com.dikelito.schedule;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class wvClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView v, String url){
        v.loadUrl(url);
        return true;
    }
}
