package com.k2udacity.baking.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.k2udacity.baking.R;
import com.k2udacity.baking.model.Step;
import com.k2udacity.baking.utils.ImageUtils;
import com.k2udacity.baking.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepDetailsFragment extends Fragment implements OnPreparedListener {

    private static final String LOG_TAG = StepDetailsFragment.class.getSimpleName();
    @BindView(R.id.nestedscrollview_step)
    NestedScrollView nestedScrollViewStep;
    //
    @BindView(R.id.videoview_step)
    VideoView videoViewStep;
    //
    @BindView(R.id.imageview_step)
    ImageView imageViewStep;
    //
    @BindView(R.id.textview_step_description)
    TextView textViewStepDescription;
    //
    @BindView(R.id.button_prev)
    Button buttonPreviousStep;

    @BindView(R.id.button_next)
    Button buttonNextStep;

    private StepDetailsOnClickListener listener;
    private String videoUrl;

    public StepDetailsFragment() {

    }

    public static StepDetailsFragment newInstance(Bundle bundle) {
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
        stepDetailsFragment.setArguments(bundle);
        return stepDetailsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Inside onCreateView()");
        View view = inflater.inflate(R.layout.fragment_detail_step, container, false);
        ButterKnife.bind(this, view);

        Context context = getContext();
        Bundle bundle = getArguments();
        List<Step> steps = null;
        int position = -1;

        if (bundle != null) {
            steps = bundle.getParcelableArrayList(getString(R.string.steps_intent_key));
            position = bundle.getInt(getString(R.string.step_position_key));
        } else {
            Toast.makeText(context, R.string.error_unable_to_fetch_any_steps, Toast.LENGTH_SHORT).show();
        }

        if (steps != null) {
            if (position < 0 || position > steps.size()) {
                Toast.makeText(context, R.string.error_unable_to_fetch_step, Toast.LENGTH_SHORT).show();
                return view;
            }
            Step currentStep = steps.get(position);

            if (currentStep == null) {
                Toast.makeText(context, R.string.unable_to_fetch_current_step, Toast.LENGTH_SHORT).show();
                return view;
            }

            textViewStepDescription.setText(currentStep.getDescription());
            setUpPrevAndNextButton(buttonPreviousStep, steps.size(), position, -1);
            setUpPrevAndNextButton(buttonNextStep, steps.size(), position, 1);

            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            ActionBar actionBar = null;
            if (activity != null) {
                actionBar = activity.getSupportActionBar();
            }

            videoUrl = currentStep.getVideoURL();
            if (!TextUtils.isEmpty(videoUrl) && NetworkUtils.isInternetAvailable(context)) {
                initializePlayer(Uri.parse(videoUrl), context);

                Configuration configuration = getResources().getConfiguration();
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                        && !getResources().getBoolean(R.bool.isTablet)) {
                    if (actionBar != null) {
                        actionBar.hide();
                    }

                    if (activity != null) {
                        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }

                    nestedScrollViewStep.setVisibility(View.GONE);
                    buttonPreviousStep.setVisibility(View.GONE);
                    buttonNextStep.setVisibility(View.GONE);
                } else {
                    imageViewStep.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams layoutParams =
                            (RelativeLayout.LayoutParams) textViewStepDescription.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.BELOW, R.id.videoview_step);
                    textViewStepDescription.setLayoutParams(layoutParams);
                }
            } else {
                videoViewStep.setVisibility(View.GONE);
                String recipeName = null;
                if (actionBar != null) {
                    if (actionBar.getTitle() != null) {
                        recipeName = actionBar.getTitle().toString();
                    }
                }

                ImageUtils.setImage(context, currentStep.getThumbnailURL(), imageViewStep, R.drawable.default_recipe);

                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams) textViewStepDescription.getLayoutParams();
                layoutParams.addRule(RelativeLayout.BELOW, R.id.imageview_step);
                textViewStepDescription.setLayoutParams(layoutParams);
            }

        }

        return view;
    }

    private void setUpPrevAndNextButton(Button button, int maxLength, final int currentIndex, final int offset) {
        final int newPosition = currentIndex + offset;
        if (newPosition > -1 && newPosition < maxLength) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onStepSelected(newPosition);
                    if (textViewStepDescription != null) {
                        textViewStepDescription.setText(null);
                    }
                }
            });
        } else {
            button.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StepDetailsOnClickListener) {
            listener = (StepDetailsOnClickListener) context;
        } else {
            throw new RuntimeException("Unable to get the listener on StepDetailsFragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(getString(R.string.scroll_position_key),
                new int[]{nestedScrollViewStep.getScrollX(), nestedScrollViewStep.getScrollY()});
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            final int[] position = savedInstanceState.getIntArray(getString(R.string.scroll_position_key));
            if (position != null) {
                nestedScrollViewStep.post(new Runnable() {
                    public void run() {
                        nestedScrollViewStep.scrollTo(position[0], position[1]);
                    }
                });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context = getContext();
        if (!TextUtils.isEmpty(videoUrl) && NetworkUtils.isInternetAvailable(context)) {
            initializePlayer(Uri.parse(videoUrl), context);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoViewStep == null) {
            return;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initializePlayer(Uri uri, Context context) {
        videoViewStep.setOnPreparedListener(this);
        videoViewStep.setVideoURI(uri);

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory(getString(R.string.app_name)),
                new DefaultExtractorsFactory(), null, null);
    }

    @Override
    public void onPrepared() {
        videoViewStep.start();
    }

    //Implemented by StepDetails Activity
    public interface StepDetailsOnClickListener {
        void onStepSelected(int position);
    }
}
