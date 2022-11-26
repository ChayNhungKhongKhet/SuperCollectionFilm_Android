package com.phamtantb24.superfilmcollection.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Film implements Serializable {
    private String uuid;
    private String title;
    private String releaseDay;
    private List<String> actors;
    private String director;
    private String language;
    private String banner;
    private int runningTime;
    private String country;

    public Film(String title, String releaseDay, List<String> actors, String director, String language, String banner, int runningTime, String country) {
        this.uuid = UUID.randomUUID().toString();
        this.title = title;
        this.releaseDay = releaseDay;
        this.actors = actors;
        this.director = director;
        this.language = language;
        this.banner = banner;
        this.runningTime = runningTime;
        this.country = country;
    }

    public Film(String uuid, String title, String releaseDay, List<String> actors, String director, String language, String banner, int runningTime, String country) {
        this.uuid = uuid;
        this.title = title;
        this.releaseDay = releaseDay;
        this.actors = actors;
        this.director = director;
        this.language = language;
        this.banner = banner;
        this.runningTime = runningTime;
        this.country = country;
    }

    @Override
    public String toString() {
        return "Film{" +
                "uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", releaseDay='" + releaseDay + '\'' +
                ", actors=" + actors +
                ", director='" + director + '\'' +
                ", language='" + language + '\'' +
                ", banner='" + banner + '\'' +
                ", runningTime=" + runningTime +
                ", country='" + country + '\'' +
                '}';
    }

    public Map<String,Object> toMap() {
        HashMap<String,Object> result = new HashMap<>();
        result.put("actors",actors);
        result.put("banner",banner);
        result.put("country",country);
        result.put("director",director);
        result.put("language",language);
        result.put("releaseDay",releaseDay);
        result.put("runningTime",runningTime);
        result.put("title",title);
        return result;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Film() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDay() {
        return releaseDay;
    }

    public void setReleaseDay(String releaseDay) {
        this.releaseDay = releaseDay;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }


    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }
}
