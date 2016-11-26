package com.projectmolo.buzzLight;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressWarnings("deprecation")



public class MainActivity extends Activity {

	private boolean lightOn = false;

	private ImageButton button;

	private Camera camera;

	private Parameters param;

	private ImageView bgAnimation1;
	
	private ImageView bgAnimation2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


	}

	protected void onStop() {
		super.onStop();
		if (camera != null) {
			camera.release();
		}
	}

	public void onStart() {
		super.onStart();
		button = (ImageButton) findViewById(R.id.flashon);
		bgAnimation1 = (ImageView) findViewById(R.id.bgcircle);
		final Animation animation1 = AnimationUtils.loadAnimation(this,
				R.anim.scale_animation01);
		bgAnimation1.setVisibility(View.VISIBLE);
		
		bgAnimation2 = (ImageView) findViewById(R.id.bgcircle);
		final Animation animation2 = AnimationUtils.loadAnimation(this,
				R.anim.scale_animation02);
		bgAnimation2.setVisibility(View.VISIBLE);

		Context context = this;

		// prepare intent which is triggered if the
// notification is selected

		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
		Notification n  = new Notification.Builder(this)
				.setContentTitle("Flashlight")
				.setContentText("Running")
				.setSmallIcon(R.drawable.icon)
				.setContentIntent(pIntent)
				.setAutoCancel(true)
				.addAction(R.drawable.icon, "On", pIntent)
				.addAction(R.drawable.icon, "Off", pIntent).build();


		NotificationManager notificationManager =
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		notificationManager.notify(0, n);




		if (!context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA_FLASH)) {
			Toast toastMessage = Toast.makeText(this, "NO FLASH FOUND",
					Toast.LENGTH_LONG);
			toastMessage.show();
			return;
		}
		camera = Camera.open();
		param = camera.getParameters();
		param.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
		camera.setParameters(param);
		camera.startPreview();
		if (lightOn){
			param.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(param);
			
		}
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lightOn) {
					button.setImageResource(R.drawable.flashon);
					param.setFlashMode(Parameters.FLASH_MODE_OFF);
					camera.setParameters(param);

					bgAnimation2.startAnimation(animation2);

					lightOn = false;
				} else {
					
					button.setImageResource(R.drawable.flashoff);
					param.setFlashMode(Parameters.FLASH_MODE_TORCH);
					camera.setParameters(param);
					
					bgAnimation1.startAnimation(animation1);

					lightOn = true;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
