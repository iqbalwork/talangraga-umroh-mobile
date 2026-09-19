package com.talangraga.umrohmobile.presentation.utils

import com.talangraga.data.AppConfig

/**
 * Resolves full image URL for mobile devices.
 * Handles:
 * - Rewriting 'localhost' / '127.0.0.1' with the active backend host from AppConfig.BASE_URL
 * - Resolving relative upload paths (e.g. '/uploads/...') to absolute URLs
 * - Supporting MinIO and Cloudinary / external S3 URLs
 */
fun resolveImageUrl(url: String?): String? {
    if (url.isNullOrBlank()) return null

    // Local content/file/data URIs (e.g. from ImagePicker)
    if (url.startsWith("data:") || url.startsWith("content:") || url.startsWith("file:")) {
        return url
    }

    val configuredBaseUrl = AppConfig.BASE_URL.trimEnd('/')
    if (configuredBaseUrl.isBlank()) return url

    val isBaseHttps = configuredBaseUrl.startsWith("https://", ignoreCase = true)

    // Extract host and port from configured BASE_URL (e.g., "http://192.168.101.25:8000" -> "192.168.101.25:8000")
    val baseWithoutScheme = configuredBaseUrl
        .removePrefix("http://")
        .removePrefix("https://")
        .removePrefix("HTTP://")
        .removePrefix("HTTPS://")

    val baseHostOnly = baseWithoutScheme.substringBefore(':').substringBefore('/')
    val basePort = if (baseWithoutScheme.contains(':')) {
        baseWithoutScheme.substringAfter(':').substringBefore('/')
    } else {
        if (isBaseHttps) "443" else "80"
    }

    var processedUrl = url.trim()

    // Replace localhost or 127.0.0.1 on port 8000 with current backend host & port
    if (processedUrl.contains("localhost:8000") || processedUrl.contains("127.0.0.1:8000")) {
        val targetHostPort = if (basePort == "80" || basePort == "443") baseHostOnly else "$baseHostOnly:$basePort"
        processedUrl = processedUrl
            .replace("localhost:8000", targetHostPort)
            .replace("127.0.0.1:8000", targetHostPort)
    } else if (processedUrl.contains("localhost:9002") || processedUrl.contains("127.0.0.1:9002")) {
        // MinIO local public endpoint
        processedUrl = processedUrl
            .replace("localhost:9002", "$baseHostOnly:9002")
            .replace("127.0.0.1:9002", "$baseHostOnly:9002")
    } else if (processedUrl.startsWith("http://localhost/") || processedUrl.startsWith("http://127.0.0.1/")) {
        processedUrl = processedUrl
            .replace("http://localhost/", "$configuredBaseUrl/")
            .replace("http://127.0.0.1/", "$configuredBaseUrl/")
    }

    // If already absolute HTTP / HTTPS URL, return processed URL
    if (processedUrl.startsWith("http://", ignoreCase = true) || processedUrl.startsWith("https://", ignoreCase = true)) {
        return processedUrl
    }

    // Relative path resolution
    val cleanPath = if (processedUrl.startsWith("/")) processedUrl else "/$processedUrl"
    return "$configuredBaseUrl$cleanPath"
}
