package com.k2udacity.baking.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelUtils {
    private ParcelUtils(){

    }
    public static Parcel obtainParcelForParcelable(Parcelable parcelable){
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, parcelable.describeContents());
        parcel.setDataPosition(0);
        return parcel;
    }
}
