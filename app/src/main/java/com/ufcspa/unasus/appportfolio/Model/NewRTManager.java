package com.ufcspa.unasus.appportfolio.Model;

import android.os.Bundle;
import android.util.Log;

import com.onegravity.rteditor.RTManager;
import com.onegravity.rteditor.api.RTApi;
import com.onegravity.rteditor.utils.Constants;

/**
 * Created by desenvolvimento on 04/01/2016.
 */
public class NewRTManager extends RTManager {
    /**
     * @param rtApi              The proxy to "the outside world"
     * @param savedInstanceState If the component is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it most
     */
    public NewRTManager(RTApi rtApi, Bundle savedInstanceState) {
        super(rtApi, savedInstanceState);
    }

    @Override
    /* @inheritDoc */
    public void onPickImage() {
    }

    @Override
	/* @inheritDoc */
    public void onCaptureImage() {
    }
}
