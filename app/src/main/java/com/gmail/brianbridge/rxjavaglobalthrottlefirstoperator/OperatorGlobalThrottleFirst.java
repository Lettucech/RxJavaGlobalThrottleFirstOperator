package com.gmail.brianbridge.rxjavaglobalthrottlefirstoperator;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class OperatorGlobalThrottleFirst<T> implements Observable.Operator<T, T> {
	private static long DEFAULT_SKIP_DURATION = 500;
	private static long lastOnNext;
	private final long timeInMilliseconds;
	private final Scheduler scheduler;

	public static void setDefaultSkipDuration(long skipDuration, @NonNull TimeUnit unit) {
		OperatorGlobalThrottleFirst.DEFAULT_SKIP_DURATION = unit.toMillis(skipDuration);
	}

	public OperatorGlobalThrottleFirst() {
		this.timeInMilliseconds = DEFAULT_SKIP_DURATION;
		this.scheduler = Schedulers.computation();
	}

	public OperatorGlobalThrottleFirst(long windowDuration, @NonNull TimeUnit unit) {
		this.timeInMilliseconds = unit.toMillis(windowDuration);
		this.scheduler = Schedulers.computation();
	}

	public OperatorGlobalThrottleFirst(long skipDuration, @NonNull TimeUnit unit, Scheduler scheduler) {
		this.timeInMilliseconds = unit.toMillis(skipDuration);
		this.scheduler = scheduler;
	}

	@Override
	public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
		return new Subscriber<T>(subscriber) {
			@Override
			public void onStart() {
				request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(T v) {
				long now = scheduler.now();
				if (lastOnNext == 0 || now - lastOnNext >= timeInMilliseconds) {
					lastOnNext = now;
					subscriber.onNext(v);
				}
			}

			@Override
			public void onCompleted() {
				subscriber.onCompleted();
			}

			@Override
			public void onError(Throwable e) {
				subscriber.onError(e);
			}

		};
	}
}
