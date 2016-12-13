package test.com.veontomo.htmlvalidator

import com.veontomo.htmlvalidator.WidthChecker
import org.junit.After
import org.junit.Test
import org.junit.Before
/**
 * Test suite for WidthChecker.
 */
class WidthCheckerTest {
    private var checker: WidthChecker? = null

    @Before
    fun setUp() {
        checker = WidthChecker()
    }

    @After
    fun tearDown() {
        checker = null
    }

    @Test
    fun check() {
        val mock = mock<WidthChecker>()


        /* Then */
        verify(mock).doSomething(any())
    }

}