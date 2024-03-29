package com.fires.common.exception

class AuthenticationException(errorCode: ErrorCode, cause: Throwable?) : ServiceException(errorCode, cause) {
    constructor(errorCode: ErrorCode) : this(errorCode, null)
}
