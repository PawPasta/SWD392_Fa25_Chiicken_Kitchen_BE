package com.ChickenKitchen.app.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream


@Configuration
class FirebaseConfig {

    @Bean
    fun firebaseApp(): FirebaseApp {

        //cái này sẽ cần phải thay đổi khi deploy nhá, đổi tên Path lại
        val serviceAccount =
            FileInputStream("src/main/resources/firebase/chicken-kitchen-b7b09-firebase-adminsdk-fbsvc-7787cbc6dc.json")

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        return FirebaseApp.initializeApp(options)
    }
}