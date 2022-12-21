package com.project.diary.Model.Sticker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aghajari.emojiview.sticker.Sticker;
import com.aghajari.emojiview.sticker.StickerCategory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.project.diary.R;

public class ShopStickers implements StickerCategory<Integer> {
    @NonNull
    @Override
    public ShopSticker[] getStickers() {
        return new ShopSticker[]{
                new ShopSticker(
                        new Sticker[]{
                                new Sticker<>(R.drawable.sticker),
                                new Sticker<>(R.drawable.sticker1),
                                new Sticker<>(R.drawable.sticker2),
                                new Sticker<>(R.drawable.sticker3),
                                new Sticker<>(R.drawable.sticker4)}
                        ,"Cuppy Pack 1",16),

                new ShopSticker(
                        new Sticker[]{
                                new Sticker<>(R.drawable.sticker),
                                new Sticker<>(R.drawable.sticker1),
                                new Sticker<>(R.drawable.sticker2),
                                new Sticker<>(R.drawable.sticker3),
                                new Sticker<>(R.drawable.sticker4)}
                        ,"Cuppy Pack 2",16),

                new ShopSticker(
                        new Sticker[]{
                                new Sticker<>(R.drawable.sticker),
                                new Sticker<>(R.drawable.sticker1),
                                new Sticker<>(R.drawable.sticker2),
                                new Sticker<>(R.drawable.sticker3),
                                new Sticker<>(R.drawable.sticker4)}
                        ,"Cuppy Pack 3",16),

                new ShopSticker(
                        new Sticker[]{
                                new Sticker<>(R.drawable.sticker),
                                new Sticker<>(R.drawable.sticker1),
                                new Sticker<>(R.drawable.sticker2),
                                new Sticker<>(R.drawable.sticker3),
                                new Sticker<>(R.drawable.sticker4)}
                        ,"Cuppy Pack 4",16),
        };
    }

    @Override
    public Integer getCategoryData() {
        return R.drawable.ic_masks_msk;
    }

    @Override
    public boolean useCustomView() {
        return true;
    }

    @Override
    public View getView(ViewGroup viewGroup) {
        RecyclerView recyclerView = new RecyclerView(viewGroup.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(viewGroup.getContext()));
        recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        return recyclerView;
    }

    @Override
    public void bindView(View view) {
        ((RecyclerView)view).setAdapter(new ShopAdapter(this));
    }

    @Override
    public View getEmptyView(ViewGroup viewGroup) {
        return null;
    }


    public static class ShopAdapter extends RecyclerView.Adapter<ShopStickers.ShopAdapter.ShopViewHolder>{
        ShopStickers provider;
        public ShopAdapter (ShopStickers provider){
            this.provider = provider;
        }

        @NonNull
        @Override
        public ShopStickers.ShopAdapter.ShopViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from( viewGroup.getContext()).inflate(R.layout.shop_recyclerview, viewGroup, false);

            TextView title = v.findViewById(R.id.sticker_title);
            TextView subtitle = v.findViewById(R.id.sticker_subtitle);
            Button add = v.findViewById(R.id.sticker_add);
            RecyclerView stickers = v.findViewById(R.id.stickers_rv);
            stickers.setLayoutManager(new GridLayoutManager(viewGroup.getContext(),5));

            return new ShopViewHolder(v, title, subtitle, add, stickers);
        }

        @Override
        public void onBindViewHolder(@NonNull final ShopStickers.ShopAdapter.ShopViewHolder viewHolder, int i) {
            final ShopSticker sticker = provider.getStickers()[i];
            viewHolder.title.setText(sticker.title);
            viewHolder.subTitle.setText(sticker.count + " Sticker");

            viewHolder.add.setOnClickListener(view -> Toast.makeText(view.getContext(),"Add "+sticker.title, Toast.LENGTH_SHORT).show());

            viewHolder.stickers.setAdapter(new IconAdapter((Sticker<?>[]) sticker.getData()));
        }

        @Override
        public int getItemCount() {
            return provider.getStickers().length;
        }

        public static class ShopViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            TextView subTitle;
            Button add;
            RecyclerView stickers;
            public ShopViewHolder(@NonNull View itemView, TextView title, TextView subTitle, Button add, RecyclerView stickers) {
                super(itemView);
                this.title = title;
                this.subTitle = subTitle;
                this.add = add;
                this.stickers = stickers;


            }
        }
    }



    public static class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconViewHolder>{
        Sticker<?>[] stickers;

        public IconAdapter (Sticker<?>[] stickers){
            this.stickers = stickers;
        }

        @NonNull
        @Override
        public IconViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from( viewGroup.getContext()).inflate(R.layout.shop_sticker_item, viewGroup, false);
            return new IconViewHolder(v, v.findViewById(R.id.sticker_img));
        }

        @Override
        public void onBindViewHolder(@NonNull IconViewHolder viewHolder, int i) {
            Glide.with(viewHolder.imageView)
                    .load(Integer.valueOf(stickers[i].getData().toString()))
                    .apply(RequestOptions.fitCenterTransform())
                    .into(viewHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return stickers.length;
        }

        public static class IconViewHolder extends RecyclerView.ViewHolder{
            AppCompatImageView imageView;
            public IconViewHolder(@NonNull View itemView, AppCompatImageView imageView) {
                super(itemView);
                this.imageView = imageView;
            }
        }
    }
}
