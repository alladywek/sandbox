package org.alladywek.exchange.extention

import com.google.gson.JsonElement
import com.google.gson.JsonObject

fun JsonObject.toMap(): Map<String, JsonElement> = entrySet().associateBy({ it.key }, { it.value })