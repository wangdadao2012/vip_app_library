package www.comradesoftware.vip.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class IcTextView extends android.support.v7.widget.AppCompatTextView {
    public IcTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/app.ttf");
        setTypeface(typeface);
    }

    public IcTextView(Context context) {
        super(context, null);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/app.ttf");
        setTypeface(typeface);
    }
}
