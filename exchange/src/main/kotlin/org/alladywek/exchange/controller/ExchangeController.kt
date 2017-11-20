package org.alladywek.exchange.controller

import org.alladywek.exchange.entity.Currencies
import org.alladywek.exchange.model.CurrenciesModel
import org.alladywek.exchange.model.CurrencyModel.Companion.from
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * Created by Abbas_Abbasov on 11/20/2017
 */
@RestController
class ExchangeController {

    @GetMapping("/currency")
    @ResponseStatus(OK)
    fun getAvailableCurrencies(): CurrenciesModel {
        return CurrenciesModel(
                currencies = Currencies.getAvailable().map { from(it) }
        )
    }
}