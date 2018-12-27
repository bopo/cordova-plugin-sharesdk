module.exports = function (context) {
    var fs = context.requireCordovaModule('fs');
    var path = context.requireCordovaModule('path');
    var cordova_util = context.requireCordovaModule("cordova-lib/src/cordova/util")
    var ConfigParser = context.requireCordovaModule('cordova-common').ConfigParser

    // var xml = cordova_util.projectConfig(context.opts.projectRoot);
    // var cfg = new ConfigParser(xml);

    // var packageName = cfg.packageName();

    // var wxapiPath = context.opts.projectRoot + '/platforms/android/src/' + packageName.replace(/\./g, '/') + '/wxapi';
    // var WXEntryActivityPath = wxapiPath + '/WXEntryActivity.java';

    // fs.readFile(context.opts.projectRoot + '/plugins/cordova-plugin-sharesdk/src/android/demo/wxapi/WXEntryActivity.java', 'utf8', function (err, data) {
    //     if (err) throw err;
    //     var result = data.replace(/cn\.sharesdk\.demo/g, packageName);
    //     fs.exists(wxapiPath, function (exists) {
    //         if (!exists) fs.mkdir(wxapiPath);
    //         fs.exists(WXEntryActivityPath, function (fexists) {
    //             fs.writeFile(WXEntryActivityPath, result, 'utf8', function (err) {
    //                 if (err) throw err;
    //             });
    //         });
    //     });
    // });
    // platforms/android/app/src/main/AndroidManifest.xml
    var projectRoot = context.opts.projectRoot;
    var xml = path.join(projectRoot, "platforms", "android", "app", "src", "main", "AndroidManifest.xml");

    fs.readFile(xml, {encoding: "utf-8"}, function(err, data){
        if(err){
            console.info(err);
            throw err;
        }
        if (data.indexOf('android:name="com.mob.MobApplication"') == -1) {
            data = data.replace(/<application/g, '<application android:name="com.mob.MobApplication"');
            fs.writeFileSync(xml, data);
        }
    }); 
}

// android:name="com.mob.MobApplication"