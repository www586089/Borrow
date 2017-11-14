package com.jyx.android.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyx.android.R;

public class DecoderActivity extends Activity {
	private final static int SCANNIN_GREQUEST_CODE = 1;

	private TextView mTextView ;

	private ImageView mImageView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.decoder_main);

		mTextView = (TextView) findViewById(R.id.result); 
		mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);

		Intent intent=getIntent();
		Bundle bundle = intent.getExtras();
		mTextView.setText(bundle.getString("result"));
//		mImageView.setImageBitmap((Bitmap) intent.getParcelableExtra("bitmap"));
		Button mButton = (Button) findViewById(R.id.button2);
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityHelper.goMain(DecoderActivity.this);

			}
		});

	}




}
