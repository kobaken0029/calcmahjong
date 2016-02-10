package com.kobaken0029.calcmahjong.view.activity;

import android.app.Activity;
import android.os.Bundle;

import com.kobaken0029.calcmahjong.view.dialog.RyukyokuDialog;

public class RyukyokuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RyukyokuDialog().showDialog(RyukyokuActivity.this, this);
    }
}
