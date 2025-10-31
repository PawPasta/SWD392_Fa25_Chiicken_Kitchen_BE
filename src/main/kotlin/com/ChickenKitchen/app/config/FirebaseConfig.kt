
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
    @param:Value("\${firebase.config.path}")
    val configuredPath: String
) {

    @Bean
    fun firebaseApp(): FirebaseApp {

        // Chỉ đổi khi build image để deploy lên server
        // val serviceAccount =
        //     FileInputStream(System.getenv("FIREBASE_CONFIG_PATH"))

        // Chạy local thì dùng cái này
        val serviceAccount = FileInputStream(configuredPath)

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        return FirebaseApp.initializeApp(options)
    }
}