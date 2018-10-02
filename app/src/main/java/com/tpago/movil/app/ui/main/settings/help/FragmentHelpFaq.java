package com.tpago.movil.app.ui.main.settings.help;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.main.BaseMainFragment;

/**
 * @author rsuazo
 */

public class FragmentHelpFaq extends BaseMainFragment {

    public static FragmentHelpFaq create() {
        return new FragmentHelpFaq();
    }

    public WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_help_faq, container, false);
        mWebView = v.findViewById(R.id.faqwebview);
        mWebView.loadUrl(getString(R.string.tpagotermconditions));

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.setWebViewClient(new WebViewClient());

        return v;
    }

    @Override
    @StringRes
    protected int titleResId() { return R.string.faq; }

    @Override
    protected String subTitle() {
        return "";
    }

    @Override
    @LayoutRes
    protected int layoutResId() {
        return R.layout.fragment_help_faq;
    }


}
