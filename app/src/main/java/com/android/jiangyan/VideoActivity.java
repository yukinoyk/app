package com.android.jiangyan;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    private WebView webView;
    private View mCustomView; // 用于全屏渲染视频的View
    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 让 Activity 变成全屏模式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video);

        // 初始化 WebView
        webView = findViewById(R.id.webView);

        // 配置 WebView 设置
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false); // 允许自动播放
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        // 确保 WebView 在应用内打开，而不是跳转到浏览器
        webView.setWebViewClient(new WebViewClient());

        // 设置 WebChromeClient，用于处理全屏视频
        webView.setWebChromeClient(new WebChromeClient() {

            // 处理全屏展示
            @Override
            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
                if (mCustomViewCallback != null) {
                    mCustomViewCallback.onCustomViewHidden();
                    mCustomViewCallback = null;
                    return;
                }

                // 强制横屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                // 设置全屏模式
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                // 隐藏原本的 WebView 父视图，显示全屏视图
                ViewGroup parent = (ViewGroup) webView.getParent().getParent();
                parent.setVisibility(View.GONE);
                ((ViewGroup) parent.getParent()).addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                mCustomView = view;
                mCustomViewCallback = customViewCallback;
            }

            // 处理取消全屏
            @Override
            public void onHideCustomView() {
                if (mCustomView != null) {
                    if (mCustomViewCallback != null) {
                        mCustomViewCallback.onCustomViewHidden();
                        mCustomViewCallback = null;
                    }

                    // 退出全屏模式
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                    // 恢复竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                    // 恢复原本的 WebView 父视图
                    if (mCustomView != null && mCustomView.getParent() != null) {
                        ViewGroup parent = (ViewGroup) mCustomView.getParent();
                        parent.removeView(mCustomView);

                        // 显示 WebView 父视图
                        if (webView.getParent().getParent() != null) {
                            ViewGroup parent2 = (ViewGroup) webView.getParent().getParent();
                            parent2.setVisibility(View.VISIBLE);
                        }
                    }
                    mCustomView = null;
                }
            }
        });

        // 获取传递过来的视频链接
        String videoUrl = getIntent().getStringExtra("videoUrl");
        if (videoUrl != null) {
            webView.loadUrl(videoUrl); // 加载视频 URL
        }
    }

    // 处理返回按钮，允许 WebView 后退
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
