package com.willh.wz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.security.MessageDigest;

public class MainActivity extends Activity {

    private final static String _mmessage_content = "";
    private final static int _mmessage_sdkVersion = 621086720;
    private final static String _mmessage_appPackage = "com.tencent.mm";

    private static final String URL = "https://open.weixin.qq.com/connect/app/qrconnect?appid=wx95a3a4d7c627e07d&bundleid=com.tencent.tmgp.sgame&scope=snsapi_base%2Csnsapi_userinfo%2Csnsapi_friend%2Csnsapi_message&state=weixin";

    private WebView mWebView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            if (mWebView != null) {
                mWebView.reload();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        mWebView = (WebView) findViewById(R.id.web_view);
        WebSettings settings = mWebView.getSettings();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 7.0; Mi-4c Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043632 Safari/537.36 MicroMessenger/6.6.1.1220(0x26060135) NetType/WIFI Language/zh_CN MicroMessenger/6.6.1.1220(0x26060135) NetType/WIFI Language/zh_CN miniProgram");
        mWebView.loadUrl(URL);
    }

    public class MyWebViewClient extends WebViewClient {
        MyWebViewClient() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            try {
                if (!TextUtils.isEmpty(url)) {
                    if (url.startsWith("wx95a3a4d7c627e07d://")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setComponent(new ComponentName("com.tencent.tmgp.sgame", "com.tencent.tmgp.sgame.wxapi.WXEntryActivity"));
                        intent.putExtras(generateBundle(url));
                        if (isIntentSafe(intent)) {
                            startActivity(intent);
                            return true;
                        }
                    } else if (!url.startsWith("http://") || !url.startsWith("https://")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        if (isIntentSafe(intent)) {
                            startActivity(intent);
                            return true;
                        }
                    }
                }
            } catch (Exception ignore) {
            }
            return false;
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            sslErrorHandler.proceed();
        }
    }

    private boolean isIntentSafe(Intent intent) {
        return getPackageManager()
                .queryIntentActivities(intent, 0).size() > 0;
    }

    private Bundle generateBundle(String req) {
        Bundle bundle = new Bundle();
        int indexOf = req.indexOf("&state=weixin");
        String code = indexOf >= 0 ? req.substring(req.indexOf("code=") + 5, indexOf) : "";
        bundle.putString("_message_token", null);
        bundle.putString("_wxapi_sendauth_resp_token", code);
        bundle.putString("_mmessage_appPackage", "com.tencent.mm");
        bundle.putString("_wxapi_baseresp_transaction", null);
        bundle.putString("_wxapi_sendauth_resp_lang", "zh_CN");
        bundle.putInt("_wxapi_command_type", 1);
        bundle.putString("_mmessage_content", _mmessage_content);
        bundle.putString("_wxapi_sendauth_resp_country", "CN");
        bundle.putByteArray("_mmessage_checksum", generateCheckSum(_mmessage_content, _mmessage_sdkVersion, _mmessage_appPackage));
        bundle.putString("wx_token_key", "com.tencent.mm.openapi.token");
        bundle.putString("_wxapi_sendauth_resp_url", req);
        bundle.putInt("_mmessage_sdkVersion", 621086720);
        bundle.putInt("_wxapi_baseresp_errcode", 0);
        bundle.putString("_wxapi_baseresp_errstr", null);
        bundle.putString("_wxapi_baseresp_openId", null);
        return bundle;
    }

    private byte[] generateCheckSum(String content, int sdkVersion, String appPackage) {
        String result = "";
        StringBuilder sb = new StringBuilder();
        if (content != null) {
            sb.append(content);
        }
        sb.append(sdkVersion);
        sb.append(appPackage);
        sb.append("mMcShCsTr");
        byte[] bytes = sb.substring(1, 9).getBytes();
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest instance = MessageDigest.getInstance(MD5Util.TAG);
            instance.update(bytes);
            byte[] digest = instance.digest();
            char[] cArr2 = new char[digest.length * 2];
            int i2 = 0;
            for (byte b : digest) {
                int i3 = i2 + 1;
                cArr2[i2] = cArr[(b >>> 4) & 15];
                i2 = i3 + 1;
                cArr2[i3] = cArr[b & 15];
            }
            result = new String(cArr2);
        } catch (Exception ignore) {
        }
        return result.getBytes();
    }

}