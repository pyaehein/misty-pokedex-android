package com.romeroz.mistypokedex.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.romeroz.mistypokedex.DetailActivity;
import com.romeroz.mistypokedex.R;
import com.romeroz.mistypokedex.model.Pokemon;
import com.romeroz.mistypokedex.model.RealmString;

import java.util.ArrayList;

import io.realm.RealmList;


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
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        // Set default view visibility

        // Data from the ArrayList
        int id;
        String name;

        id = mItemArrayList.get(position).getId();
        name = mItemArrayList.get(position).getName();

        // Format pokemon id from "1" to "001"
        String formattedId = String.format("%03d", id);
        viewHolder.mIdTextView.setText(String.valueOf(formattedId));
        viewHolder.mNameTextView.setText(name);

        // Generate Image resource Id by image name in drawable folder
        int drawableResourceId = mContext.getResources().getIdentifier("pokemon_thumb_" + String.valueOf(id)
                , "drawable", mContext.getPackageName());
        viewHolder.mPokemonImageView.setImageResource(drawableResourceId);

        // Set up pokemon types image views
        RealmList<RealmString> types = mItemArrayList.get(position).getType();

        // Check for first type (should always be there)
        if(types.size() > 0){
            // Get first type
            String typeOne = types.get(0).getVal();

            String typeImageOne = getTypeImageName(typeOne);

            // Generate Image resource Id by image name in drawable folder
            drawableResourceId = mContext.getResources().getIdentifier(typeImageOne
                    , "drawable", mContext.getPackageName());
            viewHolder.mTypeOneImageView.setImageResource(drawableResourceId);
        }

        // Check if there is array index for two types
        if (types.size() > 1){
            // Get second type
            String typeTwo = types.get(1).getVal();

            String typeImageTwo = getTypeImageName(typeTwo);

            // Generate Image resource Id by image name in drawable folder
            drawableResourceId = mContext.getResources().getIdentifier(typeImageTwo
                    , "drawable", mContext.getPackageName());
            viewHolder.mTypeTwoImageView.setImageResource(drawableResourceId);

            viewHolder.mTypeTwoImageView.setVisibility(View.VISIBLE);

        } else {
            viewHolder.mTypeTwoImageView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if ( null == mItemArrayList) return 0;
        return mItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected CardView mCardView;
        protected ImageView mPokemonImageView;
        protected TextView mNameTextView;
        protected TextView mIdTextView;
        protected ImageView mTypeOneImageView;
        protected ImageView mTypeTwoImageView;


        public ViewHolder(View view) {
            super(view);

            // Get references to our views
            mCardView = (CardView) view.findViewById(R.id.card_view);
            mPokemonImageView = (ImageView) view.findViewById(R.id.pokemon_image_view);
            mNameTextView = (TextView) view.findViewById(R.id.name_text_view);
            mIdTextView = (TextView) view.findViewById(R.id.id_text_view);
            mTypeOneImageView = (ImageView) view.findViewById(R.id.type_one_image_view);
            mTypeTwoImageView = (ImageView) view.findViewById(R.id.type_two_image_view);



            mCardView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    // Pass pokemon id to activity
                    int id = mItemArrayList.get(getAdapterPosition()).getId();

                    Intent i = new Intent(mContext, DetailActivity.class);
                    i.putExtra(DetailActivity.ARG_POKEMON_ID, id);

                    // Pretty transitions on newer devices
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // Remember to set android:transitionName in activity_detail.xml and item_layout.xml
                        mContext.startActivity(i, ActivityOptions.makeSceneTransitionAnimation(
                                (Activity) mContext, mPokemonImageView, mPokemonImageView.getTransitionName()).toBundle());
                    } else {
                        // Code to run on older devices
                        mContext.startActivity(i);
                    }

                }
            });

        }
    }

    /**
     * Get the image name based on type of pokemon
     * @param type
     * @return
     */
    private String getTypeImageName(String type){
        String typeImage = "";

        switch (type) {
            case "Normal":
                typeImage = "type_normal";
                break;
            case "Fire":
                typeImage = "type_fire";
                break;
            case "Fighting":
                typeImage = "type_fighting";
                break;
            case "Water":
                typeImage = "type_water";
                break;
            case "Poison":
                typeImage = "type_poison";
                break;
            case "Electric":
                typeImage = "type_electric";
                break;
            case "Ground":
                typeImage = "type_ground";
                break;
            case "Grass":
                typeImage = "type_grass";
                break;
            case "Flying":
                typeImage = "type_flying";
                break;
            case "Ice":
                typeImage = "type_ice";
                break;
            case "Bug":
                typeImage = "type_bug";
                break;
            case "Psychic":
                typeImage = "type_psychic";
                break;
            case "Rock":
                typeImage = "type_rock";
                break;
            case "Dragon":
                typeImage = "type_dragon";
                break;
            case "Ghost":
                typeImage = "type_ghost";
                break;
            case "Dark":
                typeImage = "type_dark";
                break;
            case "Steel":
                typeImage = "type_steel";
                break;
            case "Fairy":
                typeImage = "type_fairy";
                break;
        }

        return typeImage;
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