package com.tipgigz.android;

import net.sourceforge.zbar.Symbol;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.tipgigz.android.scanner.ZBarConstants;
import com.tipgigz.android.scanner.ZBarScannerActivity;

public class TipGigzActivity extends Activity {
	
	private static final int ZBAR_SCANNER_REQUEST = 0;
	private static final int ZBAR_QR_SCANNER_REQUEST = 1;
	
	public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{    
	    if (resultCode == RESULT_OK) 
	    {
	        // Scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT)
	        // Type of the scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT_TYPE)
	        Toast.makeText(this, "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_SHORT).show();
	        Toast.makeText(this, "Scan Result Type = " + data.getStringExtra(ZBarConstants.SCAN_RESULT_TYPE), Toast.LENGTH_SHORT).show();
	        // The value of type indicates one of the symbols listed in Advanced Options below.
	    } else if(resultCode == RESULT_CANCELED) {
	        Toast.makeText(this, "Camera unavailable", Toast.LENGTH_SHORT).show();
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_tip_gigz);

		//final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View scan_qrcode_button = findViewById(R.id.scan_qrcode_button);

		// Set up the user interaction to manually show or hide the system UI.
		scan_qrcode_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				if (isCameraAvailable()) {
		            Intent intent = new Intent(getApplicationContext(), ZBarScannerActivity.class);
		            intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
		            startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
		        } else {
		            Toast.makeText(getApplicationContext(), "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
		        }
				
			}
		});
		
		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.explore_button).setOnTouchListener(mDelayHideTouchListener);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			return false;
		}
	};

}
