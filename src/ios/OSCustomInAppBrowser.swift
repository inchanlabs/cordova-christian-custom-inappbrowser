import Foundation
import UIKit
import WebKit

@objc(OSCustomInAppBrowser)
class OSCustomInAppBrowser: CDVPlugin {

    @objc(isAvailable:)
    func isAvailable(
        command: CDVInvokedUrlCommand
    ) {

        let result =
            CDVPluginResult(
                status: CDVCommandStatus_OK,
                messageAs: 1
            )

        commandDelegate.send(
            result,
            callbackId: command.callbackId
        )
    }

    @objc(open:)
    func open(
        command: CDVInvokedUrlCommand
    ) {

        guard
            let urlString =
                command.arguments.first as? String,
            let url =
                URL(string: urlString)
        else {

            let result =
                CDVPluginResult(
                    status: CDVCommandStatus_ERROR,
                    messageAs: "URL is required"
                )

            commandDelegate.send(
                result,
                callbackId: command.callbackId
            )

            return
        }

        DispatchQueue.main.async {

            let browser =
                BrowserViewController(
                    url: url
                )

            self.viewController.present(
                browser,
                animated: true
            )

            let result =
                CDVPluginResult(
                    status: CDVCommandStatus_OK
                )

            self.commandDelegate.send(
                result,
                callbackId: command.callbackId
            )
        }
    }
}

class BrowserViewController: UIViewController {

    private let url: URL

    private var webView: WKWebView!

    init(url: URL) {

        self.url = url

        super.init(
            nibName: nil,
            bundle: nil
        )
    }

    required init?(
        coder: NSCoder
    ) {

        fatalError(
            "init(coder:) has not been implemented"
        )
    }

    override func viewDidLoad() {

        super.viewDidLoad()

        view.backgroundColor =
            .systemBackground

        webView =
            WKWebView(
                frame: .zero
            )

        webView.translatesAutoresizingMaskIntoConstraints =
            false

        view.addSubview(
            webView
        )

        NSLayoutConstraint.activate([

            webView.topAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.topAnchor
            ),

            webView.bottomAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.bottomAnchor
            ),

            webView.leadingAnchor.constraint(
                equalTo: view.leadingAnchor
            ),

            webView.trailingAnchor.constraint(
                equalTo: view.trailingAnchor
            )

        ])

        webView.load(
            URLRequest(
                url: url
            )
        )
    }
}
