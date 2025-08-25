package org.example.task_app_be_youtube.service

import org.example.task_app_be_youtube.data.Task
import org.example.task_app_be_youtube.data.model.TaskCreateRequest
import org.example.task_app_be_youtube.data.model.TaskDto
import org.example.task_app_be_youtube.data.model.TaskUpdateRequest
import org.example.task_app_be_youtube.exception.BadRequestException
import org.example.task_app_be_youtube.exception.TaskNotFoundExecption
import org.example.task_app_be_youtube.repository.TaskRepository
import org.springframework.data.util.ReflectionUtils
import org.springframework.stereotype.Service
import java.lang.reflect.Field
import java.util.stream.Collectors
import kotlin.jvm.java
import kotlin.reflect.full.memberProperties


@Service
class TaskService(private  val repository: TaskRepository) {


    private fun mappingEntityToDto(task: Task) = TaskDto(
        task.id,
        task.description,
        task.isReminderSet,
        task.isTaskOpen,
        task.createdAt,
        task.priority,
    )

    private fun mappingFromRequestToEntity(task: Task, request: TaskCreateRequest) {
        task.description = request.description
        task.isReminderSet = request.isReminderSet
        task.isTaskOpen = request.isTaskOpen
        task.priority = request.priority
    }


    private fun checkTaskForId(id: Long) {
        if (!repository.existsById(id)) {
            throw TaskNotFoundExecption("Task with the id $id does not exist")
        }
    }

    fun getTaskById(id: Long): TaskDto {
        checkTaskForId(id)
        val task: Task = repository.findTaskById(id)
        return mappingEntityToDto(task)
    }

    fun getAllTasks(): List<TaskDto> =
        repository.findAll().stream().map(this::mappingEntityToDto).collect(Collectors.toList())

    fun getAllOpenTask(): List<TaskDto> =
        repository.queryAllOpenTasks().stream().map(this::mappingEntityToDto).collect(Collectors.toList())

    fun getAllClosedTask(): List<TaskDto> =
        repository.queryAllClosedTasks().stream().map(this::mappingEntityToDto).collect(Collectors.toList())


    fun createTask(request: TaskCreateRequest): TaskDto {
        if (repository.doesDescriptionExist(request.description)) {
            throw BadRequestException("There is already a task with the description ${request.description}")
        }
        val task = Task()
        mappingFromRequestToEntity(task, request)
        val savedTask = repository.save(task)
        return mappingEntityToDto(savedTask)
    }


    fun updateTask(id: Long, request: TaskUpdateRequest): TaskDto {
        checkTaskForId(id)
        val existingTask: Task = repository.findTaskById(id)

        for (prop in TaskUpdateRequest::class.memberProperties) {
            if (prop.get(request) != null) {
                val field: Field? = ReflectionUtils.findField(Task::class.java, prop.name)
                field?.let {
                    it.isAccessible = true
                    ReflectionUtils.setField(it, existingTask, prop.get(request))
                }
            }
        }
            val savedTask: Task = repository.save(existingTask)
            return mappingEntityToDto(savedTask)
    }

    fun deleteTask(id: Long): String {
        checkTaskForId(id)
        repository.deleteById(id)
        return "task with the ID: $id  has been deleted"
    }
}