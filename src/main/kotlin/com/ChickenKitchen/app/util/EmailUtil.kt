package com.ChickenKitchen.app.util
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class EmailUtil(
    private val mailSender: JavaMailSender
) {
    fun send(to: String, subject: String, content: String, isHtml: Boolean = false) {
        val mimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true)
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(content, isHtml)
        mailSender.send(mimeMessage)
    }
}
