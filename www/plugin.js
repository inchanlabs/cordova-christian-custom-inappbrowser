var exec = require("cordova/exec");

var CustomInAppBrowser = {

    isAvailable: function (success, error) {

        exec(
            success,
            error,
            "OSCustomInAppBrowser",
            "isAvailable",
            []
        );

    }

};

module.exports = CustomInAppBrowser;
