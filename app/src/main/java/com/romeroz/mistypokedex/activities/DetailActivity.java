package com.romeroz.mistypokedex.activities;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.romeroz.mistypokedex.R;
import com.romeroz.mistypokedex.Utility;
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

    // Pokemon Type
    private ImageView mTypeOneImageView;
    private ImageView mTypeTwoImageView;

    // Pokemon Stats
    private TextView mHpTextView;
    private TextView mAtkTextView;
    private TextView mDefTextView;
    private TextView mSpaTextView;
    private TextView mSpdTextView;
    private TextView mSpeTextView;
    private TextView mTotalTextView;

    private View mHpBarView;
    private View mAtkBarView;
    private View mDefBarView;
    private View mSpaBarView;
    private View mSpdBarView;
    private View mSpeBarView;

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

        // Setup pokemon stats
        setupPokemonStats(pokemon);

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

    private void setupPokemonStats(Pokemon pokemon){
        mHpTextView = (TextView) findViewById(R.id.hp_text_view);
        mAtkTextView = (TextView) findViewById(R.id.atk_text_view);
        mDefTextView = (TextView) findViewById(R.id.def_text_view);
        mSpaTextView = (TextView) findViewById(R.id.spa_text_view);
        mSpdTextView = (TextView) findViewById(R.id.spd_text_view);
        mSpeTextView = (TextView) findViewById(R.id.spe_text_view);
        mTotalTextView = (TextView) findViewById(R.id.total_text_view);

        int hp = pokemon.getBaseStats().getHp();
        int atk = pokemon.getBaseStats().getAttack();
        int def = pokemon.getBaseStats().getDefense();
        int spa = pokemon.getBaseStats().getSpecialAttack();
        int spd = pokemon.getBaseStats().getSpecialDefense();
        // Speed wouldn't parse if set as int for some reason
        int spe = Integer.parseInt(pokemon.getBaseStats().getSpeed());
        int total = hp + atk + def + spa + spd + spe;

        mHpTextView.setText(String.valueOf(hp));
        mAtkTextView.setText(String.valueOf(atk));
        mDefTextView.setText(String.valueOf(def));
        mSpaTextView.setText(String.valueOf(spa));
        mSpdTextView.setText(String.valueOf(spd));
        mSpeTextView.setText(String.valueOf(spe));
        mTotalTextView.setText(String.valueOf(total));

        mHpBarView = findViewById(R.id.hp_bar_view);
        mAtkBarView = findViewById(R.id.atk_bar_view);
        mDefBarView = findViewById(R.id.def_bar_view);
        mSpaBarView = findViewById(R.id.spa_bar_view);
        mSpdBarView = findViewById(R.id.spd_bar_view);
        mSpeBarView = findViewById(R.id.spe_bar_view);

        /**
         * Calculating stat bar widths:
         */

        // Max width of stat bar
        int statBarWidth = (int) getResources().getDimension(R.dimen.stat_bar_width);
        // Pokemon base stats range from 1-255
        int maxBaseStat = 255;

        // Example of how to calculate width of stat bar:
        // hp/255 = x/statBarWidth
        // Cross multiply: x * 255 = hp * statBarWidth
        // Solve: x = (hp * statBarWidth)/255
        int hpBarWidth = (hp * statBarWidth)/maxBaseStat;
        int atkBarWidth = (atk * statBarWidth)/maxBaseStat;
        int defBarWidth = (def * statBarWidth)/maxBaseStat;
        int spaBarWidth = (spa * statBarWidth)/maxBaseStat;
        int spdBarWidth = (spd * statBarWidth)/maxBaseStat;
        int speBarWidth = (spe * statBarWidth)/maxBaseStat;

        mHpBarView.getLayoutParams().width = hpBarWidth;
        mAtkBarView.getLayoutParams().width = atkBarWidth;
        mDefBarView.getLayoutParams().width = defBarWidth;
        mSpaBarView.getLayoutParams().width = spaBarWidth;
        mSpdBarView.getLayoutParams().width = spdBarWidth;
        mSpeBarView.getLayoutParams().width = speBarWidth;

        // Set stat bar colors respectively
        GradientDrawable backgroundGradient = (GradientDrawable)mHpBarView.getBackground();
        backgroundGradient.setColor(ContextCompat.getColor(DetailActivity.this, R.color.stat_hp));
        backgroundGradient = (GradientDrawable)mAtkBarView.getBackground();
        backgroundGradient.setColor(ContextCompat.getColor(DetailActivity.this, R.color.stat_atk));
        backgroundGradient = (GradientDrawable)mDefBarView.getBackground();
        backgroundGradient.setColor(ContextCompat.getColor(DetailActivity.this, R.color.stat_def));
        backgroundGradient = (GradientDrawable)mSpaBarView.getBackground();
        backgroundGradient.setColor(ContextCompat.getColor(DetailActivity.this, R.color.stat_spa));
        backgroundGradient = (GradientDrawable)mSpdBarView.getBackground();
        backgroundGradient.setColor(ContextCompat.getColor(DetailActivity.this, R.color.stat_spd));
        backgroundGradient = (GradientDrawable)mSpeBarView.getBackground();
        backgroundGradient.setColor(ContextCompat.getColor(DetailActivity.this, R.color.stat_spe));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close(); // Remember to close Realm when done.
    }
}
