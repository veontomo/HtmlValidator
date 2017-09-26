package com.veontomo.htmlvalidator.Controller

import com.veontomo.htmlvalidator.Calculator
import com.veontomo.htmlvalidator.Config
import com.veontomo.htmlvalidator.Models.*
import com.veontomo.htmlvalidator.Models.Checkers.HTML
import com.veontomo.htmlvalidator.Models.Checkers.HTML2
import com.veontomo.htmlvalidator.html.CalculatorLexer
import com.veontomo.htmlvalidator.html.CalculatorParser
import com.veontomo.htmlvalidator.html.HTMLLexer
import com.veontomo.htmlvalidator.html.HTMLParser
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
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
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
    val items = mutableListOf<Report>()


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
        model.analyzer.forEach { it.observeOn(Schedulers.newThread()).subscribe({ report -> onReportReceived(report) }) }
//        val expression = "1+2 * (2+1)/2"
//        val stream = CharStreams.fromString(expression)
//        val lexer = CalculatorLexer(stream)
//        val tokenStream = CommonTokenStream(lexer)
//        val parser = CalculatorParser(tokenStream)
//        val tree = parser.expression()
//        val result = Calculator().visit(tree)
//        println(result)
        val input = "<!DOCTYPE html><html><body class=\"first\">hi!</body></html>"
        val stream = CharStreams.fromString(input)
        val lexer = HTMLLexer(stream)
        val tokenStream = CommonTokenStream(lexer)
        val parser = HTMLParser(tokenStream)
        val tree = parser.htmlDocument()
        val result1 = HTML().visit(tree)
        print("dtd=${result1.dtd}, xml = ${result1.xml}, scripts = ${result1.scripts.joinToString { it }}, nodes = ${result1.nodes.joinToString { it.name }}")
//        val result2 = HTML2().visit(tree)
//
//        lexer.ruleNames.forEach { it -> println(it) }
//        println(result1)
//        val s = tree.childCount
//        (0 until s).forEach { i ->
//            val ch = tree.children[i]
//            println("$i: ${ch?.text}")
//        }
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
        items.clear()
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


    fun onReportReceived(item: Report) {
        items.add(item)
        onReportsReceived(items)
    }


    /**
     * Set the check time.
     */
    private fun setCheckTime(date: Date) {
        checkTime!!.text = "${Config.LAST_CHECK} ${formatter.format(date)}"
    }


    fun onSelect() {
        val last = readLastUsedFileName()
        val lastUsedFile = File(last)
        if (lastUsedFile.exists() && lastUsedFile.isDirectory)
            fileChooser.initialDirectory = lastUsedFile
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
    fun showFileName(name: String) {
        fileNameText!!.text = name
    }

}

