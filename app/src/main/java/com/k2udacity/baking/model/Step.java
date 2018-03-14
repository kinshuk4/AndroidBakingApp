package com.k2udacity.baking.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {
    private String id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step(Parcel in) {
        id = in.readString();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public Step(String id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public String toString() {
        return "[id = " + id + ", shortDescription = " + shortDescription + ", description = " + description + ", videoURL = " + videoURL + ", thumbnailURL = " + thumbnailURL + "]";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Step)) return false;

        Step step = (Step) o;

        if (!getId().equals(step.getId())) return false;
        if (getShortDescription() != null ? !getShortDescription().equals(step.getShortDescription()) : step.getShortDescription() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(step.getDescription()) : step.getDescription() != null)
            return false;
        if (getVideoURL() != null ? !getVideoURL().equals(step.getVideoURL()) : step.getVideoURL() != null)
            return false;
        return getThumbnailURL() != null ? getThumbnailURL().equals(step.getThumbnailURL()) : step.getThumbnailURL() == null;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + (getShortDescription() != null ? getShortDescription().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getVideoURL() != null ? getVideoURL().hashCode() : 0);
        result = 31 * result + (getThumbnailURL() != null ? getThumbnailURL().hashCode() : 0);
        return result;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}