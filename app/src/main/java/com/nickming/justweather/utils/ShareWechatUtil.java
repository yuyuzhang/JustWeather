package com.nickming.justweather.utils;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;

/**
 * desc:
 *
 * @author:nickming date:2016/4/25
 * time: 14:35
 * e-mail：962570483@qq.com
 */

public class ShareWechatUtil {

    /**
     * 分享文字内容
     * @param iwxapi
     * @param shareContent
     * @param isToFriend
     */
    public static void shareText(IWXAPI iwxapi, String shareContent, boolean isToFriend) {

        WXTextObject textObject = new WXTextObject();
        textObject.text = shareContent;

        WXMediaMessage mediaMessage = new WXMediaMessage();
        mediaMessage.description = "描述";

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.scene = isToFriend ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }

    /**
     * 构建一个唯一标志
     *
     * @param type
     * @return
     * @author YOLANDA
     */
    private static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : (type + System.currentTimeMillis());
    }
}
