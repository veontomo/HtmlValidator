package com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.Models.Checkers.DuplicateAttrsChecker
import org.junit.Test

import org.junit.Assert.*

class DuplicateAttrsCheckerTest {
    // Test suite for detecting duplicate attributes
    // Partition the input as follows
    // 1. total # attrs: 0, 1, 2, > 2
    // 2. # of different duplicate attrs: 0, 1, > 1
    // 3. values of the duplicates: equal, different, mixed

    // Cover
    // 1. total # attrs: 0
    // 2. # of different duplicate attrs: 0
    @Test
    fun `return empty list of the tag has no attributes`() {
        val checker = DuplicateAttrsChecker()
        val report = checker.check("<span>element</span>")
        assertEquals(0, report.size)
    }

    // Cover
    // 1. total # attrs: 1
    // 2. # of different duplicate attrs: 0
    @Test
    fun `return empty list of the tag has one attribute`() {
        val checker = DuplicateAttrsChecker()
        val report = checker.check("<div id=\"1\"></div>")
        assertEquals(0, report.size)
    }

    // Cover
    // 1. total # attrs: 2
    // 2. # of different duplicate attrs: 1
    // 3. values of the duplicates: different
    @Test
    fun `return one element list of the tag has two attributes and they have equal names with different values`() {
        val checker = DuplicateAttrsChecker()
        val report = checker.check("<span class=\"first\" class=\"second\"></span>")
        assertEquals(1, report.size)
    }

    // Cover
    // 1. total # attrs: 2
    // 2. # of different duplicate attrs: 1
    // 3. values of the duplicates: equal
    @Test
    fun `return one element list of the tag has two attributes and they have equal names with equal values`() {
        val checker = DuplicateAttrsChecker()
        val report = checker.check("<span class=\"first\" class=\"second\"></span>")
        assertEquals(1, report.size)
    }

    // Cover
    // 1. total # attrs: > 2
    // 2. # of different duplicate attrs: > 1
    // 3. values of the duplicates: mixed
    @Test
    fun `return two element list of the tag has six attributes, with two sets of duplicates of sizes 3 and 2`() {
        val checker = DuplicateAttrsChecker()
        val report = checker.check("<p class=\"first\" class=\"second\" width=\"220\" class=\"first\" width=\"240\" style=\"padding:0\">XYZ</span>")
        assertEquals(1, report.size)
    }

}