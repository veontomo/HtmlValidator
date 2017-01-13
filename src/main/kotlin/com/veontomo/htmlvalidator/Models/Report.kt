package com.veontomo.htmlvalidator.Models

import javafx.beans.property.SimpleStringProperty

/**
 * Report
 */
class Report() {

    private val nameProp = SimpleStringProperty(null)
    private val statusProp = SimpleStringProperty(null)
    private val commentProp = SimpleStringProperty(null)


    constructor(name: String, status: String?, comment: String?) : this() {
        nameProp.set(name)
        statusProp.set(status)
        commentProp.set(comment)
    }

    var name: String
        set(value) {
            nameProp.set(value)
        }
        get() = nameProp.get()
    var status: String?
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