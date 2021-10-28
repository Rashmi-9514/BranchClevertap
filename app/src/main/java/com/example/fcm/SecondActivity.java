package com.example.fcm;



import android.annotation.SuppressLint;
import android.app.PictureInPictureParams;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Rational;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SecondActivity extends AppCompatActivity {
	VideoDialogFragment fragment1;
	Bundle bundle;
	private SimpleExoPlayer player;
	PlayerView videoView;
	private boolean playWhenReady = true;
	private int currentWindow = 0;
	private long playbackPosition = 0;
	//private final PictureInPictureParams.Builder pictureInPictureParamsBuilder=new PictureInPictureParams.Builder();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);

		TextView txt = findViewById(R.id.textView);

		 bundle = getIntent().getBundleExtra("bundle");
		if (bundle != null) {
			for (String key : bundle.keySet()) {
				Object value = bundle.get(key);
				txt.append(key + ": " + value + "\n\n");
			}
		}
	/*	fragment1 = new VideoDialogFragment();
		Bundle bundle2 = new Bundle();
		bundle2.putString("videourl",bundle.getString("videourl"));
		fragment1.setArguments(bundle);
		fragment1.show(getSupportFragmentManager(), "");

	 */
		pictureInPictureMode();
		videoView = findViewById(R.id.videoView);
		videoView.showController();
//		initializePlayer(bundle.getString("videourl"));
	//	this.enterPictureInPictureMode();
	}

	@Override
	public void onBackPressed() {

		if(fragment1.isVisible())
		{
			fragment1.dismiss();
		}
		else
		{
			super.onBackPressed();
		}
	}
	private void pictureInPictureMode(){
		Rational aspectRatio = new Rational(2, 1);
	//	pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
//		enterPictureInPictureMode(pictureInPictureParamsBuilder.build());
	}


	private void initializePlayer(String url) {
		player = ExoPlayerFactory.newSimpleInstance(SecondActivity.this);
		videoView.setPlayer(player);

		Uri uri = Uri.parse(url);
		MediaSource mediaSource = buildMediaSource(uri);

		player.setPlayWhenReady(playWhenReady);
		player.seekTo(currentWindow, playbackPosition);
		player.prepare(mediaSource, false, false);
	}
	private MediaSource buildMediaSource(Uri uri) {
		// These factories are used to construct two media sources below
		DataSource.Factory dataSourceFactory =
				new DefaultDataSourceFactory(SecondActivity.this, "exoplayer-codelab");
		ProgressiveMediaSource.Factory mediaSourceFactory =
				new ProgressiveMediaSource.Factory(dataSourceFactory);

		// Create a media source using the supplied URI
		MediaSource mediaSource1 = mediaSourceFactory.createMediaSource(uri);

		// Additionally create a media source using an MP3
		// Uri audioUri = Uri.parse(getString(uri);
		// MediaSource mediaSource2 = mediaSourceFactory.createMediaSource(audioUri);

		return new ConcatenatingMediaSource(mediaSource1);
	}

	private void releasePlayer() {
		if (player != null) {
			playbackPosition = player.getCurrentPosition();
			currentWindow = player.getCurrentWindowIndex();
			playWhenReady = player.getPlayWhenReady();
			player.release();
			player = null;
		}
	}
	@Override
	public void onStart() {
		super.onStart();
		if (Util.SDK_INT > 23) {
			//   initializePlayer();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		hideSystemUi();
		if ((Util.SDK_INT <= 23 || player == null)) {
			//   initializePlayer();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (Util.SDK_INT <= 23) {
			releasePlayer();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (Util.SDK_INT > 23) {
			releasePlayer();
		}
	}
	@SuppressLint("InlinedApi")
	private void hideSystemUi() {
		videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	}
}