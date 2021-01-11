package com.example.coreweb.commons

import org.apache.commons.io.FilenameUtils
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.io.File
import java.io.FileInputStream
import java.util.regex.Pattern

class ResourceUtil private constructor() {

    companion object {

        private const val phoneNumberRegexBD = "\\+?(88)?0?1[56789][0-9]{8}\\b" // this is for bangladeshi phone number
        private const val phoneNumberRegex = "^\\+(?:[0-9]●?){6,14}[0-9]\$"
        private const val emailRegex = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})\$"


        @JvmStatic
        fun isEmailValid(email: String): Boolean {
            return Pattern.compile(emailRegex).matcher(email).matches()
        }

        @JvmStatic
        fun isPhoneNumberValid(phoneNumber: String): Boolean {
            return Pattern.compile(phoneNumberRegex).matcher(phoneNumber).matches()
        }

        fun buildFileDownloadResponse(filePath: String, fileName: String): ResponseEntity<Resource> {
            val file = File(filePath)
            return buildDownloadResponse(file, fileName)
        }

        fun buildDownloadResponse(file: File, fileName: String): ResponseEntity<Resource> {
            val resource = InputStreamResource(FileInputStream(file))
            val headers = HttpHeaders()
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + "." + FilenameUtils.getExtension(file.name))
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource)
        }
    }
}
