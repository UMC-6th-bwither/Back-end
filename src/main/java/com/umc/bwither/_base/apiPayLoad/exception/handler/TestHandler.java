package com.umc.bwither._base.apiPayLoad.exception.handler;

import com.umc.bwither._base.apiPayLoad.code.BaseErrorCode;
import com.umc.bwither._base.apiPayLoad.exception.GeneralException;

public class TestHandler extends GeneralException {

    public TestHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
