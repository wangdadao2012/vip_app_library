package www.comradesoftware.vip.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ActivityBase extends AppCompatActivity {
    public static final int Handler_What_ShowLoading = 20010;
    public static final int Handler_What_CloseLoading = 20011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
