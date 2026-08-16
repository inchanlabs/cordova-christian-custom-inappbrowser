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

    },

    open: function (url, success, error) {

        exec(
            success,
            error,
            "OSCustomInAppBrowser",
            "open",
            [url]
        );

    }

};

module.exports = CustomInAppBrowser;
