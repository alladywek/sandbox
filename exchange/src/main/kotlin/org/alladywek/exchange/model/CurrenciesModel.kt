package org.alladywek.exchange.model

import org.alladywek.exchange.entity.Currency

/**
 * Created by Abbas_Abbasov on 11/20/2017
 */
data class CurrenciesModel(val currencies: List<CurrencyModel>)

data class CurrencyModel(val name: String, val code: String, val symbol: String) {

    companion object {
        @JvmStatic
        fun from(currency: Currency) = CurrencyModel(currency.name, currency.code, currency.symbol)
    }
}