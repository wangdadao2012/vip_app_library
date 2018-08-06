package www.comradesoftware.vip.activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import www.comradesoftware.vip.R;


public class BaseActivity extends AppCompatActivity {
    public static final int Handler_What_ShowLoading = 20010;
    public static final int Handler_What_CloseLoading = 20011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
