package com.xxf.baking.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.xxf.baking.Constants;
import com.xxf.baking.R;
import com.xxf.baking.bean.Step;
import com.xxf.baking.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2018/2/14.
 */

public class RecipeStepDetailFragment extends Fragment {

    @BindView(R.id.player_view)
    SimpleExoPlayerView playerView;
    @BindView(R.id.tv_instruction)
    TextView mInstructionTextView;
    @BindView(R.id.btn_next)
    Button mButton;

    private SimpleExoPlayer player;

    public static final int UPDATA_DATA = 1;

    private URL url = null;

    private String jsonResponse;

    private int position = new RecipeDetailFragment().getPosition();

    private String videoUrl = null;

    private Step step = new Step();
    private int stepPosition;

    private Context mContext;

    private long videoPosition = 0;
    private boolean videoReady;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATA_DATA:
                    videoUrl = step.getVideoURL();
                    Uri uri = Uri.parse(videoUrl);
                    exoPlayer(uri);
                    mInstructionTextView.setText(step.getDescription());
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_step_detail,container,false);

        ButterKnife.bind(this, view);
//        fetchData(Constants.API.RECIPE_JSON);
        if (isPad(getContext())){
            hideButton();
        }else {
            next();
        }

        if (savedInstanceState!= null){
            videoPosition = savedInstanceState.getLong("position");
            videoReady = savedInstanceState.getBoolean("readyState");
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        videoPosition = player.getCurrentPosition();
        videoReady = player.getPlayWhenReady();
        outState.putLong("position",videoPosition);
        outState.putBoolean("readyState",videoReady);

    }

    private void next(){
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();

            }
        });
    }

    public void hideButton(){
        mButton.setVisibility(View.GONE);
    }

    private void showDialog(){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getContext());
        normalDialog.setTitle("请选择上一个或下一个").setMessage("查看下一步or上一步");
        normalDialog.setPositiveButton("下一步",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        stepPosition = stepPosition + 1;
                        fetchData(Constants.API.RECIPE_JSON);
                    }
                });
        normalDialog.setNeutralButton("上一步",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (stepPosition == 0){
                            Toast.makeText(getContext(),"已经到第一步，无法再往前",Toast.LENGTH_SHORT).show();
                        }else {
                            stepPosition = stepPosition - 1;
                            fetchData(Constants.API.RECIPE_JSON);
                        }
                    }
                });
        normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        normalDialog.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isPad(getContext())){
            stepPosition = getActivity().getIntent().getIntExtra(getString(R.string.stepPosition),0);
        }


    }

    public void setStepPosition(int position){
        stepPosition = position;
    }

    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private void exoPlayer(Uri uri){

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        /**
         * TrackSelector用来选择磁道，由MediaSource所提供，并会被任意的可用的Renderers所使用，当播放器被创建时，TrackSelector会被注入.
         */
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        /**
         * SimpleExoPlayer单例
         */
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector,loadControl);

        /**
         * 设置用户控制，其实就是屏幕上的功能按钮，默认显示，false则不显示，设置false后可自定义
         *
         * 源码中：Sets whether the playback controls can be shown. If set to {@code false} the playback controls
         * are never visible and are disconnected from the player.
         *
         * @param useController Whether the playback controls can be shown.
         */

        playerView.setUseController(true);
        playerView.requestFocus();

        /**
         * 将播放器添加到视图
         */
        playerView.setPlayer(player);

        /**
         * Estimates bandwidth by listening to data transfers
         */
//        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        /**
         * PS:
         * DefaultUriDataSource - 用于播放本地和网络媒体；
         * AssetDataSource - 用于播放应用中assets文件夹下的媒体。
         */
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getActivity(), "Baking"));

        /**
         * ExoPlayer每帧是通过MediaSource展示的，播放的每一帧必须先创建对应的MediaSource
         *
         * MediaSource 定义了加载、播放媒体，并且可以读取已经加载的媒体，使用ExoPlayer.prepare 可在播放开始传入MediaSource
         */
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
// This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(uri,
                dataSourceFactory, extractorsFactory, null, null);

        /**
         * 无缝循环播放视频
         */
        LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);

        player.prepare(loopingSource);
        /**
         * 开始播放
         */
        player.setPlayWhenReady(videoReady);
        if (videoPosition!=0){
            player.seekTo(videoPosition);
        }
    }

    private void fetchData(final String httpUrl) {

        if (isOnline()) {
            try {
                url = new URL(httpUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                        parseJson(jsonResponse);
                        Message message = new Message();
                        message.what = UPDATA_DATA;
                        mHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

    }

    private void parseJson(String json) throws JSONException {

        JSONArray recipes = new JSONArray(json);
        JSONObject recipe = recipes.getJSONObject(position);

        JSONArray stepJsonArray = recipe.getJSONArray("steps");
            JSONObject stepJsonObject = stepJsonArray.getJSONObject(stepPosition);
            String shortDescription = stepJsonObject.getString("shortDescription");
            String description = stepJsonObject.getString("description");
            String videoURL = stepJsonObject.getString("videoURL");

            step.setShortDescription(shortDescription);
            step.setDescription(description);
            step.setVideoURL(videoURL);

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            fetchData(Constants.API.RECIPE_JSON); // 初始化播放器
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            fetchData(Constants.API.RECIPE_JSON); // 初始化播放器
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            player.release(); // 释放播放器

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            player.release();// 释放播放器
        }
    }
}
