/*
 * Copyright (C) 2015-2016 Emanuel Moecklin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onegravity.rteditor.api.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.onegravity.rteditor.RTEditorSingleton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This is a basic implementation of the RTVideo interface.
 */
public class RTVideoImpl extends RTMediaImpl implements RTVideo {
    private static final long serialVersionUID = 5966458878874846554L;

    private String mVideoPreviewImage;

    public RTVideoImpl(String filePath) {
        super(filePath);
        RTEditorSingleton singleton = RTEditorSingleton.getInstance();
        this.mVideoPreviewImage = saveSmallImage(filePath, singleton.context);
    }

    @Override
    public void remove() {
        super.remove();
        removeFile(mVideoPreviewImage);
    }

    @Override
    public String getVideoPreviewImage() {
        return mVideoPreviewImage;
    }

    @Override
    public void setVideoPreviewImage(String videoPreviewImage) {
        mVideoPreviewImage = videoPreviewImage;
    }

    @Override
    public int getHeight() {
        return getHeight(mVideoPreviewImage);
    }

    @Override
    public int getWidth() {
        return getWidth(mVideoPreviewImage);
    }

    public String saveSmallImage(String videoPath, Context context) {
        String[] path = videoPath.split("/");
        String[] secondPath = path[path.length - 1].split("\\.");
        secondPath[0] += "_small";
        path[path.length - 1] = secondPath[0] + "." + secondPath[1];

        String newPath = "";
        for (int i = 1; i < path.length; i++)
            newPath += "/" + path[i];

        OutputStream fOutputStream = null;
        File file = new File(newPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            fOutputStream = new FileOutputStream(file);

            Bitmap bitmap = createThumbnailFromPath(videoPath, MediaStore.Images.Thumbnails.MICRO_KIND);
            videoPath = newPath;

            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 320, 240, true);
            resized.compress(Bitmap.CompressFormat.PNG, 40, fOutputStream);

            fOutputStream.flush();
            fOutputStream.close();

            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoPath;
    }

    public Bitmap createThumbnailFromPath(String filePath, int type) {
        return ThumbnailUtils.createVideoThumbnail(filePath, type);
    }
}