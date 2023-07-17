package com.example.todoapp.ui.edit_screen_compose

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.ui.common.ToDoViewModel
import com.example.todoapp.ui.edit_screen.ToDoItemEditFragmentArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ToDoItemEditComposeFragment : Fragment() {

    private val toDoViewModel: ToDoViewModel by viewModels {(requireContext().applicationContext as App).appComponent.viewModelFactory()}
    private val args: ToDoItemEditComposeFragmentArgs by navArgs()
    private var itemId:String=""
    private lateinit var item:ToDoItem

    private fun setTaskById(id: UUID) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                toDoViewModel.getTaskById(id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemId=args.itemId
        if(itemId!="") setTaskById(UUID.fromString(itemId))
        item = toDoViewModel.item.value
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent{
                //SetEditableField(text = item.text, onChanged = )
            }
        }
    }

    @Composable
    private fun SetEditableField(
        text: String,
        onChanged: (String) -> Unit) {
        OutlinedTextField(
            value = "text",
            onValueChange = onChanged,
            singleLine = false,
            modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = 150.dp) // Adjust the maximum height as needed
                .padding(vertical = 10.dp, horizontal = 10.dp)
        )
    }

    @Preview
    @Composable
    private fun SetToolBar(){

    }

    @SuppressLint("ResourceType")
    @Composable
    private fun SetImportanceField(importance: ToDoItem.Importance){
        Column() {
            Text(
                text = resources.getString(R.id.importanceTextView)
            )
            Row{
//                Image(
//                    getImportanceIcon(importance),
//                    modifier = Modifier.size(20.dp)
//                )
                Text(
                    text = importance.name
                )
            }
        }
    }

    private fun getImportanceIcon(importance: ToDoItem.Importance): Int {
        return when(importance){
            ToDoItem.Importance.basic -> {
                0
            }

            ToDoItem.Importance.low -> {
                R.drawable.image_meditation
            }

            ToDoItem.Importance.important -> {
                R.drawable.icon_run
            }
        }
    }

    @Preview
    @Composable
    private fun SetDeadlineField(){

    }

    @Preview
    @Composable
    private fun SetDeleteField(){

    }

}