@file:Suppress("ComponentNotRegistered")

package com.cherryleafroad.rust.playground.scratch.ui

import com.cherryleafroad.rust.playground.Edition
import com.cherryleafroad.rust.playground.actions.CleanAction
import com.cherryleafroad.rust.playground.actions.ToolbarExecuteAction
import com.cherryleafroad.rust.playground.config.Settings
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBCheckBox
import org.rust.cargo.toolchain.RustChannel

class RustScratchFileEditor(
    val project: Project,
    file: VirtualFile
) : ToolbarTextEditor(project, file, true) {
    private val updateToolbar = { refreshToolbar() }

    override fun addTopActions(toolbarGroup: DefaultActionGroup) {
        toolbarGroup.apply {
            add(ToolbarExecuteAction())
            addSeparator()
            add(CleanAction())
            addSeparator()
            add(CheckCheckBoxAction(file))
            addSeparator()
            add(CleanCheckBoxAction(file))
            addSeparator()
            add(ExpandCheckBoxAction(file))
            addSeparator()
            add(InferCheckBoxAction(file))
            addSeparator()
            add(QuietCheckBoxAction(file))
            addSeparator()
            add(ReleaseCheckBoxAction(file))
            addSeparator()
            add(TestCheckBoxAction(file))
            addSeparator()
            add(VerboseCheckBoxAction(file))
            addSeparator()
            add(OnlyRunCheckBoxAction(file))
        }
    }

    override fun addBottomActions(toolbarGroup: DefaultActionGroup) {
        toolbarGroup.apply {
            add(ToolchainComboBoxAction(file, updateToolbar))
            addSeparator()
            add(EditionComboBoxAction(file, updateToolbar))
            addSeparator()
            add(SrcTextField(file))
            addSeparator()
            add(ArgsTextField(file))
            addSeparator()
            add(ModeTextField(file))
            addSeparator()
            add(CargoOptionTextField(file))
        }
    }
}

class ArgsTextField(
    val file: VirtualFile
) : LabeledTextEditAction("Args", "Aguments to pass to program") {
    private val properties: PropertiesComponent = PropertiesComponent.getInstance()
    override val textfieldLength: Int =  100

    init {
        val saved = properties.getValue("args/${file.path}", "")
        textfield.text = saved
    }

    override fun textChanged(text: String) {
        properties.setValue("args/${file.path}", text)
    }
}

class SrcTextField(
    val file: VirtualFile
) : LabeledTextEditAction("Src", "Additional Rust files to include in the build [Spaced]") {
    private val properties: PropertiesComponent = PropertiesComponent.getInstance()
    override val textfieldLength: Int =  100

    init {
        val saved = properties.getValue("src/${file.path}", "")
        textfield.text = saved
    }

    override fun textChanged(text: String) {
        properties.setValue("src/${file.path}", text)
    }
}

class CargoOptionTextField(
    val file: VirtualFile
) : LabeledTextEditAction("Cargo Options", "Customize flags passed to Cargo") {
    private val properties: PropertiesComponent = PropertiesComponent.getInstance()
    override val textfieldLength: Int =  100

    init {
        val saved = properties.getValue("cargoOptions/${file.path}", "")
        textfield.text = saved
    }

    override fun textChanged(text: String) {
        properties.setValue("cargoOptions/${file.path}", text)
    }
}

class ModeTextField(
    val file: VirtualFile
) : LabeledTextEditAction("Mode", "Specify subcommand to use when calling Cargo [default: run]") {
    private val properties: PropertiesComponent = PropertiesComponent.getInstance()
    override val textfieldLength: Int =  65

    init {
        val saved = properties.getValue("mode/${file.path}", "")
        textfield.text = saved
    }

    override fun textChanged(text: String) {
        properties.setValue("mode/${file.path}", text)
    }
}

class OnlyRunCheckBoxAction(
    val file: VirtualFile
) : SmallBorderCheckboxAction("Only Run Window", "Only use the run window (don't compile in build window) [Faster]") {
    private val properties: PropertiesComponent = PropertiesComponent.getInstance()

    override fun isSelected(e: AnActionEvent): Boolean {
        return properties.getBoolean("userun/${file.path}")
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        properties.setValue("userun/${file.path}", state)
    }

    override fun setPreselected(checkbox: JBCheckBox) {
        val saved = properties.getBoolean("userun/${file.path}", false)
        checkbox.isSelected = saved
    }
}

class ExpandCheckBoxAction(
    val file: VirtualFile
) : SmallBorderCheckboxAction("Expand", "Expand macros in your code") {
    private val properties: PropertiesComponent = PropertiesComponent.getInstance()

    override fun setPreselected(checkbox: JBCheckBox) {
        val saved = properties.getBoolean("expand/${file.path}", false)
        checkbox.isSelected = saved
    }

    override fun isSelected(e: AnActionEvent): Boolean {
        return properties.getBoolean("expand/${file.path}")
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        properties.setValue("expand/${file.path}", state)
    }
}

class VerboseCheckBoxAction(
    val file: VirtualFile
) : SmallBorderCheckboxAction("Verbose", "Set Cargo verbose level") {
    private val properties: PropertiesComponent = PropertiesComponent.getInstance()

    override fun setPreselected(checkbox: JBCheckBox) {
        val saved = properties.getBoolean("verbose/${file.path}", false)
        checkbox.isSelected = saved
    }

    override fun isSelected(e: AnActionEvent): Boolean {
        return properties.getBoolean("verbose/${file.path}")
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        properties.setValue("verbose/${file.path}", state)
    }
}

