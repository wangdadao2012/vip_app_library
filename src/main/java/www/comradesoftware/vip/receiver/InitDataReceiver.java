package www.comradesoftware.vip.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 接收初始化app数据进度(InitDataService)的广播接收器，用于在activity接收进度更新UI
 */
public class InitDataReceiver extends BroadcastReceiver {

    private OnInitDataReceiverListener mListener;
    public static final String ACTION_INIT_DATA_UI ="action.initData.ui";

    public InitDataReceiver(Context context){
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INIT_DATA_UI);
        context.registerReceiver(this, filter);
    }
    public interface OnInitDataReceiverListener{
        void onReceive(Context context, Intent intent);
    }
    public void setOnInitDataReceiverListener(OnInitDataReceiverListener listener){
        this.mListener=listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mListener.onReceive(context,intent);
    }
}
