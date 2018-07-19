package www.comradesoftware.vip.receiver.tac;

import android.content.Context;

import com.tencent.tac.messaging.TACMessagingReceiver;
import com.tencent.tac.messaging.TACMessagingText;
import com.tencent.tac.messaging.TACMessagingToken;
import com.tencent.tac.messaging.TACNotification;
import com.tencent.tac.messaging.type.PushChannel;

import www.comradesoftware.vip.utils.LogUtil;
import www.comradesoftware.vip.utils.ToastUtil;


/**
 * 腾讯移动推送（信鸽）回调
 */
public class MyTACMessagingReceiver extends TACMessagingReceiver {

    // 启动 Messaging 服务后，会自动向 Messaging 后台注册，注册完成后会回调此接口。
    @Override
    public void onRegisterResult(Context context, int i, TACMessagingToken tacMessagingToken) {
//        ToastUtil.showToast(context, "注册结果返回：" + tacMessagingToken);
        LogUtil.i("messaging", "MyReceiver::OnRegisterResult : code is " + i + ", token is " + tacMessagingToken.getTokenString());
    }
    // 反注册后回调此接口。
    @Override
    public void onUnregisterResult(Context context, int i) {

        ToastUtil.showToast(context, "取消注册结果返回：" + i);
        LogUtil.i("messaging", "MyReceiver::onUnregisterResult : code is " + i);
    }

    // 收到透传消息后回调此接口。
    @Override
    public void onMessageArrived(Context context, TACMessagingText tacMessagingText, PushChannel pushChannel) {
        ToastUtil.showToast(context, "收到透传消息：" + tacMessagingText);
        LogUtil.i("messaging", "MyReceiver::OnTextMessage : message is " + tacMessagingText+ " pushChannel " + pushChannel);
    }

    // 收到通知栏消息后回调此接口。
    @Override
    public void onNotificationArrived(Context context, TACNotification tacNotification, PushChannel pushChannel) {
        ToastUtil.showToast(context, "收到通知消息：" + pushChannel);
        LogUtil.i("messaging", "MyReceiver::onNotificationArrived : notification is " + tacNotification + " pushChannel " + pushChannel);
    }

    // 点击通知栏消息后回调此接口。
    @Override
    public void onNotificationClicked(Context context, TACNotification tacNotification, PushChannel pushChannel) {
        LogUtil.i("messaging", "MyReceiver::onNotificationClicked : notification is " + tacNotification + " pushChannel " + pushChannel);
    }

    // 删除通知栏消息后回调此接口。
    @Override
    public void onNotificationDeleted(Context context, TACNotification tacNotification, PushChannel pushChannel) {
        LogUtil.i("messaging", "MyReceiver::onNotificationDeleted : notification is " + tacNotification + " pushChannel " + pushChannel);
    }

    // 绑定标签回调
    @Override
    public void onBindTagResult(Context context, int i, String s) {
        ToastUtil.showToast(context, "绑定标签成功：tag = " + s);
        LogUtil.i("messaging", "MyReceiver::onBindTagResult : code is " + i + " tag " + s);
    }

    // 解绑标签回调
    @Override
    public void onUnbindTagResult(Context context, int i, String s) {
        ToastUtil.showToast(context, "解绑标签成功：tag = " + s);
        LogUtil.i("messaging", "MyReceiver::onUnbindTagResult : code is " + i + " tag " + s);
    }
}
