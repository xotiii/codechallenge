package com.danchua.codechallenge.utils;

import com.danchua.codechallenge.db.entity.Result;

/**
 *
 * Resource screen from MainActivity
 *
 * Ea. Toolbar, AlertDialogs, etc.
 *
 * */
public interface ResourceRequestScreen {

    void showDetailDialog(Result result);

    void toggleToolbarBackButton(boolean toggle);

    void setToolbarTitle(String title);

}
