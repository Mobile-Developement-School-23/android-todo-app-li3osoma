package com.example.todoapp.ui.common
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.datasource.network.connection.ConnectionObserver
import com.example.todoapp.datasource.network.connection.NetworkConnectionObserver
import com.example.todoapp.datasource.network.dto.TaskListResponse
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.datasource.network.dto.Resource
import com.example.todoapp.datasource.repository.ToDoRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/*

ViewModel

 */
class ToDoViewModel @Inject constructor(
    private val application: Application,
    private val toDoRepositoryImpl: ToDoRepositoryImpl,
    private val connection: NetworkConnectionObserver
): AndroidViewModel(application) {

    var modeVisibility: Boolean = true
    private var job: Job? = null

    private val _status = MutableStateFlow(ConnectionObserver.Status.Unavailable)
    val status = _status.asStateFlow()

    private val _list = MutableSharedFlow<List<ToDoItem>>()
    val list: SharedFlow<List<ToDoItem>> = _list.asSharedFlow()

    private val _loading = MutableStateFlow<Resource<TaskListResponse>>(Resource.Loading())
    val loading: StateFlow<Resource<TaskListResponse>> = _loading.asStateFlow()


    private var _item = MutableStateFlow(createDefaultTask())
    var item = _item.asStateFlow()


    private fun createDefaultTask(): ToDoItem {
        return ToDoItem(
            UUID.randomUUID(), "", ToDoItem.Importance.basic,
            Date().time, false, "#000000", Date().time, Date().time
        )
    }


    init {
        observeNetwork()
        getList()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            connection.observe().collectLatest {
                _status.emit(it)
            }
        }
    }

    fun changeMode() {
        modeVisibility = !modeVisibility
        job?.cancel()
        getList()
    }

    fun getList() {
        job = viewModelScope.launch(Dispatchers.IO) {
            _list.emitAll(toDoRepositoryImpl.getListDb())
        }
        Log.println(Log.INFO, "LOAD LIST", "")
    }

    fun getTaskById(id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            _item.value = toDoRepositoryImpl.getTaskDb(id)
        }
    }

    fun loadList() {
        if (status.value == ConnectionObserver.Status.Available) {
            _loading.value = Resource.Loading()
            viewModelScope.launch(Dispatchers.IO) {
                _loading.emit(toDoRepositoryImpl.loadList())
            }
        }
    }

    fun addTaskDb(item: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepositoryImpl.addTaskDb(item)
        }
    }

    fun deleteTaskDb(item: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepositoryImpl.deleteTaskDb(item)
        }
    }

    fun updateTaskDb(item: ToDoItem) {
        item.changed_at = Date().time
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepositoryImpl.updateTaskDb(item)
        }
        Log.println(Log.INFO, "UPDATE DB", item.done.toString())
    }

    fun addTaskApi(item: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepositoryImpl.addTaskApi(item)
        }
    }

    fun deleteTaskByIdApi(id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepositoryImpl.deleteTaskByIdApi(id)
        }
        Log.println(Log.INFO, "DELETE API 0", id.toString())
    }

    fun updateTaskApi(item: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepositoryImpl.updateTaskApi(item.id, item)
        }
        Log.println(Log.INFO, "UPDATE API", item.done.toString())
    }


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

//    fun getPosition(item: ToDoItem): Int {
//        var ind = -1
//        lateinit var list1:List<ToDoItem>
//        viewModelScope.launch(Dispatchers.IO) {
//
//            Log.println(Log.INFO, "LIST",list.toString())
//            for (i in list.indices) {
//                if (item.id == list[i].id) {
//                    ind = i
//                    break
//                }
//            }
//        }
//        return ind
//    }

  fun restoreTask(item: ToDoItem, position:Int, list: List<ToDoItem>){
      viewModelScope.launch(Dispatchers.IO) {
          toDoRepositoryImpl.restoreTask(item, position, list)
      }
  }

    fun restoreTaskDb(item: ToDoItem, position:Int, list: List<ToDoItem>){
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepositoryImpl.restoreTaskDb(item, position, list)
        }
    }
}