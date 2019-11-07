package com.danchua.codechallenge;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.danchua.codechallenge.db.entity.Result;
import com.danchua.codechallenge.network.Repository;
import com.danchua.codechallenge.utils.ResourceRequestScreen;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements ResourceRequestScreen, View.OnClickListener {

    private MainViewModel mMainViewModel;

    private Toolbar mToolbar;

    private boolean isWishlist = false;

    private AlertDialog mDetailDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainViewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);
        mMainViewModel.init(((CodeChallenge) getApplication()).getRepository());

        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

    }


    /**
     * Show Detailed View of Tracks
     * Created an AlertDialog with Custom
     * then populate Views from Result
     *
     * @param result Model from selected Track
     */
    @Override
    public void showDetailDialog(Result result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.detail_fragment, null);
        builder.setView(view);

        ImageButton imageButtonSave = view.findViewById(R.id.detail_fragment_imagebutton_save);
        ImageView imageViewArtwork = view.findViewById(R.id.detail_fragment_imageview_artwork);
        TextView textViewTrackName = view.findViewById(R.id.detail_fragment_textview_trackname);
        TextView textViewGenre = view.findViewById(R.id.detail_fragment_textview_genre);
        TextView textViewPrice = view.findViewById(R.id.detail_fragment_textview_price);
        TextView textViewLongDesc = view.findViewById(R.id.detail_fragment_textview_longdesc);

        textViewLongDesc.setMovementMethod(new ScrollingMovementMethod());

        mMainViewModel.selectResult(result.getTrackId()).observe(this, selectResult -> {
            if(selectResult!=null) {
                imageButtonSave.setImageResource(R.drawable.ic_wishlist_black_24dp);
                isWishlist = true;
            } else {
                imageButtonSave.setImageResource(R.drawable.ic_wishlist_border_black_24dp);
                isWishlist = false;
            }
            imageButtonSave.setEnabled(true);
        });

        imageButtonSave.setTag(result);
        imageButtonSave.setOnClickListener(this::onClick);

        Glide.with(this)
                .load(result.getArtworkUrl100())
                .apply(new RequestOptions().fitCenter())
                .placeholder(R.drawable.poster_placeholder)
                .into(imageViewArtwork);

        textViewTrackName.setText(result.getTrackName());
        textViewGenre.setText(result.getPrimaryGenreName());
        textViewPrice.setText(String.valueOf(result.getTrackPrice()));

        textViewLongDesc.setText(result.getLongDescription());

        mDetailDialog = builder.show();

    }

    /**
     *
     * Toggle Toolbar back button
     *
     * */
    @Override
    public void toggleToolbarBackButton(boolean toggle) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(toggle);
    }

    @Override
    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    /**
     *
     * Save / remove from wishlist
     * Used LiveData for more accurate response from database
     *
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_fragment_imagebutton_save:
                if(isWishlist) {
                    mMainViewModel.removeResult((Result) v.getTag()).observe(this, result -> {
                        switch (result.status) {

                            case SUCCESS:
                                isWishlist = false;
                                ((ImageButton) v).setImageResource(R.drawable.ic_wishlist_border_black_24dp);
                                v.setEnabled(true);
                                break;
                            case LOADING:
                                v.setEnabled(false);
                                break;
                            case CLIENT_ERROR:
                                break;
                            case SERVER_ERROR:
                                break;
                        }
                    });
                } else {
                    mMainViewModel.saveResult((Result) v.getTag()).observe(this, result -> {
                        switch (result.status) {

                            case SUCCESS:
                                isWishlist = true;
                                ((ImageButton) v).setImageResource(R.drawable.ic_wishlist_black_24dp);
                                v.setEnabled(true);
                                break;
                            case LOADING:
                                v.setEnabled(false);
                                break;
                            case CLIENT_ERROR:
                                break;
                            case SERVER_ERROR:
                                break;
                        }
                    });
                }
                break;
        }
    }

    /**
     *
     * Set Last visit inside onDestroy() method
     *
     * */
    @Override
    protected void onDestroy() {
        mMainViewModel.setLastVisit(new Date());
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
