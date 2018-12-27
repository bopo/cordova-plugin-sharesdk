 
Cordova插件，与ionic3插件ionic-native-sharesdk搭配使用

该插件使用ShareSDK作为分享插件，使用版本为ShareSDK For Android v3.1.0，ShareSDK For iOS v4.0.2，目前支持微信、腾讯QQ、新浪微博、邮件、短信 

1、安装

```
ionic cordova plugin add https://github.com/bopo/cordova-plugin-sharesdk2.git --save \
      --variable MOB_APPKEY="xxx" --variable MOB_SECRET="xxx"
      
npm i git+https://github.com/bopo/ionic-native-sharesdk.git
```

2、配置参数
android下修改，`android/ShareSDK/assets/ShareSDK.xml` 将参数修改为自己的参数

 3、参考代码
 ```
import { ShareSDK, ShareSDKOptions } from '@ionic-native/sharesdk';

constructor(private sharesdk: ShareSDK) {}

  // 分享参数 
  const options: ShareSDKOptions = {
    title: 'string',
    titleUrl?: 'string',  // 可选
    text: 'string',
    image?: 'string',     // 可选
    url?: 'string',       // 可选
    siteName?: 'string',  // 可选
    siteUrl?: 'string',   // 可选
  };

  this.sharesdk.share(options).then(result => {
       console.log(result); // Success
    }).catch(error => {
       console.log(error); // Failed
    });
  }

```
