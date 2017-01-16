package com.veontomo.htmlvalidator.Controller

import com.veontomo.htmlvalidator.Models.*
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.MenuItem
import javafx.scene.control.TableView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.text.Text
import javafx.scene.web.WebView
import javafx.stage.FileChooser
import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

/**
 * A controller that orchestrates execution of available checks of given file.
 */
class MainController : Initializable {
    @FXML private var checkersView: TableView<Report>? = null
    @FXML private var fileNameText: Text? = null
    @FXML private var fileInfoText: Text? = null
    @FXML private var browser: WebView? = null
    @FXML private var menuSelect: MenuItem? = null
    @FXML private var menuAnalyze: MenuItem? = null
    @FXML private var menuClear: MenuItem? = null

    /**
     * Name of the file that stores the preferences
     */
    val pref = "HtmlValidator.history"

    // keyboard shortcut for selecting a file "Ctrl+o"
    val fileSelectShortcut = KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN)
    // keyboard shortcut for analyzing a selected file "Ctrl+Enter"
    val analyzeShortcut = KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN)
    // keyboard shortcut for clearing the results "Ctrl+C"
    val clearShortcut = KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN)



    var selectedFile: File? = null

    private val model  = Model()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        menuSelect!!.accelerator = fileSelectShortcut
        menuAnalyze!!.accelerator = analyzeShortcut
        menuClear!!.accelerator = clearShortcut
        menuSelect!!.setOnAction { onSelect() }
        menuAnalyze!!.setOnAction { onAnalyze() }
        menuClear!!.setOnAction { onClear() }
        enableSelect(true)
        enableAnalyze(false)
        enableClear(false)
        loadItems(model.createEmptyReport())
    }





    /**
     * Perform check of the selected file
     */
    fun onAnalyze() {
        val file = selectedFile
        if (file != null) {
            enableAnalyze(false)
            enableSelect(false)
            val reports = model.performCheck(file)
            loadItems(reports)
            enableAnalyze(true)
            enableSelect(true)
        }
    }

    /**
     * Clear the file
     */
    fun onClear() {
        selectedFile = null
        enableClear(false)
        enableAnalyze(false)
        fileInfoText!!.text = null
        fileNameText!!.text = null
        showFileContent(null)
        loadItems(model.createEmptyReport())
    }


    /**
     * Display the content of the selected file inside the web viewer.
     * @param url
     */
    fun showFileContent(url: String?) {
        browser!!.engine.load(url)
    }

    /**
     * Enable/disable a menu item that is used to analyze the file
     * @param isEnabled true to enable, false to disable
     */
    fun enableAnalyze(isEnabled: Boolean) {
        menuAnalyze!!.isDisable = !isEnabled
    }

    /**
     * Enable/disable a menu item that is used to select a file
     * @param isEnabled true to enable, false to disable
     */
    fun enableSelect(isEnabled: Boolean) {
        menuSelect!!.isDisable = !isEnabled
    }

    /**
     * Enable/disable a menu item that is used to clear the result of analysis
     * @param isEnabled true to enable, false to disable
     */
    fun enableClear(isEnabled: Boolean) {
        menuClear!!.isDisable = !isEnabled
    }

    /**
     * Load the items in to the list view
     * @param items
     */
    fun loadItems(items: List<Report>) {
        checkersView!!.items = FXCollections.observableArrayList(items)
    }


    fun onSelect() {
        val allowedExtensions = listOf("html", "htm")
        val last = readLastUsedFileName()
        val fileChooser = FileChooser()
        if (last != null) {
            val file = File(last)
            if (file.exists() && file.isDirectory)
                fileChooser.initialDirectory = file
        }
        fileChooser.extensionFilters.addAll(
                FileChooser.ExtensionFilter("html", allowedExtensions.map { "*.$it" })
        )
        fileChooser.title = "Select a file"
        val file = fileChooser.showOpenDialog(fileInfoText!!.scene.window)
//        val file = fileChooser.showOpenDialog(Stage())

        if (file?.exists() ?: false) {
            if (allowedExtensions.contains(file.extension)) {
                selectedFile = file
                showFileName(file!!.path)
                showFileContent(file.toURI().toURL().toExternalForm())
                enableAnalyze(true)
                onFileSelected(file)
                enableClear(true)
                showFileInfo(file)
                saveLastUsedDir(file.parent)
            }
        } else {
            enableAnalyze(false)
        }
    }


    /**
     * Gets info about file (its last modification time) and displays it.
     * @param file
     */
    private fun showFileInfo(file: File) {
        val time = Date(file.lastModified())
        val formatter = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale("Italy"))
        val data = formatter.format(time)
        fileInfoText!!.text = if (!data.isBlank()) "Last modified: $data" else null
    }

    /**
     * A folder from which a file that has been chosen last time.
     * @return name of a folder or null.
     */
    private fun readLastUsedFileName(): String? {
        val prefFile = File(pref)
        if (prefFile.exists()) {
            return prefFile.readText()
        }
        return null
    }

    /**
     * Save folder name where the file is located.
     * @param dirName folder name
     */
    private fun saveLastUsedDir(dirName: String) {
        val file = File(pref)
        file.writeText(dirName)
    }

    /**
     * Inform the appropriate controller that the user has selected a file.
     * @param file
     */
    fun onFileSelected(file: File) {
        selectedFile = file
    }

    /**
     * Display the selected file name
     * @param name file name
     */
    fun showFileName(name: String) {
        fileNameText!!.text = name
    }

}

