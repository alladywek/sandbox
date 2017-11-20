package org.alladywek.exchange.entity

import com.google.gson.JsonParser
import org.alladywek.exchange.exception.UnsupportedCurrencyCodeException
import org.alladywek.exchange.extention.toMap

/**
 * Created by Abbas_Abbasov on 11/20/2017
 */
object Currencies {

    private val currencies: Map<String, Currency>

    init {
        val jsonString = this.javaClass.classLoader.getResourceAsStream("currencies.json").bufferedReader(Charsets.UTF_8).use { it.readText() }
        currencies = JsonParser().parse(jsonString).asJsonObject
                .toMap()
                .map { (code, props) ->
                    code to Currency(
                            name = props.asJsonObject["name"].asString,
                            code = code,
                            symbol = props.asJsonObject["symbol"].asString)
                }.toMap()
    }

    fun getAvailable(): List<Currency> = currencies.values.toList()

    fun getByCode(code: String): Currency = currencies[code] ?: throw UnsupportedCurrencyCodeException("Unsupported currency code [$code]")
}