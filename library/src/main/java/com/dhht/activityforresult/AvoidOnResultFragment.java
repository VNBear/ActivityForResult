package com.dhht.activityforresult;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvoidOnResultFragment extends Fragment {

    private Map<Integer, PublishSubject<ActivityResultInfo>> mSubjects = new HashMap<>();
    private Map<Integer, AvoidOnResult.Callback> mCallbacks = new HashMap<>();

    public AvoidOnResultFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public Observable<ActivityResultInfo> startForResult(final Intent intent, final int requestCode) {
        final PublishSubject<ActivityResultInfo> subject = PublishSubject.create();
        return subject.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                mSubjects.put(requestCode, subject);
                startActivityForResult(intent, requestCode);
            }
        });
    }

    public void startForResult(Intent intent, int requestCode, AvoidOnResult.Callback callback) {
        mCallbacks.put(requestCode, callback);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //rxjava方式的处理
        //if (mSubjects.size() >0 ){
            PublishSubject<ActivityResultInfo> subject = mSubjects.remove(requestCode);
            if (subject != null) {
                subject.onNext(new ActivityResultInfo(resultCode, data));
                subject.onComplete();
            }
        //}

        //callback方式的处理
        AvoidOnResult.Callback callback = mCallbacks.remove(requestCode);
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, data);
        }
    }
}
