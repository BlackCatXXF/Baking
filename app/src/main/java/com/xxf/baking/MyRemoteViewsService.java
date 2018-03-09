package com.xxf.baking;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViewsService;

/**
 * Created by dell on 2018/3/9.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MyRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new MyRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
