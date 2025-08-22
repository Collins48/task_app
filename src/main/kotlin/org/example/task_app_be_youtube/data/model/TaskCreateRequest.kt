package org.example.task_app_be_youtube.data.model

import jakarta.validation.constraints.NotBlank
import org.example.task_app_be_youtube.data.Priority
import java.time.LocalDateTime

data class TaskCreateRequest(
    @NotBlank(message = "Task description cannot be blank")
    val description: String,

    val isReminderSet: Boolean,
    val isTaskOpen: Boolean,

    @NotBlank(message = "Task created_at cannot be blank")
    val createdAt: LocalDateTime,
    val priority: Priority
)
