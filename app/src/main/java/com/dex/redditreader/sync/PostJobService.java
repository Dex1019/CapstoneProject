package com.dex.redditreader.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

public class PostJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Intent nowIntent = new Intent(getApplicationContext(), PostIntentService.class);
        getApplicationContext().startService(nowIntent);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
