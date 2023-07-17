package com.example.todoapp.ui.list_screen

import android.annotation.SuppressLint
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todoapp.App
import com.example.todoapp.datasource.network.connection.ConnectionObserver
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentToDoListBinding
import com.example.todoapp.datasource.local_persistence.SharedPreferencesHelper
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.utils.*
import com.example.todoapp.ui.common.ToDoViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


class ToDoListFragment : Fragment(){

    private lateinit var binding:FragmentToDoListBinding
    lateinit var adapter: ToDoListAdapter
    private val toDoViewModel: ToDoViewModel by viewModels {(requireContext().applicationContext as App).appComponent.viewModelFactory()}
    private var internetState = ConnectionObserver.Status.Unavailable

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentToDoListBinding.inflate(inflater, container, false)
        (requireActivity().application as App).appComponent.inject(this)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toDoViewModel.getList()
        setUpUi()

        lifecycleScope.launch {
            toDoViewModel.list.collect{
                setAdapterItems(it)
                setUpCompleteNum(it.count { it -> it.done }, it.size)
            }
        }
        lifecycleScope.launch {
            toDoViewModel.status.collectLatest{
                updateStatusUI(it)
            }
        }

        internetState = toDoViewModel.status.value
    }

    private fun setUpRecyclerView(){
        adapter= ToDoListAdapter(object : TaskActionListener {
            override fun onTaskDetails(itemId: UUID) {
                openItemEditFragment(itemId.toString())
            }

            override fun onTaskChangeComplete(item: ToDoItem) {
                item.done=!item.done
                if (internetState == ConnectionObserver.Status.Available) {
                    toDoViewModel.updateTaskApi(item)
//                    if(item.done) Toast.makeText(context, getString(R.string.complete_message), Toast.LENGTH_SHORT).show()
//                    else Toast.makeText(context, getString(R.string.incomplete_message), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, getString(R.string.no_connection_message), Toast.LENGTH_LONG).show()
                }
                toDoViewModel.updateTaskDb(item)
                toDoViewModel.loadList()
            }
        })

        binding.recyclerView.adapter=adapter
        val layoutManager= LinearLayoutManager(context)
        binding.recyclerView.layoutManager=layoutManager

        //setSwipeAction()
    }
    private fun setAdapterItems(list: List<ToDoItem>){
        if(toDoViewModel.modeVisibility) adapter.items=list.reversed()
        else adapter.items=list.filter { !it.done }.reversed()
    }

    private fun setUpFloatingButton(){
        binding.addButton.setOnClickListener {
            openItemEditFragment("")
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setUpThemeMenu(){

        when(sharedPreferencesHelper.getTheme()){
            SYSTEM_THEME -> {
                binding.themButton.setImageDrawable(resources.getDrawable(R.drawable.icon_system))
            }
            DAY_THEME -> {
                binding.themButton.setImageDrawable(resources.getDrawable(R.drawable.icon_day))
            }
            NIGHT_THEME -> {
                binding.themButton.setImageDrawable(resources.getDrawable(R.drawable.icon_night))
            }
        }

        val popupMenu = PopupMenu(requireContext(), binding.themButton)
        popupMenu.inflate(R.menu.theme_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.system -> {
                    if(sharedPreferencesHelper.getTheme() != SYSTEM_THEME) {
                        sharedPreferencesHelper.putTheme(SYSTEM_THEME)
                        //AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                        requireActivity().recreate()
                    }
                }
                R.id.day -> {
                    if(sharedPreferencesHelper.getTheme() != DAY_THEME) {
                        sharedPreferencesHelper.putTheme(DAY_THEME)
                        //AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                        requireActivity().recreate()
                    }
                }
                R.id.night -> {
                    if(sharedPreferencesHelper.getTheme() != NIGHT_THEME) {
                        sharedPreferencesHelper.putTheme(NIGHT_THEME)
                        //AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                        requireActivity().recreate()
                    }
                }
            }
            false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true)
        }

        binding.themButton.setOnClickListener {
            popupMenu.show()
        }
    }

    private fun openItemEditFragment(itemId:String){
        val action = ToDoListFragmentDirections.actionToDoListFragmentToToDoItemEditFragment(itemId)
        Navigation.findNavController(binding.root).navigate(action)
    }

    private fun setUpUi(){
        setUpThemeMenu()
        setUpFloatingButton()
        setUpRecyclerView()
        setUpVisibility()
        setUpRefreshLayout()
        setSwipeAction()
    }

    @SuppressLint("SetTextI18n")
    private fun setUpCompleteNum(doneNum:Int, taskNum:Int){
        binding.completeTextView.text=getString(R.string.done_title_text)+" $doneNum / $taskNum"
    }
    private fun setUpRefreshLayout(){
        binding.swipeToRefreshLayout.setOnRefreshListener {
            if (internetState == ConnectionObserver.Status.Available) {
                toDoViewModel.loadList()
                Toast.makeText(context, getString(R.string.updated_message), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, getString(R.string.no_connection_message), Toast.LENGTH_LONG).show()
            }
            binding.swipeToRefreshLayout.isRefreshing=false
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setUpVisibility(){
        if(toDoViewModel.modeVisibility){
            binding.eyeButton.setImageResource(R.drawable.icon_watch)
        }
        else{
            binding.eyeButton.setImageResource(R.drawable.ic_not_watch)
        }

        binding.eyeButton.setOnClickListener {
            if(toDoViewModel.modeVisibility){
                binding.eyeButton.setImageResource(R.drawable.ic_not_watch)
            }
            else{
                binding.eyeButton.setImageResource(R.drawable.icon_watch)
            }
            toDoViewModel.changeMode()
        }
    }

    private fun setSwipeAction(){
        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val item = (viewHolder.itemView.tag as ToDoItem)
                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            deleteTask(item)
                        }
                        ItemTouchHelper.RIGHT -> {
                            item.done=!item.done
                            if (internetState == ConnectionObserver.Status.Available) {
                                toDoViewModel.updateTaskApi(item)
//                                if(item.done) Toast.makeText(context, getString(R.string.complete_message), Toast.LENGTH_SHORT).show()
//                                else Toast.makeText(context, getString(R.string.incomplete_message), Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, getString(R.string.no_connection_message), Toast.LENGTH_LONG).show()
                            }
                            toDoViewModel.updateTaskDb(item)
                            toDoViewModel.loadList()
                        }
                    }
                }

                override fun getSwipeThreshold(viewHolder: ViewHolder) = 0.5f

                private fun showRestoreItemSnackbar(item: ToDoItem, position: Int) {
                    Snackbar.make(binding.recyclerView, "Task deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            if (internetState == ConnectionObserver.Status.Available) {
                                toDoViewModel.restoreTask(item, position)
                            } else {
                                toDoViewModel.restoreTaskDb(item, position)
                                Toast.makeText(context, getString(R.string.no_connection_message), Toast.LENGTH_LONG).show()
                            }
                            toDoViewModel.loadList()
                        }.show()
                }

                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView: View = viewHolder.itemView
                    lateinit var p:Paint
                    if (dX < 0) {
                        p = Paint().also { it.color = resources.getColor(R.color.red) }
                        c.drawRect(
                            itemView.right.toFloat() + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat(),
                            p
                        )
                        val icon: Bitmap =
                            requireContext().getDrawable(R.drawable.icon_delete)!!.toBitmap()
                        val iconMarginRight = (dX * -0.2f).coerceAtMost(70f).coerceAtLeast(0f)
                        c.drawBitmap(
                            icon,
                            itemView.right.toFloat() - iconMarginRight - icon.width,
                            itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height) / 2,
                            p
                        )

                    }
                    if (dX > 0) {
                        p = Paint().also { it.color = resources.getColor(R.color.green) }
                        c.drawRect(
                            itemView.left.toFloat() + dX,
                            itemView.top.toFloat(),
                            itemView.left.toFloat(),
                            itemView.bottom.toFloat(),
                            p
                        )
                        val icon: Bitmap =
                            requireContext().getDrawable(R.drawable.icon_save)!!.toBitmap()

                        val iconMarginLeft = (dX * 0.2f).coerceAtMost(70f).coerceAtLeast(0f)
                        c.drawBitmap(
                            icon,
                            itemView.left.toFloat() + iconMarginLeft,
                            itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height) / 2,
                            p
                        )
                    }

                    // Draw background


                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun deleteTask(item: ToDoItem){
        if (internetState == ConnectionObserver.Status.Available) {
            toDoViewModel.deleteTaskByIdApi(item.id)
            Toast.makeText(context, getString(R.string.delete_message), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, getString(R.string.no_connection_message), Toast.LENGTH_LONG).show()
        }
        toDoViewModel.deleteTaskDb(item)
        toDoViewModel.loadList()
    }

    private fun updateStatusUI(status: ConnectionObserver.Status) {
        when (status) {
            ConnectionObserver.Status.Available -> {
                if (internetState != status) {
                    Toast.makeText(context, getString(R.string.connected_message), Toast.LENGTH_SHORT).show()
                    toDoViewModel.loadList()
                }

            }

            ConnectionObserver.Status.Unavailable -> {

                if (internetState != status) {
                    Toast.makeText(context, getString(R.string.no_connection_message), Toast.LENGTH_LONG).show()
                    toDoViewModel.loadList()
                }
            }

            ConnectionObserver.Status.Losing -> {

                if (internetState != status) {
                    Toast.makeText(context, getString(R.string.weak_connection_message), Toast.LENGTH_SHORT).show()
                }
            }

            ConnectionObserver.Status.Lost -> {

                if (internetState != status) {
                    Toast.makeText(context, getString(R.string.no_connection_message), Toast.LENGTH_SHORT).show()
                }
            }
        }
        internetState = status
    }

}