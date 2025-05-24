package de.salomax.currencies.repository

import android.content.Context
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.interceptors.LogRequestInterceptor
import com.github.kittinunf.fuel.core.interceptors.LogResponseInterceptor
import com.github.kittinunf.result.Result
import de.salomax.currencies.BuildConfig
import de.salomax.currencies.model.ApiProvider
import de.salomax.currencies.model.Currency
import de.salomax.currencies.model.ExchangeRates
import de.salomax.currencies.model.Timeline
import java.time.LocalDate

object ExchangeRatesService {

    init {
        if (BuildConfig.DEBUG) {
            FuelManager.instance.addRequestInterceptor { LogRequestInterceptor(it) }
            FuelManager.instance.addResponseInterceptor { LogResponseInterceptor(it) }
        }
    }

    /**
     * Get all the current exchange rates from the given api provider. Base will be Euro.
     */
    suspend fun getRates(
        apiProvider: ApiProvider,
        date: LocalDate? = null,
        context: Context? = null
    ): Result<ExchangeRates, FuelError> {
        return apiProvider.getRates(context, date)
    }

    /**
     * Get the historic rates of the past year between the given base and symbol.
     * Won't get all the symbols, as it makes a big difference in transferred data size:
     * ~12 KB for one symbol to ~840 KB for all symbols
     */
    suspend fun getTimeline(
        apiProvider: ApiProvider,
        base: Currency,
        symbol: Currency,
        context: Context? = null
    ): Result<Timeline, FuelError> {
        val endDate = LocalDate.now()
        val startDate = endDate.minusYears(1)
        return apiProvider.getTimeline(context, base, symbol, startDate, endDate)
    }

}
