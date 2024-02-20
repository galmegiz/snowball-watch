package com.fires.common.annotation

import com.fires.common.annotation.validator.MobileTelValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [MobileTelValidator::class])
annotation class MobileTel(
    val message: String = "휴대폰 번호 형식이 잘못되었습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
