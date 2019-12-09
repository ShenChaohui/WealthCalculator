package com.zydl.wealthcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.j256.ormlite.stmt.query.In;
import com.zydl.wealthcalculator.utils.SPUtils;
import com.zydl.wealthcalculator.view.PrinterTextView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Sch.
 * Date: 2019/11/28
 * description:
 */
public class WelcomeActivity extends AppCompatActivity {
    private PrinterTextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);
        mTextView = findViewById(R.id.ptv);
        showWelcomeText();
    }

    private void showWelcomeText() {
        final String welcomeText ="\t\t\t\t"+SPUtils.get("welcomeText", "往后余生，请多多指教");
        mTextView.setPrintText(welcomeText, 150);
        mTextView.startPrint();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(150 * welcomeText.length() + 800);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
