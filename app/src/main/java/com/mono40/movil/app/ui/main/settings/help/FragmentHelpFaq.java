package com.mono40.movil.app.ui.main.settings.help;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mono40.movil.R;
import com.mono40.movil.app.ui.main.BaseMainFragment;
import com.mono40.movil.d.ui.view.widget.FullScreenLoadIndicator;
import com.mono40.movil.d.ui.view.widget.LoadIndicator;
import com.mono40.movil.util.ObjectHelper;

/**
 * @author rsuazo
 */

public class FragmentHelpFaq extends BaseMainFragment {

    public static FragmentHelpFaq create() {
        return new FragmentHelpFaq();
    }

    public WebView mWebView;

    private LoadIndicator loadIndicator;
    private LoadIndicator fullScreenLoadIndicator;
    private LoadIndicator currentLoadIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_help_faq, container, false);
        mWebView = v.findViewById(R.id.faqwebview);
        mWebView.loadUrl(getString(R.string.tpagotermconditions));

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoadingIndicator();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoadIndicator();
            }
        });

        return v;
    }

    private void dismissLoadIndicator() {
        if (ObjectHelper.isNotNull(currentLoadIndicator)) {
            currentLoadIndicator.hide();
            currentLoadIndicator = null;
        }
    }

    private void showLoadingIndicator() {
        if (ObjectHelper.isNull(fullScreenLoadIndicator)) {
            fullScreenLoadIndicator = new FullScreenLoadIndicator(getChildFragmentManager());
        }
        currentLoadIndicator = fullScreenLoadIndicator;
        currentLoadIndicator.show();
    }

    @Override
    @StringRes
    protected int titleResId() {
        return R.string.faq;
    }

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
