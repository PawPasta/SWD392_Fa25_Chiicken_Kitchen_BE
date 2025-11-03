// package com.ChickenKitchen.app.config

// import com.google.auth.oauth2.GoogleCredentials
// import com.google.firebase.FirebaseApp
// import com.google.firebase.FirebaseOptions
// import org.springframework.beans.factory.annotation.Value
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
// import org.springframework.context.annotation.Bean
// import org.springframework.context.annotation.Configuration
// import java.io.FileInputStream


// @Configuration
// class FirebaseConfig {

//     @Bean
//     @ConditionalOnProperty(name = ["firebase.enabled"], havingValue = "true", matchIfMissing = false)
//     fun firebaseApp(
//         @Value("\${firebase.config.path:}") configuredPath: String?
//     ): FirebaseApp {
//         val envPath = System.getenv("FIREBASE_CONFIG_PATH")
//         val path = when {
//             !configuredPath.isNullOrBlank() -> configuredPath
//             !envPath.isNullOrBlank() -> envPath
//             else -> null
//         }

//         require(!path.isNullOrBlank()) {
//             "Firebase enabled but no credentials path provided. Set 'firebase.config.path' or FIREBASE_CONFIG_PATH."
//         }

//         FileInputStream(path).use { serviceAccount ->
//             val options = FirebaseOptions.builder()
//                 .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                 .build()
//             return FirebaseApp.initializeApp(options)
//         }
//     }
// }

package com.ChickenKitchen.app.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value
import java.io.FileInputStream


@Configuration
class FirebaseConfig (
    @Value("\${firebase.config.path}")
    val configuredPath: String
) {

    @Bean
    fun firebaseApp(): FirebaseApp {

        // Chỉ đổi khi build image để deploy lên server
        val serviceAccount =
            FileInputStream(System.getenv("FIREBASE_CONFIG_PATH"))

        // Chạy local thì dùng cái này
        // val serviceAccount = FileInputStream(configuredPath)

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        return FirebaseApp.initializeApp(options)
    }
}