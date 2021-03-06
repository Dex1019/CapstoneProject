package com.dex.redditreader.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.dex.redditreader.MyApplication;
import com.dex.redditreader.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.http.oauth.OAuthHelper;

import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    public static final Credentials CREDENTIALS = Credentials.installedApp("ZdlKS-zCNQPKOg",
            "https://dex1019.github.io/");

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();

        final OAuthHelper helper = AuthenticationManager.get().getRedditClient().getOAuthHelper();

        String[] scopes = {"identity", "read", "subscribe", "mysubreddits", "vote"};

        final URL authorizationUrl = helper.getAuthorizationUrl(CREDENTIALS, true, true, scopes);
        final WebView webView = findViewById(R.id.webview);

        // Load the authorization URL into the browser
        webView.loadUrl(authorizationUrl.toExternalForm());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains("code=")) {
                    // detected the redirect URL
                    onUserChallenge(url, CREDENTIALS);
                } else if (url.contains("error=")) {
                    Toast.makeText(LoginActivity.this, R.string.must_login, Toast.LENGTH_SHORT).show();
                    webView.loadUrl(authorizationUrl.toExternalForm());
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void onUserChallenge(final String url, final Credentials creds) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    OAuthData data = AuthenticationManager.get().getRedditClient().getOAuthHelper().onUserChallenge(params[0], creds);
                    AuthenticationManager.get().getRedditClient().authenticate(data);
                    return AuthenticationManager.get().getRedditClient().getAuthenticatedUser();
                } catch (NetworkException | OAuthException | IllegalStateException e) {
                    Log.e(TAG, "Could not log in", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                LoginActivity.this.finish();
            }
        }.execute(url);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mTracker.setScreenName(getString(R.string.login_screen));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
