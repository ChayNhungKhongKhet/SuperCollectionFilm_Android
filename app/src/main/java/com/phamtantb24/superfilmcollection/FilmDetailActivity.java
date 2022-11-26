package com.phamtantb24.superfilmcollection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.phamtantb24.superfilmcollection.model.Film;

public class FilmDetailActivity extends DrawerBaseActivity {

    TextView title,releaseDay,language, runningTimeDetail,actors,country,director;
    ImageView poster,backHome,edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        Film film = null;

        mapping();
        if (getIntent() != null && getIntent().getSerializableExtra("film") != null && getIntent().hasExtra("film")) {
            film = (Film) getIntent().getSerializableExtra("film");
            allocateActivityTitle("Film "+film.getTitle());
            Log.i("-------------------------","film"+ film);
            title.setText(film.getTitle());
            releaseDay.setText(film.getReleaseDay());
            language.setText("Language : "+film.getLanguage());
            runningTimeDetail.setText("Running time : "+film.getRunningTime());
            actors.setText(film.getActors().toString().replace("[","").replace("]","").replace(", ","\n").replace(" ",""));
            country.setText("Country : "+film.getCountry());
            director.setText("Director : "+film.getDirector());
            Glide.with(getApplicationContext()).load(film.getBanner()).into(poster);
        }

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Film filmEdit = film;
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditFilmActivity.class);
                intent.putExtra("filmEdit", filmEdit);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
         finish();
    }
    private void mapping() {
        title = findViewById(R.id.film_title);
        releaseDay = findViewById(R.id.release__day);
        language = findViewById(R.id.language);
        runningTimeDetail = findViewById(R.id.running_time_detail);
        actors = findViewById(R.id.starring);
        country = findViewById(R.id.country);
        director = findViewById(R.id.director_detail_film);
        poster = findViewById(R.id.poster_film);
        backHome = findViewById(R.id.backHome);
        edit = findViewById(R.id.editFilmDetail);
    }
}