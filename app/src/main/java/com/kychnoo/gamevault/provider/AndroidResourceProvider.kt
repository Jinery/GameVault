package com.kychnoo.gamevault.provider

import android.content.Context
import com.kychnoo.gamevault.R

class AndroidResourceProvider(private val context: Context) : ResourceProvider {
    override fun getString(resId: Int): String = context.getString(resId)
    override fun getString(resId: Int, vararg args: Any): String = context.getString(resId, *args)

    override fun getHttpCodeMessage(code: Int): String {
        return when (code) {
            in 400..499 -> when (code) {
                400 -> getString(R.string.error_bad_request)
                401 -> getString(R.string.error_unauthorized)
                403 -> getString(R.string.error_forbidden)
                404 -> getString(R.string.error_not_found)
                429 -> getString(R.string.error_too_many_requests)
                else -> getString(R.string.error_client, code.toString())
            }
            in 500..599 -> getString(R.string.error_server, code.toString())
            else -> getString(R.string.error_http_unknown, code.toString())
        }
    }

}