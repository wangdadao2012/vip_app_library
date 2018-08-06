package www.comradesoftware.vip.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class MyAlertDialog{
    //region 确认/取消 弹出框
    //取消按钮，默认canel
    public static Dialog createConfirmDialog(Context context, String title, String message,
                                             String positiveBtnName, String negativeBtnName, DialogInterface.OnClickListener positiveBtnListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //设置对话框标题
        builder.setTitle(title);
        //设置对话框消息
        builder.setMessage(message);
        //设置确定按钮
        builder.setPositiveButton(positiveBtnName, positiveBtnListener);
        //设置取消按钮
        builder.setNegativeButton(negativeBtnName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }

    //自定义取消按钮事件
    public static Dialog createConfirmDialog(Context context, String title, String message,
                                             String positiveBtnName, String negativeBtnName, DialogInterface.OnClickListener positiveBtnListener,
                                             DialogInterface.OnClickListener negativeBtnListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //设置对话框标题
        builder.setTitle(title);
        //设置对话框消息
        builder.setMessage(message);
        //设置确定按钮
        builder.setPositiveButton(positiveBtnName, positiveBtnListener);
        //设置取消按钮
        builder.setNegativeButton(negativeBtnName, negativeBtnListener);
        //创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }
    //endregion


    //region 单选 弹出框

    public static Dialog createRadioDialog(Context context, String title, final String[] ss , DialogInterface.OnClickListener btnListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //设置对话框标题
        builder.setTitle(title);
        builder.setSingleChoiceItems(ss, 1, btnListener);

        //创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }

    //endregion
}
