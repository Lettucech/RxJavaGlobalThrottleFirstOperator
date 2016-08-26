package com.gmail.brianbridge.rxjavaglobalthrottlefirstoperator;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
	public static final String TAG = MainActivity.class.getSimpleName();
	private static final long DEFAULT_SKIP_DURATION_IN_SECOND = 1;

	private TextView mLogTextView;
	private Button mSendButtonA;
	private Button mSendButtonB;
	private String mSendButtonAStringFormat;
	private String mSendButtonBStringFormat;
	private String mLogButtonAStringFormat;
	private String mLogButtonBStringFormat;
	private int mSentCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		OperatorGlobalThrottleFirst.setDefaultSkipDuration(DEFAULT_SKIP_DURATION_IN_SECOND, TimeUnit.SECONDS);

		mSendButtonAStringFormat = getString(R.string.btn_send_a_stringFormat);
		mSendButtonBStringFormat = getString(R.string.btn_send_b_stringFormat);
		mLogButtonAStringFormat = getString(R.string.log_btn_a_stringFormat);
		mLogButtonBStringFormat = getString(R.string.log_btn_b_stringFormat);

		mLogTextView = (TextView) findViewById(R.id.textView_log);
		mSendButtonA = (Button) findViewById(R.id.btn_send_a);
		mSendButtonB = (Button) findViewById(R.id.btn_send_b);

		mSendButtonA.setText(String.format(mSendButtonAStringFormat, mSentCount));
		mSendButtonA.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				send(mLogButtonAStringFormat);
			}
		});
		mSendButtonB.setText(String.format(mSendButtonBStringFormat, mSentCount));
		mSendButtonB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				send(mLogButtonBStringFormat);
			}
		});
	}

	private void send(final String outputStringFormat) {
		Observable.just(mSentCount)
				.lift(new OperatorGlobalThrottleFirst<Integer>())
				.subscribe(new Action1<Integer>() {
					@Override
					public void call(Integer integer) {
						mLogTextView.append(String.format(outputStringFormat, ++mSentCount) + "\n");
						mSendButtonA.setText(String.format(mSendButtonAStringFormat, mSentCount));
						mSendButtonB.setText(String.format(mSendButtonBStringFormat, mSentCount));
					}
				});
	}
}
