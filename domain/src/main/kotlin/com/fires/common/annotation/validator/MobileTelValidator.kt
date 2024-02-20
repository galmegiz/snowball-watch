package com.fires.common.annotation.validator

import com.fires.common.annotation.MobileTel
import com.fires.common.constant.TelValidatorConstant.MOBILE_NUMBER_PATTERN
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class MobileTelValidator : ConstraintValidator<MobileTel, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) {
            return true
        }

        return value.matches(MOBILE_NUMBER_PATTERN.toRegex())
    }
}
