package me.bopo.sharesdk;

import android.app.Activity;

import com.mob.MobSDK;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
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
        this.mContext = cordova.getActivity().getApplicationContext();
        
        MobSDK.init(this.activity);
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

            if (appkey != '' && secret != '') {
                MobSDK.init(this.activity, appkey, secret);
            } else {
                MobSDK.init(this.activity);
            }

            return true
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
            // 启动分享GUI
            oks.show(cordova.getActivity());

            return true;
        } catch (JSONException e) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
        } catch (Exception e) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.toString()));
        }

        return false
    }
}


