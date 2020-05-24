package com.mono40.movil.app.ui.main.settings;

import android.content.Intent;
import android.net.Uri;

import com.mono40.movil.BuildConfig;
import com.mono40.movil.R;
import com.mono40.movil.app.ui.fragment.base.FragmentBase;
import com.mono40.movil.d.ui.main.DepMainActivityBase;
import com.mono40.movil.util.StringHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hecvasro
 */
public final class FragmentAbout extends FragmentBase {

    public static FragmentAbout create() {
        return new FragmentAbout();
    }

    @BindView(R.id.setting_option_version)
    MultiLineSettingsOption versionOption;

    @Override
    protected int layoutResId() {
        return R.layout.fragment_about;
    }

    private final void openLinkIntent(String URL) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        startActivity(i);
    }

    @OnClick(R.id.setting_option_mail_office)
    final void onEmailPressed() {
        String URL = getString(R.string.tpagoemail);
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + URL));
        startActivity(i);
    }

    @OnClick(R.id.setting_option_call_office)
    final void onPhonePressed() {
        String URL = getString(R.string.tpagophone);
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + URL));
        startActivity(i);
    }

    @OnClick(R.id.setting_option_site)
    final void onSitePressed() {
        String URL = getString(R.string.tpagoweb);
        openLinkIntent(URL);
    }

    @OnClick(R.id.linear_layout_twitter)
    final void onTwitterPressed() {
        String URL = getString(R.string.tpagotwitter);
        openLinkIntent(URL);
    }

    @OnClick(R.id.linear_layout_facebook)
    final void onFacebookPressed() {
        String URL = getString(R.string.tpagofacebook);
        openLinkIntent(URL);
    }

    @OnClick(R.id.linear_layout_instagram)
    final void onInstagramPressed() {
        String URL = getString(R.string.tpagoinstagram);
        openLinkIntent(URL);
    }

    @OnClick(R.id.linear_layout_snapchat)
    final void onSnapchatPressed() {
        String URL = getString(R.string.tpagosnapchat);
        openLinkIntent(URL);
    }

    @OnClick(R.id.linear_layout_youtube)
    final void onYoutubePressed() {
        String URL = getString(R.string.tpagoyoutube);
        openLinkIntent(URL);
    }

    @Override
    public void onStart() {
        super.onStart();

        final String title = StringHelper.builder()
                .append(this.getString(R.string.about))
                .append(' ')
                .append("tPago") // TODO: Add app name to BuildConfig.
                .toString();
        DepMainActivityBase.get(this.getActivity())
                .toolbar()
                .setTitle(title);
        versionOption
                .secondaryText(BuildConfig.VERSION_NAME);
    }
}
