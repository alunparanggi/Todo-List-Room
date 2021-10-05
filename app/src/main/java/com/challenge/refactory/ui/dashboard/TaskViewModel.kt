package com.challenge.refactory.ui.dashboard

import androidx.lifecycle.*
import com.challenge.refactory.data.Task
import com.challenge.refactory.data.TaskDao
import kotlinx.coroutines.launch

class TaskViewModel(private val taskDao: TaskDao)
    :ViewModel() {

    val allTasks: LiveData<List<Task>> = taskDao.getTasks().asLiveData()

    fun retrieveTask(id: Int): LiveData<Task> {
        return taskDao.getTask(id).asLiveData()
    }

    private fun updateTask(task: Task){
        viewModelScope.launch {
            taskDao.update(task)
        }
    }

    fun deleteItem(task: Task){
        viewModelScope.launch {
            taskDao.delete(task)
        }
    }

    fun isEntryValid(name: String, desc: String, date: String, startTime: String, finishTime: String): Boolean {
        if (name.isBlank() || desc.isBlank() || date.isBlank() || startTime.isBlank() || finishTime.isBlank()) {
            return false
        }
        return true
    }

    private fun getUpdatedTaskEntry(
        id: Int,
        name: String,
        desc: String,
        date: String,
        startTime: String,
        finishTime: String,
    ): Task {
        return Task(
            id = id,
            name = name,
            description = desc,
            date = date,
            startTime = startTime,
            finishTime = finishTime
        )
    }

    private fun getNewTaskEntry(name: String, desc: String, date: String, startTime: String, finishTime: String): Task {
        return Task(
            name = name,
            description = desc,
            date = date,
            startTime = startTime,
            finishTime = finishTime,
        )
    }

    private fun insertTask(task: Task){
        viewModelScope.launch {
            taskDao.insert(task)
        }
    }

    fun addNewItem(name: String, desc: String, date: String, startTime: String, finishTime: String) {
        val newTask = getNewTaskEntry(name, desc, date, startTime, finishTime)
        insertTask(newTask)
    }

}

class TaskViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}