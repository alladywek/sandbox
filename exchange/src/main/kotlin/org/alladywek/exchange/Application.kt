package org.alladywek.exchange

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = arrayOf("org.alladywek.exchange"))
@PropertySource(value = "classpath:application.properties")
class Application {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication(Application::class.java).run(*args)
        }
    }
}