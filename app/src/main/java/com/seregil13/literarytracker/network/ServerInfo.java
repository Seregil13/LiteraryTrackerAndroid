package com.seregil13.literarytracker.network;

import com.seregil13.literarytracker.BuildConfig;

import java.util.Locale;

public enum ServerInfo {
    LIGHT_NOVEL("lightnovels"),
    MANGA("manga"),
    BOOKS("books"),
    GENRE("genre");

    String mSubDir;

    ServerInfo(String subdir) {
        this.mSubDir = subdir;
    }

    public String getListUrl() {
        return String.format(Locale.US, "%s/%s/list/", BuildConfig.BASE_URL, this.mSubDir);
    }

    public String getDetailUrl(int id) {
        return String.format(Locale.US, "%s/%s/get/%d", BuildConfig.BASE_URL, this.mSubDir, id);
    }

    public String getCreateUrl() {
        return String.format(Locale.US, "%s/%s/create", BuildConfig.BASE_URL, this.mSubDir);
    }

    public String getUpdateUrl(int id) {
        return String.format(Locale.US, "%s/%s/update/%d", BuildConfig.BASE_URL, this.mSubDir, id);
    }

    public String getExistsUrl(String title) {
        return String.format(Locale.US, "%s/%s/exists/%s", BuildConfig.BASE_URL, this.mSubDir, title);
    }
}
