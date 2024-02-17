package com.example.coreweb.scheduling.jobs

import org.quartz.Job
import org.quartz.JobDataMap
import org.quartz.JobExecutionContext

class PaymentReminderJob : Job {

    override fun execute(jobContext: JobExecutionContext?) {
        val data: JobDataMap = jobContext?.mergedJobDataMap ?: return
        val message = data.getString("message")
        val phone = data.getString("phone")
        println("$message $phone")
    }

}