package org.example.task_app_be_youtube.data

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant
import java.time.LocalDateTime


@Entity
@Table(name = "tasks", uniqueConstraints = [UniqueConstraint(name = "uk_task_description", columnNames = ["description"])])
class Task {

    @Id
    @GeneratedValue(generator = "task_sequences", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "task_sequences", sequenceName = "task_sequences", allocationSize = 1)
    val id: Long = 0

    @NotBlank
    @Column(name = "description", nullable = false, unique = true)
    var description: String = ""

    @Column(name = "is_reminder_set", nullable = false, )
    var isReminderSet: Boolean = false

    @Column(name = "is_task_open", nullable = false)
    var isTaskOpen: Boolean = false

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @NotNull
    @Enumerated(EnumType.STRING)
    var priority: Priority = Priority.LOW
}