package sontungmtp.project.diary.Model.Chart;

import java.util.Collections;
import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import sontungmtp.project.diary.Model.ThemeManager.AppThemeManager;

public class ColumnChartView extends View {
    private Hashtable<String, Integer> data;

    private int columnWidth;

    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }


    public Hashtable<String, Integer> getData() {
        return data;
    }

    public void setData(Hashtable<String, Integer> data) {
        this.data = data;
    }

    public ColumnChartView(Context context) {
        super(context);
    }

    public ColumnChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColumnChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ColumnChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Calculate column spacing
        int columnSpacing = width / (data.size() + 1);

        // Calculate maximum height of the chart
        int maxHeight = height - 50;

        int maxDataValue = Collections.max(data.values());
        int maxValue = maxDataValue + 4;
        int numberSpacing;
        if (maxDataValue > maxValue) {
            numberSpacing = maxHeight / maxDataValue;
        } else {
            numberSpacing = maxHeight / maxValue;
        }

        // Draw x and y axis
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#BFBFBF"));
        paint.setStrokeWidth(3);
        canvas.drawLine(50, height - 50, width, height - 50, paint);
        canvas.drawLine(50, 0, 50, height - 50, paint);

        int numberOfNumbers = maxHeight / numberSpacing;
        if (numberOfNumbers > 10) {
            numberOfNumbers = 10;
        }
        int newNumberSpacing = maxHeight / numberOfNumbers;
        paint.setTextSize(25);
        for (int i = 0; i <= maxValue; i += maxValue / numberOfNumbers) {
            int y = height - 50 - newNumberSpacing * (i / (maxValue / numberOfNumbers));
            canvas.drawText(String.valueOf(i), -1, y, paint);
        }

        // Draw columns
        paint.setColor(Color.parseColor(new AppThemeManager(getContext()).getPaletteColor()[3]));
        int i = 0;
        paint.setTextSize(40);
        for (String key : data.keySet()) {
            int value = data.get(key);
            int columnHeight = numberSpacing * value;
            int left = columnSpacing * (i + 1);
            int top = height - 50 - columnHeight;
            int right = left + columnSpacing - 10;
            int bottom = height - 50;
            canvas.drawRect(left, top, right, bottom, paint);
            int labelX = columnSpacing * (i + 1) + (columnSpacing - columnWidth) / 2 + columnWidth / 2;
            // Draw label below x axis at the center of the column
            canvas.drawText(key, labelX - 30, height - 10, paint);
            i++;
        }
    }

    public void generateChart() {
        invalidate();
    }
}
