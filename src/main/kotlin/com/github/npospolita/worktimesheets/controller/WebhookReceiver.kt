package com.github.npospolita.worktimesheets.controller

import com.github.kotlintelegrambot.Bot
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WebHookReceiver(
    val bot: Bot
) {
    companion object {
        val log = LoggerFactory.getLogger(WebHookReceiver::class.java.name)
    }


    @PostMapping(path = ["/\${bot.token}"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun receiveWebHook(@RequestBody request: String?) {
        log.info("Received update: {}", request)
        bot.processUpdate(request!!)
        log.info("Update parsed: {}", request)
    }
}
