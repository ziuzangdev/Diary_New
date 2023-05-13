package sontungmtp.project.diary.Model.TagDiary;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class TagCardView extends CardView {
    private boolean isChoose = false;
    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        isChoose = !isChoose;
        onCLickTagCardView(isChoose);
    }

    public TagCardView(@NonNull Context context) {
        super(context);
    }

    public TagCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TagCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Call everytime TagCardView was click by User
     */
    public void onCLickTagCardView(boolean isChoose){

    }
}
