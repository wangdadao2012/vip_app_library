package www.comradesoftware.vip.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class IcTextView extends android.support.v7.widget.AppCompatTextView {
    public IcTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IcTextView(Context context) {
        super(context, null);
        init(context);
    }

    private void init(Context context){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/iconfont.woff");
        setTypeface(typeface);
    }
}
