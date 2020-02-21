package com.messoft.gaoqin.wanyiyuan.wxapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.MyApplication;
import com.messoft.gaoqin.wanyiyuan.utils.ImageViewUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import java.io.Serializable;


/**
 * 实现微信分享功能的核心类
 *
 * @author chengcj1
 */
public class WechatShareManager implements Serializable {

    private static final int THUMB_SIZE = 150;

    public static final int WECHAT_SHARE_WAY_TEXT = 1;   //文字
    public static final int WECHAT_SHARE_WAY_PICTURE = 2; //图片
    public static final int WECHAT_SHARE_WAY_WEBPAGE = 3;  //链接
    public static final int WECHAT_SHARE_WAY_VIDEO = 4; //视频
    public static final int WECHAT_SHARE_TYPE_TALK = SendMessageToWX.Req.WXSceneSession;  //会话
    public static final int WECHAT_SHARE_TYPE_FRENDS = SendMessageToWX.Req.WXSceneTimeline; //朋友圈

    private static WechatShareManager mInstance;
    private ShareContent mShareContentText, mShareContentPicture, mShareContentWebpag, mShareContentVideo;
    //    private IWXAPI MyApplication.mWxapi;
    private Context mContext;

    private WechatShareManager(Context context) {
        this.mContext = context;
        //初始化数据
//        //初始化微信分享代码
//        initWechatShare(context);
    }

