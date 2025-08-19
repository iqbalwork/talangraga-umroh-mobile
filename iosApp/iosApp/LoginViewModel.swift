//
//  LoginViewModel.swift
//  iosApp
//
//  Created by Iqbal Fauzi on 15/08/25.
//
import Shared

@MainActor
final class LoginViewModel: ObservableObject {

    // Inject AuthRepository from KMP shared module
    private let authRepository: AuthRepository = KoinHelper().authRepository

    // State yang akan diamati oleh SwiftUI
    @Published private(set) var loginState: LoginState = .idle

//    init(authRepository: AuthRepository) {
//        self.authRepository = authRepository
//    }

    // Fungsi untuk melakukan login
    func login(identifier: String, password: String) {
        loginState = .loading

        Task {
            do {
                // Panggil fungsi loginUser dari repository KMP
                let resource = try await authRepository.loginUser(
                    identifier: identifier,
                    password: password
                )

                // Konversi Resource dari Kotlin ke state Swift
                switch resource {
                case is ResourceSuccess<AuthResponse>:
                    let successResource = resource as! ResourceSuccess<AuthResponse>
                    self.loginState = .success(successResource.data)
                case let errorResource as ResourceError<AuthResponse>:
                    self.loginState = .error(errorResource.message)
                default:
                    // Fallback untuk state yang tidak terduga
                    self.loginState = .error("An unknown error occurred.")
                }
            } catch {
                self.loginState = .error(
                    "Failed to login. Please check your credentials."
                )
            }
        }
    }

}
