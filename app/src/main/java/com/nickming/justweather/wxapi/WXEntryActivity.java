package com.nickming.justweather.wxapi;

import android.os.Bundle;
import android.util.Log;

import com.nickming.justweather.base.BaseActivity;
import com.nickming.justweather.common.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Desc:
 * Author:nickming
 * Date:16/4/29
 * Time:00:54
 * E-mail:962570483@qq.com
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler{
    private static final String TAG=WXEntryActivity.class.getSimpleName();


    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID, false);
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onReq(BaseReq arg0) { }

    @Override
    public void onResp(BaseResp resp) {
        Log.i(TAG, "resp.errCode:" + resp.errCode + ",resp.errStr:"
                + resp.errStr);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.i(TAG, "onResp: 成功!");
                //分享成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.i(TAG, "onResp: 取消分享!");
                //分享取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.i(TAG, "onResp: 拒绝分享!");
                //分享拒绝
                break;
            default:
                Log.i(TAG, "onResp: 未知错误!");
                break;
        }
        finish();
    }
}
