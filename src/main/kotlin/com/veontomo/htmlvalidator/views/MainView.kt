package com.veontomo.htmlvalidator.views

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.stage.Stage

/**
 * Main view
 */
class MainView(val stage: Stage) {

    fun getScene(): Scene {
        val grid = FXMLLoader.load<GridPane>(javaClass.getResource("/MainView.fxml"))
        val scene = Scene(grid, stage.width - grid.padding.left - grid.padding.right, 500.0)
        return scene
    }

}


