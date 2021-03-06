package com.veontomo.htmlvalidator.controller

import com.veontomo.htmlvalidator.Config
import com.veontomo.htmlvalidator.models.*
import javafx.application.Platform
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
import rx.schedulers.Schedulers
import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.prefs.Preferences

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
     * An accumulator for the reports. Once a checker produces a report, it gets
     * inserted to this list which then is passed to the method that displays the reports.
     * TODO: get rid of this accumulator. Make it work in such a way that a report is sent
     * directly to its row inside the table that shaow the reports.
     */
    private val items = mutableListOf<Report>()


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

    private val formatter = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale("Italy"))

    init {
        fileChooser.title = Config.FILE_CHOOSER_DIALOG_TITLE
        model.analyzer.forEach { it.observeOn(Schedulers.newThread()).subscribe({ report -> onReportReceived(report) }, {e -> println(e.message)}) }

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
        items.clear()
    }

    /**
     * Empty reports from all available checkers.
     * Since the available checkers do not change during the application lifetime, the empty report
     * is calculated once when it is needed.
     */
    private val emptyReport by lazy {
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
    private fun onAnalyze(file: File) {
        enableRefresh(false)
        enableClear(false)
        model.analyze(file)
    }

    /**
     * Clear the file
     */
    private fun onClear() {
        enableClear(false)
        enableRefresh(false)
        fileInfoText!!.text = null
        fileNameText!!.text = null
        showFileContent(null)
        clearReport()
        items.clear()
    }


    /**
     * Display the content of the selected file inside the web viewer.
     * @param url
     */
    private fun showFileContent(url: String?) {
        browser!!.engine.load(url)
    }

    /**
     * Enable/disable a menu item that is used to analyze the file
     * @param isEnabled true to enable, false to disable
     */
    private fun enableRefresh(isEnabled: Boolean) {
        menuRecheck!!.isDisable = !isEnabled
    }

    /**
     * Enable/disable a menu item that is used to select a file
     * @param isEnabled true to enable, false to disable
     */
    private fun enableSelect(isEnabled: Boolean) {
        menuSelect!!.isDisable = !isEnabled
    }

    /**
     * Enable/disable a menu item that is used to clear the result of analysis
     * @param isEnabled true to enable, false to disable
     */
    private fun enableClear(isEnabled: Boolean) {
        menuClear!!.isDisable = !isEnabled
    }

    /**
     * Load the reports in corresponding view and enable the menu items.
     * @param items
     */
    private fun onReportsReceived(items: List<Report>) {
        checkersView!!.items = FXCollections.observableArrayList(items)
        enableRefresh(true)
        enableClear(true)
        setCheckTime(Date())
    }


    private fun onReportReceived(item: Report) {
        items.add(item)
        Platform.runLater {

            onReportsReceived(items)
        }
    }


    /**
     * Set the check time.
     */
    private fun setCheckTime(date: Date) {
        checkTime!!.text = "${Config.LAST_CHECK} ${formatter.format(date)}"
    }


    private fun onSelect() {
        val last = readLastUsedFileName()
        val lastUsedFile = File(last)
        if (lastUsedFile.exists() && lastUsedFile.isDirectory)
            fileChooser.initialDirectory = lastUsedFile
        fileChooser.extensionFilters.addAll(
                FileChooser.ExtensionFilter("html", allowedExtensions.map { "*.$it" })
        )
        val file = fileChooser.showOpenDialog(fileInfoText!!.scene.window)
        //        val file = fileChooser.showOpenDialog(Stage())

        if (file?.exists() == true) {
            if (allowedExtensions.contains(file.extension)) {
                showFileName(file.path)
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

    private val LAST_USED_FILE = "last-used-file"

    /**
     * A folder from which a file that has been chosen last time.
     * @return name of a folder or empty string.
     */
    private fun readLastUsedFileName(): String {
        val prefs = Preferences.userNodeForPackage(this::class.java)
        return prefs.get(LAST_USED_FILE, "")
    }

    /**
     * Save folder name where the file is located.
     * @param dirName folder name
     */
    private fun saveLastUsedDir(dirName: String) {
        val prefs = Preferences.userNodeForPackage(this::class.java)
        prefs.put(LAST_USED_FILE, dirName)
        prefs.flush()
    }

    /**
     * Display the selected file name
     * @param name file name
     */
    private fun showFileName(name: String) {
        fileNameText!!.text = name
    }

}

