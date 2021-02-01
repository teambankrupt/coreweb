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
