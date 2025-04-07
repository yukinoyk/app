package com.android.jiangyan;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
public class MainActivity extends AppCompatActivity {
    private EditText videoUrlInput; // 用户输入视频链接
    private Button parseButton; // 解析按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 加载布局文件
        setContentView(R.layout.activity_main);

        videoUrlInput = findViewById(R.id.videoUrlInput); // 绑定输入框
        parseButton = findViewById(R.id.parseButton); // 绑定解析按钮

        parseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userUrl = videoUrlInput.getText().toString().trim(); // 获取用户输入的URL
                if (!userUrl.isEmpty()) {
                    // 拼接解析 API 接口
                    String parsedUrl = "https://jx.playerjy.com/?url=" + userUrl;

                    // 跳转到 VideoActivity 播放视频
                    Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                    intent.putExtra("videoUrl", parsedUrl); // 传递解析后的视频链接
                    startActivity(intent);
                }
            }
        });
    }

    // 重写返回键操作，支持 WebView 后退操作
    @Override
    public void onBackPressed() {
        WebView webView = null;
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
