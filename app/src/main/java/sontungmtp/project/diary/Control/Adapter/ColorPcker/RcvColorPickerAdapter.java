package sontungmtp.project.diary.Control.Adapter.ColorPcker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sontungmtp.project.diary.Model.Painting.DrawView;
import sontungmtp.project.diary.Model.RichEditor.RichEditor;
import sontungmtp.project.diary.R;

import java.util.ArrayList;

/**
 * A RecyclerView.Adapter for displaying a list of colors as items in a RecyclerView.
 * Allows the user to select a color from the list to use for either text color in a RichEditor or paint color in a DrawView.
 * @author [TrikayDev]
 * @since [12/30/2022]
 */
public class RcvColorPickerAdapter extends RecyclerView.Adapter<RcvColorPickerAdapter.ViewHolder> {
    private ArrayList<String> color;
    private Context context;

    private RichEditor richEditor;

    private DrawView drawView;

    /**
     * Constructor for the RcvColorPickerAdapter for use with a DrawView.
     * Initializes the list of colors to a default list.
     *
     * @param drawView the DrawView to apply the selected color to
     */
    public RcvColorPickerAdapter(DrawView drawView) {
        this.drawView = drawView;
        inutColor();
    }

    /**
     * Constructor for the RcvColorPickerAdapter for use with a RichEditor.
     * Initializes the list of colors to a default list.
     *
     * @param richEditor the RichEditor to apply the selected color to
     */
    public RcvColorPickerAdapter(RichEditor richEditor) {
        this.richEditor = richEditor;
        inutColor();
    }



    /**
     * Initializes the list of colors based on which constructor was used.
     */
    private void inutColor() {
        if(richEditor != null){
            color = new ArrayList<>();
            color.add("#000000");
            color.add("#FFFFFF");
            color.add("#FFFC00");
            color.add("#0099FF");
            color.add("#81C784");
            color.add("#BA68C8");
            color.add("#D500F9");
            color.add("#FF3D00");
        }else if(drawView != null){
            color = new ArrayList<>();
            color.add("#FFFC00");
            color.add("#0099FF");
            color.add("#81C784");
            color.add("#212121");
            color.add("#BA68C8");
            color.add("#D500F9");
            color.add("#FF3D00");
        }

    }

    /**
     * Creates a new ViewHolder when a new item is added to the RecyclerView.
     *
     * @param parent the parent ViewGroup
     * @param viewType the type of the View
     * @return the new ViewHolder
     */
    @NonNull
    @Override
    public RcvColorPickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = inflater.inflate(R.layout.item_rcvcolorpicker, parent, false);
        return new RcvColorPickerAdapter.ViewHolder(view);
    }

    /**
     * Binds the data at the given position to the ViewHolder.
     * Sets the background color of the item View to the color at the given position in the list,
     * and sets an OnClickListener on the item View that applies the color to either a RichEditor or DrawView.
     *
     * @param holder the ViewHolder to bind the data to
     * @param position the position of the data in the list
     */
    @Override
    public void onBindViewHolder(@NonNull RcvColorPickerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(richEditor != null){
            holder.color_item.setBackgroundColor(Color.parseColor(color.get(position)));
            holder.color_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    richEditor.setTextColor(Color.parseColor(color.get(position)));
                }
            });
        }else if(drawView != null){
            holder.color_item.setBackgroundColor(Color.parseColor(color.get(position)));
            holder.color_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   drawView.getDrawPaint().setColor((Color.parseColor(color.get(position))));
                }
            });
        }

    }

    /**
     * @return the size of the list of colors
     */
    @Override
    public int getItemCount() {
        return color.size();
    }

    /**
     * @return the list of colors
     */
    public ArrayList<String> getColor() {
        return color;
    }

    /**
     * A ViewHolder for the RcvColorPickerAdapter.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout color_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            color_item = itemView.findViewById(R.id.color_item);
        }
    }
}
