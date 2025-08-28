import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        KoinKt.doInitKoin()
        LoggerKt.debugBuild()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
