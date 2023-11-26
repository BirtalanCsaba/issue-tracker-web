package com.issue.tracker.infra.web.auth

import java.io.BufferedReader

object ServletUtils {
    fun readBody(reader: BufferedReader): String {
        val sb = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            sb.append(line)
        }
        return sb.toString()
    }
}