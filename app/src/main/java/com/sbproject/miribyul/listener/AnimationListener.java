package com.sbproject.miribyul.listener;

/**
 * (주)오픈잇 | http://www.openit.co.kr
 * Copyright (c)2006-2017, openit Inc.
 * All rights reserved.
 */
public interface AnimationListener {
    void onStart(int directionIn);

    void onEvent(int directionIn);

    void onEnd(int directionIn);
}
