package com.k2udacity.baking.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
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


import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.k2udacity.baking.R;
import com.k2udacity.baking.model.Step;
import com.k2udacity.baking.utils.ImageUtils;
import com.k2udacity.baking.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepDetailsFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String LOG_TAG = StepDetailsFragment.class.getSimpleName();
    @BindView(R.id.nestedscrollview_step)
    NestedScrollView nestedScrollViewStep;
    //
    @BindView(R.id.simpleexoplayerview)
    SimpleExoPlayerView simpleExoPlayerView;
    //
    @BindView(R.id.imageview_step)
    ImageView imageViewStep;
    //
    @BindView(R.id.textview_step_description)
    TextView textViewStepDescription;
    //
    @BindView(R.id.button_prev)
    Button buttonPrev;

    @BindView(R.id.button_next)
    Button buttonNext;

    private static MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;

    private SimpleExoPlayer exoPlayer;
    private StepDetailsOnClickListener listener;
    private String videoUrl;
    private long positionMillis = 0;
    private boolean playWhenReady = true;

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

            setUpPrevAndNextButton(buttonPrev, steps.size(), position, -1);
            setUpPrevAndNextButton(buttonNext, steps.size(), position, 1);

            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            ActionBar actionBar = null;
            if (activity != null) {
                actionBar = activity.getSupportActionBar();
            }

            videoUrl = currentStep.getVideoURL();
            if (!TextUtils.isEmpty(videoUrl) && NetworkUtils.isInternetAvailable(context)) {
                initializeMediaSession(context);

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

//                    nestedScrollViewStep.setVisibility(View.GONE);
                    imageViewStep.setVisibility(View.GONE);
                    textViewStepDescription.setVisibility(View.GONE);
                    buttonPrev.setVisibility(View.GONE);
                    buttonNext.setVisibility(View.GONE);

                } else {
                    imageViewStep.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams layoutParams =
                            (RelativeLayout.LayoutParams) textViewStepDescription.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.BELOW, R.id.simpleexoplayerview);
                    textViewStepDescription.setLayoutParams(layoutParams);
                }
            } else {
                simpleExoPlayerView.setVisibility(View.GONE);
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

            getExpoplayerState(savedInstanceState);
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
            throw new RuntimeException("Unable to get the listener on StepDetailsFragment, more details: " + context.getClass().getSimpleName());
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
        saveExpoplayerState(outState);
        saveScrollState(outState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getScrollState(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer == null) {
            return;
        }
        notePlayerState();
        exoPlayer.setPlayWhenReady(false);
    }

    private void notePlayerState() {
        if (exoPlayer != null) {
            positionMillis = exoPlayer.getCurrentPosition();
            playWhenReady = exoPlayer.getPlayWhenReady();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }

    private void initializeMediaSession(Context context) {
        mediaSession = new MediaSessionCompat(context, LOG_TAG);
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );

        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE
                );

        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(getCallBack());
        mediaSession.setActive(true);

    }

    private void initializePlayer() {
        Context context = getContext();
        if (!TextUtils.isEmpty(videoUrl) && NetworkUtils.isInternetAvailable(context)) {
            if (exoPlayer == null) {
                exoPlayer = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(context),
                        new DefaultTrackSelector(), new DefaultLoadControl());

                simpleExoPlayerView.setPlayer(exoPlayer);
                exoPlayer.addListener(this);

                exoPlayer.setPlayWhenReady(true);
                MediaSource mediaSource = buildMediaSource(Uri.parse(videoUrl));
                exoPlayer.prepare(mediaSource, true, false);
            }

            exoPlayer.seekTo(positionMillis);
            exoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory(getString(R.string.app_name)),
                new DefaultExtractorsFactory(), null, null);
    }

    private void saveScrollState(@NonNull Bundle outState) {
        outState.putIntArray(getString(R.string.scroll_position_key),
                new int[]{nestedScrollViewStep.getScrollX(), nestedScrollViewStep.getScrollY()});
    }

    private void getScrollState(@Nullable Bundle savedInstanceState) {
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

    private void saveExpoplayerState(Bundle outState) {
        if (exoPlayer != null) {
            outState.putLong(getString(R.string.position_millis_key), exoPlayer.getCurrentPosition());
            outState.putBoolean(getString(R.string.play_when_ready_key), playWhenReady);
        }
    }

    private void getExpoplayerState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            positionMillis = savedInstanceState.getLong(getString(R.string.position_millis_key));
            playWhenReady = savedInstanceState.getBoolean(getString(R.string.play_when_ready_key));
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
            stateBuilder.setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(),
                    1f
            );

        } else if (playbackState == ExoPlayer.STATE_READY) {
            stateBuilder.setState(
                    PlaybackStateCompat.STATE_PAUSED,
                    exoPlayer.getCurrentPosition(),
                    1f
            );
        }
        //TODO enable when there is a way to show text -  moving to next step in 5 seconds
//        if (playbackState == ExoPlayer.STATE_ENDED){
//            //player back ended
//            if(buttonNext.getVisibility() == View.VISIBLE){
//                buttonNext.performClick();
//            }
//        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

//    @Override
//    public void onPrepared() {
//        simpleExoPlayerView.start();
//    }

    //Implemented by StepDetails Activity
    public interface StepDetailsOnClickListener {
        void onStepSelected(int position);
    }

    private MediaSessionCompat.Callback getCallBack() {
        return new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                exoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                exoPlayer.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                exoPlayer.seekTo(0);
            }
        };
    }
}
