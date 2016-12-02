package com.uuzuche.lib_zxing.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.uuzuche.lib_zxing.R;

/**
 * Initial the camera
 *
 * 默认的二维码扫描Activity
 */
public class CaptureActivity extends AppCompatActivity {

    /**
     * 扫描跳转
     */
    public static final int CALLBACK_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        CaptureFragment captureFragment = new CaptureFragment();
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_zxing_container, captureFragment).commit();
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
//            bundle.putString(CodeUtils.RESULT_STRING, result);
            //resultIntent.putExtras(bundle);
            //CaptureActivity.this.setResult(RESULT_OK, resultIntent);

            //EventBus.getDefault().post(new EventCenter(CALLBACK_CODE,bundle));

            Intent intent = new Intent("android.intent.action.ER_WEI_MA");
            intent.putExtra(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            intent.putExtra(CodeUtils.RESULT_STRING, result);
            sendBroadcast(intent);

            CaptureActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
            CaptureActivity.this.finish();
        }
    };
}