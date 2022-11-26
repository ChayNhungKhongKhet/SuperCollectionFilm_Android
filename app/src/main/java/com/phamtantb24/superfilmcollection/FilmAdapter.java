package com.phamtantb24.superfilmcollection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.phamtantb24.superfilmcollection.model.Film;

import java.util.List;

public class FilmAdapter extends BaseAdapter {
    private List<Film> films;
    int layout;
    Context context;

    public FilmAdapter(List<Film> films, int layout, Context context) {
        this.films = films;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return films.size();
    }

    @Override
    public Object getItem(int position) {
        return films.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder {
        TextView title,releaseDay,director,runningTime;
        ImageView banner;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.banner = convertView.findViewById(R.id.banner_film);
            viewHolder.runningTime = convertView.findViewById(R.id.runningTime);
            viewHolder.title = convertView.findViewById(R.id.title_film);
            viewHolder.director = convertView.findViewById(R.id.director);
            viewHolder.releaseDay = convertView.findViewById(R.id.release_day);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        Film film = films.get(position);

        Glide.with(context).load(film.getBanner()).into(viewHolder.banner);
        viewHolder.title.setText(film.getTitle());
        viewHolder.director.setText("Director : "+film.getDirector());
        viewHolder.releaseDay.setText(film.getReleaseDay());
        viewHolder.runningTime.setText("Time : "+film.getRunningTime()+"min");
        return convertView;
    }
}