    public boolean isWebchatAvaliable() {
        //检测手机上是否安装了微信
        try {
            mContext.getPackageManager().getPackageInfo("com.tencent.mm", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取WeixinShareManager实例
     * 非线程安全，请在UI线程中操作
     *
     * @return
     */
    public static WechatShareManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WechatShareManager(context);
        }
        return mInstance;
    }

//    private void initWechatShare(Context context){
//        if (MyApplication.mWxapi == null) {
//            MyApplication.mWxapi = WXAPIFactory.createWXAPI(context, Constants.WEIXIN_APP_ID, true);
//        }
//        MyApplication.mWxapi.registerApp(Constants.WEIXIN_APP_ID);
//    }

    /**
     * 通过微信分享
     *
     * @param （文本、图片、链接）
     * @param shareType  分享的类型（朋友圈，会话）
     */
    public void shareByWebchat(ShareContent shareContent, int shareType) {
        switch (shareContent.getShareWay()) {
            case WECHAT_SHARE_WAY_TEXT:
                shareText(shareContent, shareType);
                break;
            case WECHAT_SHARE_WAY_PICTURE:
                sharePicture(shareContent, shareType);
                break;
            case WECHAT_SHARE_WAY_WEBPAGE:
                shareWebPage(shareContent, shareType);
                break;
            case WECHAT_SHARE_WAY_VIDEO:
                shareVideo(shareContent, shareType);
                break;
        }
    }

    private abstract class ShareContent implements Serializable {
        protected abstract int getShareWay();

        protected abstract String getContent();

        protected abstract String getTitle();

        protected abstract String getURL();

        protected abstract int getPictureResource();
    }

    /**
     * 设置分享文字的内容
     *
     * @author chengcj1
     */
    public class ShareContentText extends ShareContent implements Serializable {
        private String content;

        /**
         * 构造分享文字类
         * 分享的文字内容
         */
        public ShareContentText(String content) {
            this.content = content;
        }

        @Override
        protected int getShareWay() {
            return WECHAT_SHARE_WAY_TEXT;
        }

        @Override
        protected String getContent() {
            return content;
        }

        @Override
        protected String getTitle() {
            return null;
        }

        @Override
        protected String getURL() {
            return null;
        }

        @Override
        protected int getPictureResource() {
            return R.mipmap.logo;
        }
    }

    /*
     * 获取文本分享对象
     */
    public ShareContent getShareContentText(String content) {
        if (mShareContentText == null) {
            mShareContentText = new ShareContentText(content);
        }
        return (ShareContentText) mShareContentText;
    }

    /**
     * 设置分享图片的内容
     *
     * @author chengcj1
     */
    public class ShareContentPicture extends ShareContent implements Serializable {
        private int pictureResource;

        public ShareContentPicture(int pictureResource) {
//            if (!StringUtils.isNoEmpty(pictureResource)) {
//                this.pictureResource = Constants.SHARR_DEFALUT_IMG;
//            } else {
//                this.pictureResource = pictureResource;
//            }
            this.pictureResource = pictureResource;
        }

        @Override
        protected int getShareWay() {
            return WECHAT_SHARE_WAY_PICTURE;
        }

        @Override
        protected int getPictureResource() {
            return pictureResource;
        }

        @Override
        protected String getContent() {
            return null;
        }

        @Override
        protected String getTitle() {
            return null;
        }

        @Override
        protected String getURL() {
            return null;
        }
    }

    /*
     * 获取图片分享对象
     */
    public ShareContent getShareContentPicture(int pictureResource) {
        if (mShareContentPicture == null) {
            mShareContentPicture = new ShareContentPicture(pictureResource);
        }
        return (ShareContentPicture) mShareContentPicture;
    }

    /**
     * 设置分享链接的内容
     *
     * @author chengcj1
     */
    public class ShareContentWebpage extends ShareContent implements Serializable {
        private String title;
        private String content;
        private String url;
        private int pictureResource;

        public ShareContentWebpage(String title, String content, String url, int pictureResource) {
            this.title = title;
            this.content = content;
            this.url = url;
//            if (!StringUtils.isNoEmpty(pictureResource)) {
//                this.pictureResource = Constants.SHARR_DEFALUT_IMG;
//            } else {
//                this.pictureResource = pictureResource;
//            }
            this.pictureResource = pictureResource;
        }

        @Override
        protected int getShareWay() {
            return WECHAT_SHARE_WAY_WEBPAGE;
        }

        @Override
        protected String getContent() {
            return content;
        }

        @Override
        protected String getTitle() {
            return title;
        }

        @Override
        protected String getURL() {
            return url;
        }

        @Override
        protected int getPictureResource() {
            return pictureResource;
        }

        public void setTitle(String title) {
            this.title = title == null ? "" : title;
        }

        public void setContent(String content) {
            this.content = content == null ? "" : content;
        }

        public String getUrl() {
            return url == null ? "" : url;
        }

        public void setUrl(String url) {
            this.url = url == null ? "" : url;
        }

        public void setPictureResource(int pictureResource) {
            this.pictureResource = pictureResource;
        }
    }

    /*
     * 获取网页分享对象
     */
    public ShareContent getShareContentWebpag(String title, String content, String url, int pictureResource) {
//        if (mShareContentWebpag == null) {
//        }
        //每次都new个对象，不然数据不清空，或者重新设置数据也行
        mShareContentWebpag = new ShareContentWebpage(title, content, url, pictureResource);
        return (ShareContentWebpage) mShareContentWebpag;
    }

    /**
     * 设置分享视频的内容
     *
     * @author chengcj1
     */
    public class ShareContentVideo extends ShareContent implements Serializable {
        private String url;

        public ShareContentVideo(String url) {
            this.url = url;
        }

        @Override
        protected int getShareWay() {
            return WECHAT_SHARE_WAY_VIDEO;
        }

        @Override
        protected String getContent() {
            return null;
        }

        @Override
        protected String getTitle() {
            return null;
        }

        @Override
        protected String getURL() {
            return url;
        }

        @Override
        protected int getPictureResource() {
            return R.mipmap.logo;
        }
    }

    /*
     * 获取视频分享内容
     */
    public ShareContent getShareContentVideo(String url) {
        if (mShareContentVideo == null) {
            mShareContentVideo = new ShareContentVideo(url);
        }
        return (ShareContentVideo) mShareContentVideo;
    }

    /*
     * 分享文字
     */
    private void shareText(ShareContent shareContent, int shareType) {
        String text = shareContent.getContent();
        //初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        //用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //transaction字段用于唯一标识一个请求
        req.transaction = buildTransaction("textshare");
        req.message = msg;
        //发送的目标场景， 可以选择发送到会话 WXSceneSession 或者朋友圈 WXSceneTimeline。 默认发送到会话。
        req.scene = shareType;
        MyApplication.mWxapi.sendReq(req);
    }

    /*
     * 分享图片
     */
    private void sharePicture(ShareContent shareContent, int shareType) {

//        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), shareContent.getPictureResource());
//        WXImageObject imgObj = new WXImageObject(bitmap);
//
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = imgObj;
//
//        Bitmap thumbBitmap =  Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
//        bitmap.recycle();
//        msg.thumbData = ImageViewUtils.bmpToByteArray(thumbBitmap, true);  //设置缩略图
//
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("imgshareappdata");
//        req.message = msg;
//        req.scene = shareType;
//        MyApplication.mWxapi.sendReq(req);
    }

    /*
     * 分享链接
     */
    @SuppressLint("CheckResult")
    private void shareWebPage(final ShareContent shareContent, final int shareType) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareContent.getURL();
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareContent.getTitle();
        msg.description = shareContent.getContent();

//        Bitmap bt = null;
//        bt = (bitmap == null) ? BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo) : bitmap;
//        msg.thumbData = ImageViewUtils.bmpToByteArray(bitmap, true);
        int pictureResource = shareContent.getPictureResource();
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), pictureResource);
        msg.thumbData = ImageViewUtils.bmpToByteArray(bitmap, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = shareType;
        MyApplication.mWxapi.sendReq(req);

//        //下载图片并转换成bitmap去分享
//        String pictureResource = shareContent.getPictureResource();
//        HttpClient.Builder.getServer().downloadPicFromNet(pictureResource)
//                .subscribeOn(Schedulers.newThread())//在新线程中实现该方法
//                .map(new Function<ResponseBody, byte[]>() {
//
//                    @Override
//                    public byte[] apply(ResponseBody response) {
//                        //下面两行代码 bitmap一直返回null -> https://blog.csdn.net/u011418943/article/details/55667115 和 https://bbs.csdn.net/topics/391876476
//                        //原因 首先response.body()只能使用一次，
//                        // 而BitmapFactory.decodeStream这个方法不知道是不是谷歌的问题他读取的时候是会使用多次这个inputStream，
//                        // 而inputStream又没有缓冲区，所以需要用ByteArrayOutputStream 来先缓冲这个inputStream，再转换。
////                        InputStream inputStream = responseBody.byteStream();
////                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//                        try {
//                            byte[] bytes = response.bytes();
//                            return bytes;
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
////                        InputStream inputStream = response.byteStream();
////                        try {
////                            byte[] data = BitmapUtils.readStream(inputStream);
////                            return data;
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                        }
//                        return null;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())//在主线程中展示
//                .subscribe(new Consumer<byte[]>() {
//                    @Override
//                    public void accept(byte[] bitmap) throws Exception {
//                        DebugUtil.debug("WechatShareManager", "下载图片成功");
////                        Bitmap bt = null;
////                        bt = (bitmap == null) ? BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo) : bitmap;
////                        msg.thumbData = ImageViewUtils.bmpToByteArray(bitmap, true);
//                        msg.thumbData = bitmap;
//                        SendMessageToWX.Req req = new SendMessageToWX.Req();
//                        req.transaction = buildTransaction("webpage");
//                        req.message = msg;
//                        req.scene = shareType;
//                        MyApplication.mWxapi.sendReq(req);
//                    }
//                });
    }

    /*
     * 分享视频
     */
    private void shareVideo(ShareContent shareContent, int shareType) {
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = shareContent.getURL();

        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = shareContent.getTitle();
        msg.description = shareContent.getContent();
        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
//      BitmapFactory.decodeStream(new URL(video.videoUrl).openStream());
        /**
         * 测试过程中会出现这种情况，会有个别手机会出现调不起微信客户端的情况。造成这种情况的原因是微信对缩略图的大小、title、description等参数的大小做了限制，所以有可能是大小超过了默认的范围。
         * 一般情况下缩略图超出比较常见。Title、description都是文本，一般不会超过。
         */
        Bitmap thumbBitmap = Bitmap.createScaledBitmap(thumb, THUMB_SIZE, THUMB_SIZE, true);
        thumb.recycle();
        msg.thumbData = ImageViewUtils.bmpToByteArray(thumbBitmap, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = shareType;
        MyApplication.mWxapi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}