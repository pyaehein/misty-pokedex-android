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

import io.realm.Realm;

public class DetailActivity extends AppCompatActivity {

    // Activity initialization parameters
    public static final String ARG_POKEMON_ID = "POKEMON_ID";
    public static final String ARG_EXTRA_CURVE = "EXTRA_CURVE";
    private int pokemonId;

    private Toolbar mToolbar;
    private ImageView mPokemonImageView;
    private TextView mNameTextView;

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
        mPokemonImageView = (ImageView) findViewById(R.id.pokemon_image_view);
        mNameTextView = (TextView) findViewById(R.id.name_text_view);

        // Find the pokemon in Realm
        Pokemon pokemon = mRealm.where(Pokemon.class).equalTo("id", pokemonId).findFirst();

        mNameTextView.setText(pokemon.getName());
        getSupportActionBar().setTitle(pokemon.getName());

        // Generate Image resource Id by image name in drawable folder
        int drawableResourceId = getResources().getIdentifier("pokemon_img_" + String.valueOf(pokemonId)
                , "drawable", getPackageName());
        mPokemonImageView.setImageResource(drawableResourceId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this)
                    .inflateTransition(R.transition.move));
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close(); // Remember to close Realm when done.
    }

}
