package com.mdove.dependent.common.network

import java.io.IOException

class ForbiddenException(val statusCode: Int) : IOException("Permission Exception: $statusCode")
