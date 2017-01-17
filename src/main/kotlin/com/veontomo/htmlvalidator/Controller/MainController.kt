package com.veontomo.htmlvalidator.Controller

import com.veontomo.htmlvalidator.Config
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
import rx.Observable
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import java.io.File
import java.net.URL
import java.sql.Time
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
    @FXML private var menuRecheck: MenuItem? = null
    @FXML private var menuClear: MenuItem? = null
    @FXML private var checkTime: Text? = null


    /**
     * Name of the file that stores the preferences
     */
    private val pref = Config.HISTORY_FILE

    // keyboard shortcut for selecting a file "Ctrl+o"
    private val fileSelectShortcut = KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN)
    // keyboard shortcut for refreshing the results "Ctrl+F5"
    private val refreshShortcut = KeyCodeCombination(KeyCode.F5)
    // keyboard shortcut for clearing the results "Ctrl+C"
    private val clearShortcut = KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN)

    private val fileChooser = FileChooser()
    /**
     * Set of file extensions that are allowed to be displayed in the file chooser dialog.
     */
    private val allowedExtensions = listOf("html", "htm")


    private val model = Model()

    val formatter = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale("Italy"))

    init {
        fileChooser.title = Config.FILE_CHOOSER_DIALOG_TITLE
        model.reports
                .subscribe(
                        { it -> onReportsReceived(it) },
                        { e -> showFileName("error: ${e.message}") })
    }


    override fun initialize(location: URL?, resources: ResourceBundle?) {
        menuSelect!!.accelerator = fileSelectShortcut
        menuRecheck!!.accelerator = refreshShortcut
        menuClear!!.accelerator = clearShortcut
        menuSelect!!.setOnAction { onSelect() }
        menuRecheck!!.setOnAction { onRefresh() }
        menuClear!!.setOnAction { onClear() }
        enableSelect(true)
        enableRefresh(false)
        enableClear(false)
        clearReport()


    }

    /**
     * Perform the checks of the previously selected file.
     */
    private fun onRefresh() {
        model.recheck()
    }

    /**
     * Empty reports from all available checkers.
     * Since the available checkers do not change during the application lifetime, the empty report
     * is calculated once when it is needed.
     */
    private val emptyReport  by lazy {
        model.createEmptyReport()
    }

    /**
     * Clear the view containing the results of checks.
     */
    private fun clearReport() {
        checkersView!!.items = FXCollections.observableArrayList(emptyReport)
    }


    /**
     * Perform check of the selected file
     * @param file a file whose content is to be analyzed
     */
    fun onAnalyze(file: File) {
        enableRefresh(false)
        enableClear(false)
        model.analyze(file)
    }

    /**
     * Clear the file
     */
    fun onClear() {
        enableClear(false)
        enableRefresh(false)
        fileInfoText!!.text = null
        fileNameText!!.text = null
        showFileContent(null)
        clearReport()
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
    fun enableRefresh(isEnabled: Boolean) {
        menuRecheck!!.isDisable = !isEnabled
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
     * Load the reports in corresponding view and enable the menu items.
     * @param items
     */
    fun onReportsReceived(items: List<Report>) {
        checkersView!!.items = FXCollections.observableArrayList(items)
        enableRefresh(true)
        enableClear(true)
        setCheckTime(Date())
    }

    /**
     * Set the check time.
     */
    private fun setCheckTime(date: Date) {
        checkTime!!.text = "${Config.LAST_CHECK} ${formatter.format(date)}"
    }


    fun onSelect() {
        val last = readLastUsedFileName()
        if (last != null) {
            val file = File(last)
            if (file.exists() && file.isDirectory)
                fileChooser.initialDirectory = file
        }
        fileChooser.extensionFilters.addAll(
                FileChooser.ExtensionFilter("html", allowedExtensions.map { "*.$it" })
        )
        val file = fileChooser.showOpenDialog(fileInfoText!!.scene.window)
        //        val file = fileChooser.showOpenDialog(Stage())

        if (file?.exists() ?: false) {
            if (allowedExtensions.contains(file.extension)) {
                showFileName(file!!.path)
                showFileContent(file.toURI().toURL().toExternalForm())
                enableRefresh(true)
                enableClear(true)
                showFileInfo(file)
                saveLastUsedDir(file.parent)
                onAnalyze(file)
            }
        } else {
            enableRefresh(false)
        }
    }


    /**
     * Gets info about file (its last modification time) and displays it.
     * @param file
     */
    private fun showFileInfo(file: File) {
        val time = Date(file.lastModified())
        val data = formatter.format(time)
        fileInfoText!!.text = if (!data.isBlank()) "${Config.LAST_MODIFIED} $data" else null
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
     * Display the selected file name
     * @param name file name
     */
    fun showFileName(name: String) {
        fileNameText!!.text = name
    }

}

