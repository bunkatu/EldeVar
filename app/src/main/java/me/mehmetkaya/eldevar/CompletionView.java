package me.mehmetkaya.eldevar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Created by kullanici on 09.01.2016.
 */
public class CompletionView extends TokenCompleteTextView<Malzeme> {
    public CompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Malzeme malzeme) {

        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout)l.inflate(R.layout.malzeme_token, (ViewGroup)CompletionView.this.getParent(), false);
        ((TextView)view.findViewById(R.id.name)).setText(malzeme.getIsim());

        return view;
    }

    @Override
    protected Malzeme defaultObject(String completionText) {
            return new Malzeme(completionText);
    }
}
