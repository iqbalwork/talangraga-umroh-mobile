//
//  LoginState.swift
//  iosApp
//
//  Created by Iqbal Fauzi on 15/08/25.
//
import Shared

enum LoginState {
    case idle
    case loading
    case success(AuthResponse)
    case error(String)
}
