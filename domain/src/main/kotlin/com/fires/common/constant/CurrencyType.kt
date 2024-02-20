package com.fires.common.constant

import com.fires.domain.asset.constant.CountryType

enum class CurrencyType {
    KRW, USD, NONE;

    companion object {
        fun fromAssetCountry(countryType: CountryType): CurrencyType {
            return when (countryType) {
                CountryType.KOR -> KRW
                CountryType.USA -> USD
            }
        }
    }
}
