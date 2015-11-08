package com.dev.funglejunk.wallpapr.rx;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxServiceCallback {

    private final static PublishSubject<String> ps = PublishSubject.create();
    private final static Subject<String, String> bus = new SerializedSubject<>(ps);

    public static Observable<String> observable() {
        return bus;
    }

    public static void report(String o) {
        bus.onNext(o);
    }

}
