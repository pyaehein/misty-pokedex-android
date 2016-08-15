package com.romeroz.mistypokedex;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.romeroz.mistypokedex.model.Pokemon;
import com.romeroz.mistypokedex.model.RealmString;

import io.realm.Realm;
import io.realm.RealmList;

public class DetailActivity extends AppCompatActivity {

    // Activity initialization parameters
    public static final String ARG_POKEMON_ID = "POKEMON_ID";
    private int pokemonId;

    private Toolbar mToolbar;
    private ImageView mPokemonImageView;
    private TextView mNameTextView;
    private ImageView mTypeOneImageView;
    private ImageView mTypeTwoImageView;

    Realm mRealm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Setup Toolbar
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Override default behavior to be that of onBackPressed() to maintain animation
                onBackPressed();
            }
        });


        mRealm = Realm.getDefaultInstance();

        // Get extras passed in from previous invoker
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pokemonId  = extras.getInt(ARG_POKEMON_ID);
            setupUI(pokemonId);
        }
    }

    private void setupUI(int pokemonId){

        // Get reference to view
        mPokemonImageView = (ImageView) findViewById(R.id.pokemon_image_view);
        mNameTextView = (TextView) findViewById(R.id.name_text_view);
        mTypeOneImageView = (ImageView) findViewById(R.id.type_one_image_view);
        mTypeTwoImageView = (ImageView) findViewById(R.id.type_two_image_view);

        // Find the pokemon in Realm
        Pokemon pokemon = mRealm.where(Pokemon.class).equalTo("id", pokemonId).findFirst();

        mNameTextView.setText(pokemon.getName());

        // Set title to read e.g. "#001 Bulbasaur"
        getSupportActionBar().setTitle(Utility.getPokemonIdStringFromInt(pokemonId) + " " + pokemon.getName());

        // Generate Image resource Id by image name in drawable folder
        int drawableResourceId = getResources().getIdentifier("pokemon_img_" + String.valueOf(pokemonId)
                , "drawable", getPackageName());
        mPokemonImageView.setImageResource(drawableResourceId);

        // Set up type imagesviews (e.g. Fire, water, etc.)
        RealmList<RealmString> types = pokemon.getType();
        setupTypeImageViews(types);



        // For mPokemonImageView transition (see PokedexAdapter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this)
                    .inflateTransition(R.transition.move));
        }
    }

    private void setupTypeImageViews(RealmList<RealmString> types){
        // Check for first type (should always be there)
        if(types.size() > 0){
            // Get first type
            String typeOne = types.get(0).getVal();

            String typeImageOne = Utility.getTypeImageName(typeOne);

            // Generate Image resource Id by image name in drawable folder
            int drawableResourceId = getResources().getIdentifier(typeImageOne
                    , "drawable", getPackageName());
            mTypeOneImageView.setImageResource(drawableResourceId);
        }

        // Check if there is array index for two types
        if (types.size() > 1){
            // Get second type
            String typeTwo = types.get(1).getVal();

            String typeImageTwo = Utility.getTypeImageName(typeTwo);

            // Generate Image resource Id by image name in drawable folder
            int drawableResourceId = getResources().getIdentifier(typeImageTwo
                    , "drawable", getPackageName());
            mTypeTwoImageView.setImageResource(drawableResourceId);

            mTypeTwoImageView.setVisibility(View.VISIBLE);

        } else {
            mTypeTwoImageView.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close(); // Remember to close Realm when done.
    }

}
