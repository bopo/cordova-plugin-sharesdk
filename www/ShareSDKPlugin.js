var pluginName = "ShareSDKPlugin";
var ShareSDKPlugin = {
};


//分享
ShareSDKPlugin.share = function (successCallback,errorCallback,params) {
    var result = undefined;
    if(Array.isArray(params)){
        result = params;
    }else{
        result = [params]
    }
    cordova.exec(successCallback, errorCallback, pluginName, "share", result);
};
module.exports = ShareSDKPlugin;

