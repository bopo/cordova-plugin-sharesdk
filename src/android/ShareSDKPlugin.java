package ma.kuai.magicican.sharesdk;

import android.app.Activity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.device.Device;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.mob.MobSDK;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.UIHandler;

public class ShareSDKPlugin extends CordovaPlugin {

  private Activity activity;

  public ShareSDKPlugin() {

  }

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    this.activity = cordova.getActivity();

	try{
		ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
		String appKey = appInfo.metaData.getString("Mob-AppKey");
		String appSecret = appInfo.metaData.getString("Mob-AppSecret");

		//调用initSDK初始化
		MobSDK.init(this.activity, appkey, appSecret);

	} catch (PackageManager.NameNotFoundException e) {
		e.printStackTrace();
	}

  }


  @Override
  public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
    if ("share".equals(action)) {
      return share(args, callbackContext);
    }
    return super.execute(action, args, callbackContext);
  }


  /*
  * 分享
  */
  private boolean share(CordovaArgs args, CallbackContext callbackContext) {

		final JSONObject data;
		try {
			  data = args.getJSONObject(0);
			  String title = data.has("title")?  data.getString("title"): "";
			  String titleUrl = data.has("titleUrl")?  data.getString("titleUrl"): "";
			  String url = data.has("url")?  data.getString("url"): "";
			  String text = data.has("text")?  data.getString("text"): "";
			  String siteUrl = data.has("siteUrl")?  data.getString("siteUrl"): "";
			  String siteName = data.has("siteName")?  data.getString("siteName"): "";
			  String image = data.has("image")?  data.getString("image"): "";

			 OnekeyShare oks = new OnekeyShare();
			 //关闭sso授权
			 oks.disableSSOWhenAuthorize(); 

			// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
			 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
			 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
			 if(title.isEmpty())oks.setTitle(title);
			 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
			 if(titleUrl.isEmpty())oks.setTitleUrl(titleUrl);
			 // text是分享文本，所有平台都需要这个字段
			 if(text.isEmpty())oks.setText(text);
			 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
			 if(image.isEmpty())oks.setImagePath(image);//确保SDcard下面存在此张图片
			 // url仅在微信（包括好友和朋友圈）中使用
			 if(url.isEmpty())oks.setUrl(url);
			 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
			 //oks.setComment("我是测试评论文本");
			 // site是分享此内容的网站名称，仅在QQ空间使用
			 if(siteName.isEmpty())oks.setSite(siteName);
			 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
			 if(siteUrl.isEmpty())oks.setSiteUrl(siteUrl);

			// 启动分享GUI
			 oks.show(this.activity);

			 return true;

		} catch (JSONException e) {
		  return false;
		}
		return false;
   }

}


