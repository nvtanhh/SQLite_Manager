package com.example.sqlitemanager.listViewAdapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.sqlitemanager.R;

import java.util.ArrayList;
import java.util.Arrays;

class RowView extends FrameLayout {

    private final int mColumnCount;
    private final ArrayList<TextView> mTextViewList;
    private final LinearLayout mRowViewContainer;
    private String[] row;

    RowView(Context context, int columnCount) {
        super(context);

//        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sqlite_manager_background_white));

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sqlite_manager_selectable_horizontal_linear_layout, this, true);
        mRowViewContainer = (LinearLayout) findViewById(R.id.sqlite_manager_row_view_container);

        mColumnCount = columnCount;
        mTextViewList = new ArrayList<>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            TextView textView = getTextView();
            mTextViewList.add(textView);
            mRowViewContainer.addView(textView);
        }
    }

    private TextView getTextView() {
        return (TextView) LayoutInflater.from(getContext()).inflate(R.layout.sqlite_manager_column_text_item, this, false);
    }

    void setData(String[] columnIndexToValues) {
        if (columnIndexToValues.length != mColumnCount) {
            throw new IllegalArgumentException("columnIndexValues count doesn't match columnCount");
        }

        row = columnIndexToValues;

        for (int i = 0; i < row.length; i++) {
            Object columnEntry = row[i];

            if (columnEntry == null) {
                mTextViewList.get(i).setText("(NULL)");
                mTextViewList.get(i).setTextColor(ContextCompat.getColor(getContext(), R.color.sqlite_manager_txt_disabled));
            } else {
                if (columnEntry instanceof String && TextUtils.isEmpty(columnEntry.toString())) {
                    mTextViewList.get(i).setText("(EMPTY)");
                    mTextViewList.get(i).setTextColor(ContextCompat.getColor(getContext(), R.color.sqlite_manager_txt_disabled));
                } else if (columnEntry instanceof byte[]) {
                    mTextViewList.get(i).setText(Arrays.toString((byte[]) columnEntry));
                    mTextViewList.get(i).setTextColor(ContextCompat.getColor(getContext(), R.color.sqlite_manager_txt_secondary));
                } else {
                    mTextViewList.get(i).setText(columnEntry.toString());
                    mTextViewList.get(i).setTextColor(ContextCompat.getColor(getContext(), R.color.sqlite_manager_txt_secondary));
                }
            }

        }
    }
}
