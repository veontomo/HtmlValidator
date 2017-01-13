package com.veontomo.htmlvalidator.Models

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty

/**
 * Report
 */
class Report() {

    private val nameProp = SimpleStringProperty(null)
    private val statusProp = SimpleObjectProperty<Boolean?>(true)
    private val commentProp = SimpleStringProperty(null)

    constructor(name: String, status: Boolean?, comment: String?) : this() {
        nameProp.set(name)
        if (status != null) statusProp.set(status)
        if (comment != null) commentProp.set(comment)
    }

    var name: String
        set(value) {
            nameProp.set(value)
        }
        get() = nameProp.get()
    var status: Boolean?
        set(value) {
            statusProp.set(value)
        }
        get() = statusProp.get()
    var comment: String?
        set(value) {
            commentProp.set(value)
        }
        get() = commentProp.get()
}