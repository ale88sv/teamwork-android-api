package com.vegna.teamwork.android.teamwork.helpers;

/**
 * Created by ale on 10/7/17.
 */


import android.util.Log;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;


public class CustomPromise {
    private Promise promise;
    private DeferredObject deferredObject;

    public CustomPromise() {
        this.deferredObject = new DeferredObject();
        this.promise = this.deferredObject.promise();

        new Thread(new Runnable() {
            public void run() {
                try {
                    execute();
                } catch (Exception e) {
                    Log.e("custom-promise",e.getMessage());
                }
            }
        }).start();
    }

    public void resolve(Object obj) {
        if (this.deferredObject.isPending() && !this.deferredObject.isResolved()) {
            this.deferredObject.resolve(obj);
        }
    }

    public void reject(Exception e) {
        Log.e("custom-promise-reject","error:"+e.getMessage());

        if (!this.deferredObject.isRejected()) {
            this.deferredObject.reject(e);
        }
    }

    public Promise then(DoneCallback doneCallback) {
        return promise.then(doneCallback);
    }

    public Promise fail(FailCallback failCallback) {
        return promise.fail(failCallback);
    }

    public void execute() {
    }

    public void then() {
    }
}