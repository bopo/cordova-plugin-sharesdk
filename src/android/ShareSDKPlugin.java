package me.bopo.sharesdk;

import android.app.Activity;
import android.content.Context;

import com.mob.MobSDK;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class ShareSDKPlugin extends CordovaPlugin {

    private Activity activity;
    private Context mContext;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        
        this.activity = cordova.getActivity();
        this.mContext = webView.getContext();
        
        MobSDK.init(this.mContext);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("setup")) {
            return setup(args, callbackContext);
        }

        if (action.equals("share")) {
            return share(args, callbackContext);
        }

        return false;
    }

    /*
     * 初始化
     */
    private boolean setup(JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            final JSONObject params = args.getJSONObject(0);

            String appkey = params.has("appkey") ? params.getString("appkey") : ""; 
            String secret = params.has("secret") ? params.getString("secret") : "";

            if (!appkey.equals("") && !secret.equals("")) {
                MobSDK.init(this.activity, appkey, secret);
            } else {
                MobSDK.init(this.activity);
            }

            return true;
        } catch (JSONException e) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
        } catch (Exception e) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.toString()));
        }

        return false;
    }
    /*
     * 分享
     */
    private boolean share(JSONArray args, CallbackContext callbackContext) throws JSONException {

        try {
            final JSONObject params = args.getJSONObject(0);

            String title = params.has("title") ? params.getString("title") : "";
            String titleUrl = params.has("titleUrl") ? params.getString("titleUrl") : "";
            String url = params.has("url") ? params.getString("url") : "";
            String text = params.has("text") ? params.getString("text") : "";
            String siteUrl = params.has("siteUrl") ? params.getString("siteUrl") : "";
            String siteName = params.has("siteName") ? params.getString("siteName") : "";
            String image = params.has("image") ? params.getString("image") : "";

            OnekeyShare oks = new OnekeyShare();

            //关闭sso授权
            oks.disableSSOWhenAuthorize();
            // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
            //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            if (title.isEmpty()) oks.setTitle(title);
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            if (titleUrl.isEmpty()) oks.setTitleUrl(titleUrl);
            // text是分享文本，所有平台都需要这个字段
            if (text.isEmpty()) oks.setText(text);
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            if (image.isEmpty()) oks.setImagePath(image);//确保SDcard下面存在此张图片
            // url仅在微信（包括好友和朋友圈）中使用
            if (url.isEmpty()) oks.setUrl(url);
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            //oks.setComment("我是测试评论文本");
            // site是分享此内容的网站名称，仅在QQ空间使用
            if (siteName.isEmpty()) oks.setSite(siteName);
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            if (siteUrl.isEmpty()) oks.setSiteUrl(siteUrl);
            //自定义分享的回调想要函数
            oks.setShareContentCustomizeCallback((platform, paramsToShare) -> {
                //点击微信好友
                if ("Wechat".equals(platform.getName())) {
                    //微信分享应用 ,此功能需要微信绕开审核，需要使用项目中的wechatdemo.keystore进行签名打包
                    //由于Onekeyshare没有关于应用分享的参数如setShareType等，我们需要通过自定义 分享来实现
                    //比如下面设置了setTitle,可以覆盖oks.setTitle里面的title值
                    paramsToShare.setTitle("标题");
                    paramsToShare.setText("内容");
                    paramsToShare.setShareType(Platform.SHARE_IMAGE);
//                    paramsToShare.setExtInfo("应用信息");
//                    paramsToShare.setFilePath("xxxxx.apk");
                    paramsToShare.setImagePath(image);
                    Log.d("Wechat", image);
                }
                //点击新浪微博
                if ("SinaWeibo".equals(platform.getName())) {
                    //限制微博分享的文字不能超过20
                    if (paramsToShare.getText().length() > 20) {
                        Toast.makeText(cordova.getActivity(), "分享长度不能超过20个字", Toast.LENGTH_SHORT).show();
                    }
                }
                //点击除了QQ以外的平台
                if (!"QQ".equals(platform.getName())) {
                    Log.i("QQ", "点击了QQ以外的平台");
                }
            });
                        
            // 启动分享GUI
            oks.show(webView.getContext());

            return true;
        } catch (JSONException e) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
        } catch (Exception e) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.toString()));
        }

        return false;
    }
}


