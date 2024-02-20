package com.fires.common.annotation.validator

import com.fires.common.annotation.Tel
import com.fires.common.constant.TelValidatorConstant.LAND_LINE_NUMBER_PATTERN
import com.fires.common.constant.TelValidatorConstant.MOBILE_NUMBER_PATTERN
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class TelValidator : ConstraintValidator<Tel, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) {
            return true
        }

        return value.matches(MOBILE_NUMBER_PATTERN.toRegex()) ||
            value.matches(LAND_LINE_NUMBER_PATTERN.toRegex())
    }
}
