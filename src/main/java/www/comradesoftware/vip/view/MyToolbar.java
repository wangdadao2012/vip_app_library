package www.comradesoftware.vip.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import www.comradesoftware.vip.R;
import www.comradesoftware.vip.utils.LogUtil;

public class MyToolbar extends LinearLayout implements View.OnClickListener {
    private OnItemClickListener mListener;
    private Context context;
    private IcTextView mStartText, mEndText;
    private TextView mCenterText;
    private List<TextView> textViewList = new ArrayList<>();

    public MyToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //判断调用类是否实现了本类的接口，是则赋值
        if (MyToolbar.class.isAssignableFrom(context.getClass())) {
            this.mListener = (OnItemClickListener) context;
        }
        //初始化一些属性
        setMinimumHeight(45);
        setBackgroundColor(getResources().getColor(android.R.color.white));
        setOrientation(HORIZONTAL);
        addView();
    }

    private void addView() {
        for (int i = 0; i < 4; i++) {
            int weight = 1;
            TextView textView =null;
            switch (i){
                case 0:
                    weight = 2;
                    textView = new IcTextView(context);
                    textView.setText(R.string.ic_back);
                    break;
                case 1:
                    weight = 4;
                    textView = new TextView(context);
                    textView.setText(R.string.app_name);
                    break;
                case 2:
                    weight = 1;
                    textView = new TextView(context);
                    textView.setText("刷新");
                    break;
                case 3:
                    weight = 1;
                    textView = new IcTextView(context);
                    textView.setText(R.string.ic_close);
                    break;
            }
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, weight);
            textView.setId(View.generateViewId());
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(layoutParams);
            textView.setTextColor(getResources().getColor(R.color.colorAppText));
            textView.setTextSize(16);
            textView.getPaint().setFakeBoldText(true);//加粗
            textView.setOnClickListener(this);
            addView(textView);
            textViewList.add(textView);
        }
    }

    public void setTitle(String title) {
        textViewList.get(1).setText(title);
    }

    public void setTitleSize(float size) {
        textViewList.get(1).setTextSize(size);
    }

    public void setTitieColor(int color) {
        textViewList.get(1).setTextColor(color);
    }

    public void setStartTx(String text) {
        textViewList.get(0).setText(text);
    }

    public void setStartTxSize(float size) {
        textViewList.get(0).setTextSize(size);
    }

    public void setStartTxColor(int color) {
        textViewList.get(0).setTextColor(color);
    }

    public void setEndTx(String text) {
        textViewList.get(2).setText(text);
    }

    public void setEndTxSize(float size) {
        textViewList.get(2).setTextSize(size);
    }

    public void setEndTxColor(int color) {
        textViewList.get(2).setTextColor(color);
    }

    public void hideBack(boolean hide){
        if (hide)
            textViewList.get(0).setVisibility(INVISIBLE);
        else
            textViewList.get(0).setVisibility(VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            int id = v.getId();
            int size=textViewList.size();
            for (int i=0;i<size;i++){
                if (textViewList.get(i).getId()== id){
                    mListener.onItemClick(v, i);
                }
            }
            LogUtil.e("MyToolBar", "id:" + id);
        }
    }

    //    点击事件接口
    public interface OnItemClickListener {
        void onItemClick(View v, int index);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
