package com.romeroz.mistypokedex.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.romeroz.mistypokedex.R;
import com.romeroz.mistypokedex.model.Pokemon;

import java.util.ArrayList;


public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Pokemon> mItemArrayList;

    public PokedexAdapter(Context context) {
        this.mContext = context;
     }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Set default view visibility

        // Data from the ArrayList
        int id;
        String name;


        id = mItemArrayList.get(position).getId();
        name = mItemArrayList.get(position).getName();

        
        viewHolder.mNameTextView.setText(name);

        // Generate Image resource Id by image name in drawable folder
        int drawableResourceId = mContext.getResources().getIdentifier("pokemon_thumb_" + String.valueOf(id)
                , "drawable", mContext.getPackageName());
        viewHolder.mPokemonImageView.setImageResource(drawableResourceId);
    }

    @Override
    public int getItemCount() {
        if ( null == mItemArrayList) return 0;
        return mItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mPokemonImageView;
        protected TextView mNameTextView;


        public ViewHolder(View view) {
            super(view);

            // Get references to our views
            mPokemonImageView = (ImageView) view.findViewById(R.id.pokemon_image_view);
            mNameTextView = (TextView) view.findViewById(R.id.name_text_view);


        }
    }

    public void swapData(ArrayList<Pokemon> itemArrayList) {
        this.mItemArrayList = null;
        this.mItemArrayList = itemArrayList;;

        notifyDataSetChanged();
    }

    public void addItem(Pokemon item) {
        if (item != null){
            mItemArrayList.add(item);
            notifyItemInserted(mItemArrayList.size()-1);
        }
    }

    public void removeItem(int position) {
        mItemArrayList.remove(position);
        notifyItemRemoved(position);
    }

    public void changeItem(Pokemon itemArrayList, int position){
        if (itemArrayList != null){
            mItemArrayList.set(position, itemArrayList);
            notifyItemChanged(position);
        }
    }

}