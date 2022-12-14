package com.first_Ideall.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.getColor
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.first_Ideall.fragments_idea.MindMapEditToolFragment
import com.first_Ideall.R
import com.first_Ideall.custom_views.FlexibleLayout
import com.first_Ideall.enums.ItemPosEnum
import com.first_Ideall.custom_views.MindMapItem
import com.first_Ideall.dialogs.SeriesListDialog
import com.first_Ideall.listeners.MindMapItemClick
import com.first_Ideall.listeners.OnEditTextDialogBtnClick
import com.first_Ideall.rooms.data.IdeaData
import com.first_Ideall.rooms.data.MindMapItemData
import com.first_Ideall.rooms.view_model.IdeaViewModel
import com.first_Ideall.rooms.view_model.MindMapViewModel
import com.first_Ideall.utils.DialogUtils
import com.first_Ideall.utils.PictureUtils

class MindMapActivity : AppCompatActivity() {
    private lateinit var mindMapEditToolbar: Toolbar
    private lateinit var mindMapTitleTextView: TextView
    private lateinit var editFlexibleLayout: FlexibleLayout
    private lateinit var ideaData: IdeaData

    private val ideaViewModel: IdeaViewModel by viewModels()
    private val mindMapViewModel: MindMapViewModel by viewModels()
    private var groupId: Long = -1L
    private var defaultBGColor: Int = -1
    private var defaultTxtColor: Int = -1

    private val itemClickListener = object : MindMapItemClick {
        override fun onClick(item: MindMapItem) {
            MindMapEditToolFragment.newInstance(
                item.getItemData(),
                object : MindMapEditToolFragment.ToolListener {
                    override fun onAdd(text: String) {
                        val itemData = MindMapItemData(
                            null, item.getItemData().itemId, text,
                            ItemPosEnum.LEFT, defaultBGColor, defaultTxtColor, groupId
                        )
                        when (item.itemPosition) {
                            ItemPosEnum.PRIMARY -> {
                                if (item.getLeftChildSize() <= item.getRightChildSize()) {
                                    itemData.itemPos = ItemPosEnum.LEFT
                                } else {
                                    itemData.itemPos = ItemPosEnum.RIGHT
                                }
                            }
                            ItemPosEnum.LEFT -> {
                                itemData.itemPos = ItemPosEnum.LEFT
                            }
                            ItemPosEnum.RIGHT -> {
                                itemData.itemPos = ItemPosEnum.RIGHT
                            }
                        }

                        mindMapViewModel.insert(itemData) { id ->
                            itemData.itemId = id
                        }
                        updateChangesAndModifiedDate()
                        editFlexibleLayout.isAddedItem = true
                    }

                    override fun onDelete() {
                        val parentItem = item.getItemParent()!!
                        when (item.itemPosition) {
                            ItemPosEnum.LEFT -> {
                                var changes = -item.leftTotalHeight
                                if (parentItem.getLeftChildSize() > 1) {
                                    changes -= editFlexibleLayout.verInterval
                                }
                                parentItem.leftChildHeight += changes
                                editFlexibleLayout.changeParentLeftHeight(parentItem)
                                parentItem.getLeftChild().remove(item)
                            }
                            ItemPosEnum.RIGHT -> {
                                var changes = -item.rightTotalHeight
                                if (parentItem.getRightChildSize() > 1) {
                                    changes -= editFlexibleLayout.verInterval
                                }
                                parentItem.rightChildHeight += changes
                                editFlexibleLayout.changeParentRightHeight(parentItem)
                                parentItem.getRightChild().remove(item)
                            }
                            else -> {
                            }
                        }
                        removeFromDB(item)
                        updateChangesAndModifiedDate()
                        editFlexibleLayout.removeChildViews(item)
                    }

                    override fun onEditText(text: String) {
                        item.getItemData().itemText = text
                        mindMapViewModel.update(item.getItemData())
                        updateChangesAndModifiedDate()
                    }

                    override fun onEditBackgroundColor(color: Int) {
                        item.getItemData().backgroundColor = color
                        mindMapViewModel.update(item.getItemData())
                        updateChangesAndModifiedDate()
                    }

                    override fun onEditTextColor(color: Int) {
                        item.getItemData().textColor = color
                        mindMapViewModel.update(item.getItemData())
                        updateChangesAndModifiedDate()
                    }

                }).show(supportFragmentManager, "MIND_MAP_EDIT_TOOL_FRAGMENT")
        }
    }

    private val dialogBtnClickListener = object : OnEditTextDialogBtnClick {
        override fun onClick(text: CharSequence) {
            ideaData.ideaTitle = text.toString()
            updateChangesAndModifiedDate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mind_map)

        defaultBGColor = getColor(this, R.color.soft_orange)
        defaultTxtColor = getColor(this, R.color.black)

        bindViews()

        groupId = intent.getLongExtra("groupId", -1L)
        val isNewIdea = intent.getBooleanExtra("isNewIdea", false)

        setInitialState(isNewIdea)

        ideaViewModel.findOneIdeaById(groupId).observe(this, { idea ->
            if (idea != null) {
                ideaData = idea
                mindMapTitleTextView.text = idea.ideaTitle
                val star = mindMapEditToolbar.menu.findItem(R.id.ideaStarredMenu)
                if (idea.isStarred) {
                    star.setIcon(R.drawable.ic_star)
                } else {
                    star.setIcon(R.drawable.ic_star_border)
                }
            }
        })

        mindMapViewModel.getAllByGroupId(groupId).observe(this, { items ->
            if (items.isNotEmpty()) {
                val mindMapItems = editFlexibleLayout.getItemList()
                val arraySize = mindMapItems.size
                val listSize = items.size
                for (i in 0 until arraySize) {
                    mindMapItems[i].applyChanges(items[i])
                    when (mindMapItems[i].itemPosition) {
                        ItemPosEnum.LEFT -> {
                            if (mindMapItems[i].getItemParent() != null) {
                                editFlexibleLayout.changeParentLeftHeight(mindMapItems[i].getItemParent()!!)
                            }
                        }
                        ItemPosEnum.RIGHT -> {
                            if (mindMapItems[i].getItemParent() != null) {
                                editFlexibleLayout.changeParentRightHeight(mindMapItems[i].getItemParent()!!)
                            }
                        }
                        else -> {

                        }
                    }
                }
                for (i in arraySize until listSize) {
                    val newItem = MindMapItem(this, items[i])
                    newItem.setOnItemClick(itemClickListener)
                    mindMapItems.add(newItem)
                    if (items[i].itemPos != ItemPosEnum.PRIMARY) {
                        val parentItem =
                            mindMapItems.find { item -> (item.getItemData().itemId == items[i].parentId) }
                        editFlexibleLayout.addItem(newItem, parentItem!!)
                    }
                }
            }
        })
    }

