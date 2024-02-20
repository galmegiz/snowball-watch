package com.fires.common.config

import com.fires.common.interceptor.AccessTokenVerifyInterceptor
import com.fires.common.interceptor.TokenVerifyInterceptor
import com.fires.common.jackson.Jackson
import com.fires.common.resolver.UserArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.charset.StandardCharsets

@Configuration
class WebConfig(
    private val accessTokenVerifyInterceptor: AccessTokenVerifyInterceptor,
    private val tokenVerifyInterceptor: TokenVerifyInterceptor,
    private val loginStudioUserArgumentResolver: UserArgumentResolver
) : WebMvcConfigurer {
    /*
    SpringBoot Default MessageConverter Setting
    0 = {ByteArrayHttpMessageConverter}
    1 = {StringHttpMessageConverter}
    2 = {ResourceHttpMessageConverter}
    3 = {ResourceRegionHttpMessageConverter}
    4 = {AllEncompassingFormHttpMessageConverter}
    5 = {Jaxb2RootElementHttpMessageConverter}
    6 = {MappingJackson2HttpMessageConverter}
     */
    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        // StringMessageConverter 기본 charset 변경
        if (converters[1] is StringHttpMessageConverter) {
            (converters[1] as StringHttpMessageConverter).defaultCharset = StandardCharsets.UTF_8
        }
        // kotlin data class나 lazyloding 기존 jackson mapper를 사용하면 문제 발생
        // 기존 MappingJackson2HttpMessageConverter앞에 Kotlin용 Mapper 추가
        converters.add(6, MappingJackson2HttpMessageConverter(Jackson.mapper()))
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(tokenVerifyInterceptor)
            .addPathPatterns("/api/v1/**")
            .excludePathPatterns(
                "/api/v1/user/signup",
                "/api/v1/user/login",
                "/api/v1/user/refresh-token",
                "/api/v1/user/email"
            )

        registry.addInterceptor(accessTokenVerifyInterceptor)
            .addPathPatterns(
                "/api/v1/user/signup",
                "/api/v1/user/login"
            )
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("*")
    }

    /**
     * method argument resolver 정의
     */
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginStudioUserArgumentResolver)
    }
}
