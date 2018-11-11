package com.dex.redditreader.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;


public class PostIntentService extends IntentService {
    public PostIntentService() {
        super(PostIntentService.class.getSimpleName());

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        PostSyncJob.getPosts(getApplicationContext());
    }
}
