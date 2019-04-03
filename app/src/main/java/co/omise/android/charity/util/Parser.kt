package co.omise.android.charity.util

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


private val GSON = GsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    .create()


internal inline fun <reified A> JsonElement.parseAs() = GSON.fromJson<A>(this, typeToken<A>())

internal inline fun <reified A> A.toJson(): String = GSON.toJson(this)

internal inline fun <reified A> typeToken(): Type = object : TypeToken<A>() {}.type

internal inline fun <reified A> String.parseAs() = GSON.fromJson<A>(this, typeToken<A>())