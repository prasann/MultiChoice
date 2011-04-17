package com.prasans.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.prasans.R;
import com.prasans.domain.ScoreInfo;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ScoreInfoListAdapter extends ArrayAdapter<ScoreInfo> {


    private List<ScoreInfo> items;
    private Context myContext;

    public ScoreInfoListAdapter(Context context, int textViewResourceId, List<ScoreInfo> items) {
        super(context, textViewResourceId, items);
        this.items = items;
        this.myContext = context;
    }

    

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) myContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.row_score, null);
        }
        ScoreInfo scoreInfo = items.get(position);
        if (scoreInfo != null) {
            TextView number = (TextView) view.findViewById(R.id.row_number);
            TextView score = (TextView) view.findViewById(R.id.row_score);
            TextView time = (TextView) view.findViewById(R.id.row_time);
            setFields(scoreInfo.getPhoneNumber(), number);
            setFields(scoreInfo.getScore(), score);
            setFields(scoreInfo.getReceivedTime(), time);
        }
        return view;
    }

    private void setFields(String text, TextView textView) {
        if (textView != null) {
            textView.setText(text);
        }
    }
}
