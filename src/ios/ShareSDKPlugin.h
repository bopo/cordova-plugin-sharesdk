#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>

#import <ShareSDK/ShareSDK.h>
#import <ShareSDKUI/ShareSDK+SSUI.h>

//继承CDVPlugin类
@interface ShareSDKPlugin : CDVPlugin

//接口方法， command.arguments[0]获取前端传递的参数

//分享
- (void)share:(CDVInvokedUrlCommand *)command;

@end
