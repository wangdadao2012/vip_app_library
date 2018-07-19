package www.comradesoftware.vip.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import www.comradesoftware.vip.R;

import www.comradesoftware.vip.receiver.InitDataReceiver;
import www.comradesoftware.vip.service.InitDataService;
import www.comradesoftware.vip.view.CircleImageView;

//启动页
public class SplashActivity extends ActivityBase{
    private final String TAG ="SplashActivity";
    private CircleImageView imgLauncher;
    private InitDataReceiver mInitDataReceiver;
    public ProgressBar progressBar;
    public TextView tvProgressText;

    private boolean isBind=false;

    private ServiceConnection conn = new ServiceConnection() {
        //连接回调
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            InitDataService.MyBinder myBinder = (InitDataService.MyBinder) binder;
            InitDataService service = myBinder.getService();
            service.installDefaultData();
            service.setActivity(SplashActivity.this);
        }
        //断开连接回调
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        bindInitService();
        receiveInitDataBroadcast();
    }

    //接收InitService处理数据进度的广播，更新UI
    private void receiveInitDataBroadcast() {
        mInitDataReceiver=new InitDataReceiver(this);
        mInitDataReceiver.setOnInitDataReceiverListener(new InitDataReceiver.OnInitDataReceiverListener() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tvProgressText.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                int progress=intent.getIntExtra("progress",0);
                String text=intent.getStringExtra("content");
                tvProgressText.setText(text+String.valueOf(progress)+"%");
//                tvProgressText.setText("loading...");
                progressBar.setProgress(progress);
            }
        });
    }

    //绑定服务
    private void bindInitService() {
        Intent intent = new Intent(SplashActivity.this, InitDataService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private void initView() {
        imgLauncher=findViewById(R.id.imgLauncher);
        tvProgressText=findViewById(R.id.tvProgressText);
        progressBar=findViewById(R.id.progressBar);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUESTCODE) {
//            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0]
//                    == PackageManager.PERMISSION_GRANTED) {
//                //用户同意
//                initAppFile();
//            } else {
//                //用户不同意
//                finish();
//            }
//        }
//    }
//
//    private void requestPermissions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            int checkSelfPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE);
//            }else {
//                //用户同意
//                initAppFile();
//            }
//        }else {
//            //6.0以下直接come
//            initAppFile();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        unregisterReceiver(mInitDataReceiver);
    }

    @Override//禁用返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

}
