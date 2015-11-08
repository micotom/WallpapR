package com.dev.funglejunk.wallpapr.rx;

import com.dev.funglejunk.wallpapr.model.info.RawInfo;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxServiceCallback {

    private final static PublishSubject<RawInfo> ps = PublishSubject.create();
    private final static Subject<RawInfo, RawInfo> bus = new SerializedSubject<>(ps);

    public static Observable<RawInfo> observable() {
        return bus;
    }

    public static void report(RawInfo o) {
        bus.onNext(o);
    }

}
