package com.k2udacity.baking.model;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.k2udacity.baking.utils.ParcelUtils.obtainParcelForParcelable;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
public class StepTest {
    @Test
    public void testStepIsParcelable() throws Exception {
        Step step = new Step("1", "short desc", "desc", "vid_url", "thumb_url" );
        Parcel parcel = obtainParcelForParcelable(step);

        Step createdFromParcel = Step.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getId(), is("1"));
        assertThat(createdFromParcel.getDescription(), is("desc"));

    }
}