package tv.dfyc.yckt.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;
import android.widget.TextView;

/**
 * Created by admin on 2017-9-6.
 */

public class MarqueeTextView extends AppCompatTextView {
    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
