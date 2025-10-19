import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        LoggerKt.debugBuild()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
