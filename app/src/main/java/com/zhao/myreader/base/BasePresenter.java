package com.zhao.myreader.base;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import static androidx.lifecycle.Lifecycle.State.STARTED;

/**
 * Created by zhao on 2016/10/19.
 */

public abstract class BasePresenter implements LifecycleObserver {

     private Context context;
     private Lifecycle lifecycle;
     private boolean enabled;

     public BasePresenter(Context context, Lifecycle lifecycle) {
          this.context = context;
          this.lifecycle = lifecycle;
          lifecycle.addObserver(this);
          enable();

     }

     protected void enable() {
          enabled = true;
          if (lifecycle.getCurrentState().isAtLeast(STARTED)) {
               // connect if not connected
          }
     }

     protected void unEnable() {
          enabled = false;
          if (lifecycle.getCurrentState().isAtLeast(STARTED)) {
               // connect if not connected
          }
     }

     @OnLifecycleEvent(Lifecycle.Event.ON_START)
     void onsStart() {
          if (enabled) {
              start();
          }
     }

     @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
     void onDestroy() {
          if (enabled) {
               destroy();
          }
     }

     @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
     void onResume() {
          if (enabled) {
               resume();
          }
     }



     @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
     void onPause() {
          if (enabled) {
               pause();
          }
     }




     @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
     void onStop() {
          if (enabled) {
               stop();
          }
     }



     protected abstract void start();

     protected  void destroy(){

     };

     protected  void resume(){

     };

     protected  void pause(){

     };

     protected  void stop(){

     };

     public Context getContext() {
          return context;
     }

     public boolean isEnabled() {
          return enabled;
     }

     public Lifecycle getLifecycle() {
          return lifecycle;
     }
}