    private fun bindViews() {
        mindMapEditToolbar = findViewById(R.id.mindMapEditToolbar)
        mindMapTitleTextView = findViewById(R.id.mindMapTitleTextView)
        editFlexibleLayout = findViewById(R.id.editFlexibleLayout)

        setToolbarListener()
    }

    private fun setInitialState(isNewIdea: Boolean) {
        val primaryItemData = MindMapItemData(
            itemPos = ItemPosEnum.PRIMARY,
            itemText = getString(R.string.new_mind_map),
            backgroundColor = getColor(this, R.color.orange),
            textColor = getColor(this, R.color.white),
            itemGroup = groupId
        )
        val primaryItem = MindMapItem(this, primaryItemData)
        primaryItem.setOnItemClick(itemClickListener)
        editFlexibleLayout.addPrimaryItem(primaryItem)
        editFlexibleLayout.getItemList().add(primaryItem)
        if (isNewIdea) {
            mindMapViewModel.insert(primaryItemData) { id ->
                primaryItem.getItemData().itemId = id
            }
        }
    }

    private fun setToolbarListener() {
        mindMapEditToolbar.setNavigationOnClickListener {
            finish()
        }

        mindMapEditToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.ideaTitleEditMenu -> {
                    DialogUtils.showEditTitleDialog(
                        this,
                        resources.getString(R.string.edit_title_dialog_title),
                        resources.getString(R.string.title_edit_hint),
                        mindMapTitleTextView.text,
                        dialogBtnClickListener
                    )
                }

                R.id.ideaStarredMenu -> {
                    ideaData.isStarred = !ideaData.isStarred
                    updateChangesAndModifiedDate()
                }

                R.id.ideaDeleteMenu -> {
                    MaterialAlertDialogBuilder(this)
                        .setMessage(R.string.idea_delete_dialog_msg)
                        .setNegativeButton(R.string.dialog_cancel, null)
                        .setPositiveButton(R.string.dialog_ok) { _, _ ->
                            mindMapViewModel.deleteByGroupId(groupId)
                            ideaViewModel.delete(ideaData)
                            finish()
                        }
                        .setCancelable(false)
                        .create()
                        .show()
                }

                R.id.ideaSeriesAddMenu -> {
                    SeriesListDialog().apply {
                        arguments = Bundle().apply {
                            putSerializable("ideaData", ideaData)
                        }
                    }.show(
                        supportFragmentManager,
                        "SERIES_LIST_DIALOG"
                    )
                }

                R.id.ideaExportMenu -> runWithPermission()
            }
            true
        }
    }

    private fun removeFromDB(item: MindMapItem) {
        for (child in item.getLeftChild()) {
            removeFromDB(child)
        }
        for (child in item.getRightChild()) {
            removeFromDB(child)
        }
        editFlexibleLayout.getItemList().remove(item)
        mindMapViewModel.delete(item.getItemData())
    }

    private fun updateChangesAndModifiedDate() {
        ideaData.modifiedDate = System.currentTimeMillis()
        ideaViewModel.update(ideaData)
    }

    private val permissionLauncher = registerForActivityResult(RequestPermission()) { isGranted ->
        when {
            isGranted -> showExportDialog()
            else -> Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showExportDialog() {
        val bitmap = PictureUtils.getBitmapImage(editFlexibleLayout)
        val view = LayoutInflater.from(this).inflate(R.layout.export_idea_dialog, null).apply {
            this.findViewById<ImageView>(R.id.previewImage).setImageBitmap(bitmap)
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.export_idea_dialog_title)
            .setView(view)
            .setNegativeButton(R.string.dialog_cancel, null)
            .setPositiveButton(R.string.dialog_save) { dialog, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    PictureUtils.saveBitmapAsImageAfterQ(this, contentResolver, bitmap)
                } else {
                    PictureUtils.saveBitmapAsImageBeforeQ(this, bitmap)
                }
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun runWithPermission() {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val pref = getPreferences(Context.MODE_PRIVATE)
        val isFirstRequest = pref.getBoolean("isFirstRequest", true)

        when {
            checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                showExportDialog()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this, permission) -> {
                permissionLauncher.launch(permission)
            }
            isFirstRequest -> {
                pref.edit().putBoolean("isFirstRequest", false).apply()
                permissionLauncher.launch(permission)
            }
            else -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.permission_dialog_title)
                    .setMessage(R.string.permission_dialog_msg)
                    .setNegativeButton(R.string.dialog_cancel, null)
                    .setPositiveButton(R.string.dialog_setting) { dialog, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            addCategory(Intent.CATEGORY_DEFAULT)
                            data = Uri.parse("package:$packageName")
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }
}