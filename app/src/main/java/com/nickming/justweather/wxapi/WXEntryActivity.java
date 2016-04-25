package com.nickming.justweather.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.nickming.justweather.R;
import com.nickming.justweather.base.BaseActivity;
import com.nickming.justweather.common.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler{
	
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

	private IWXAPI iwxapi;

	private static final String TAG=WXEntryActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wechat_entry);

		iwxapi= WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID,false);

		iwxapi.registerApp(Constants.WECHAT_APP_ID);

		iwxapi.handleIntent(getIntent(),this);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		iwxapi.handleIntent(intent,this);
	}

	@Override
	public void onReq(BaseReq baseReq) {


	}

	@Override
	public void onResp(BaseResp baseResp) {

		switch (baseResp.errCode)
		{
			case BaseResp.ErrCode.ERR_OK:
				Log.i(TAG, "onResp :分享成功!");
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				Log.i(TAG, "onResp :被拒绝");
				break;
			case BaseResp.ErrCode.ERR_UNSUPPORT:
				Log.i(TAG, "onResp :不支持");
				break;
			case BaseResp.ErrCode.ERR_SENT_FAILED:
				Log.i(TAG, "onResp :发送失败");
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				Log.i(TAG, "onResp :取消");
				break;
			default:
				break;
		}

	}
}