class TestCheckBoxAction(
    val file: VirtualFile
) : SmallBorderCheckboxAction("Test", "Run test code") {
    private val properties: PropertiesComponent = PropertiesComponent.getInstance()

    override fun setPreselected(checkbox: JBCheckBox) {
        val saved = properties.getBoolean("test/${file.path}", false)
        checkbox.isSelected = saved
    }

    override fun isSelected(e: AnActionEvent): Boolean {
        return properties.getBoolean("test/${file.path}")
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        properties.setValue("test/${file.path}", state)
    }
}

class ReleaseCheckBoxAction(
    val file: VirtualFile
) : SmallBorderCheckboxAction("Release", "Build program in release mode") {
    private val properties: PropertiesComponent = PropertiesComponent.getInstance()

    override fun setPreselected(checkbox: JBCheckBox) {
        val saved = properties.getBoolean("release/${file.path}", false)
        checkbox.isSelected = saved
    }

    override fun isSelected(e: AnActionEvent): Boolean {
        return properties.getBoolean("release/${file.path}")
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        properties.setValue("release/${file.path}", state)
    }
}

class QuietCheckBoxAction(
    val file: VirtualFile
) : SmallBorderCheckboxAction("Quiet", "Disable output from Cargo") {
    private val properties: PropertiesComponent = PropertiesComponent.getInstance()

    override fun setPreselected(checkbox: JBCheckBox) {
        val saved = properties.getBoolean("quiet/${file.path}", false)
        checkbox.isSelected = saved
    }

    override fun isSelected(e: AnActionEvent): Boolean {
        return properties.getBoolean("quiet/${file.path}")
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        properties.setValue("quiet/${file.path}", state)
    }
}

class InferCheckBoxAction(
    val file: VirtualFile
) : SmallBorderCheckboxAction("Infer", "[Experimental] Automatically infers crate dependencies") {
    private val properties: PropertiesComponent = PropertiesComponent.getInstance()

    override fun setPreselected(checkbox: JBCheckBox) {
        val saved = properties.getBoolean("infer/${file.path}", false)
        checkbox.isSelected = saved
    }

    override fun isSelected(e: AnActionEvent): Boolean {
        return properties.getBoolean("infer/${file.path}")
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        properties.setValue("infer/${file.path}", state)
    }
}

class CleanCheckBoxAction(
    val file: VirtualFile
) : SmallBorderCheckboxAction("Clean", "Rebuild the Cargo project without the cache from previous run") {
    private val properties: PropertiesComponent = PropertiesComponent.getInstance()

    override fun setPreselected(checkbox: JBCheckBox) {
        val saved = properties.getBoolean("clean/${file.path}", false)
        checkbox.isSelected = saved
    }

    override fun isSelected(e: AnActionEvent): Boolean {
        return properties.getBoolean("clean/${file.path}")
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        properties.setValue("clean/${file.path}", state)
    }
}

class CheckCheckBoxAction(
    val file: VirtualFile
) : SmallBorderCheckboxAction("Check", "Check for errors in your code") {
    private val properties: PropertiesComponent = PropertiesComponent.getInstance()

    override fun setPreselected(checkbox: JBCheckBox) {
        val saved = properties.getBoolean("check/${file.path}", false)
        checkbox.isSelected = saved
    }

    override fun isSelected(e: AnActionEvent): Boolean {
        return properties.getBoolean("check/${file.path}")
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        properties.setValue("check/${file.path}", state)
    }
}

class ToolchainComboBoxAction(
    val file: VirtualFile,
    val updateToolbar: () -> Unit
) : ComboBoxAction("Toolchain") {
    private val properties: PropertiesComponent = PropertiesComponent.getInstance()
    private val defaultSelection = Settings.getSelectedToolchain()

    override val itemList = RustChannel.values().map { it.name }

    override val preselectedItem: Condition<AnAction> = Condition { action ->
        val saved = properties.getInt("toolchain/${file.path}", defaultSelection.index)
        (action as InnerAction).index == saved
    }

    override var currentSelection: String = defaultSelection.name

    override fun performAction(e: AnActionEvent, index: Int) {
        properties.setValue("toolchain/${file.path}", index, defaultSelection.index)
        currentSelection = RustChannel.values()[index].name
        updateToolbar()
    }
}

class EditionComboBoxAction(
    val file: VirtualFile,
    val updateToolbar: () -> Unit
) : ComboBoxAction("Edition") {
    private val properties: PropertiesComponent = PropertiesComponent.getInstance()

    override val itemList = Edition.values().map { it.myName }

    override val preselectedItem: Condition<AnAction> = Condition { action ->
        val saved = properties.getInt("edition/${file.path}", Edition.DEFAULT.index)
        (action as InnerAction).index == saved
    }

    override var currentSelection: String = Edition.DEFAULT.myName

    override fun performAction(e: AnActionEvent, index: Int) {
        properties.setValue("edition/${file.path}", index, Edition.DEFAULT.index)
        currentSelection = itemList[index]
        updateToolbar()
    }
}