package com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.Views.MainView
import javafx.application.Application
import javafx.scene.image.Image
import javafx.stage.Stage

/**
 * Entry point
 */
fun main(args: Array<String>) {
    GUI.main(args)
}

/**
 * A GUI application that performs various checks of selected html file.
 */
class GUI : Application() {

    override fun start(primaryStage: Stage) {
        primaryStage.title = Config.APP_NAME
        primaryStage.icons.add(Image(Config.ICON_PATH))
        primaryStage.scene = MainView(primaryStage).getScene()
        primaryStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(GUI::class.java)
        }
    }



}

