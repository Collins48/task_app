package org.example.task_app_be_youtube.data.model

import org.example.task_app_be_youtube.data.Priority
import java.time.LocalDateTime
import javax.swing.text.StyledEditorKit

data class TakDto(
    val id: Long,
    val description: String,
    val isReminderSet: Boolean,
    val isTaskOpen: Boolean,
    val createdAt: LocalDateTime,
    val priority: Priority
)
