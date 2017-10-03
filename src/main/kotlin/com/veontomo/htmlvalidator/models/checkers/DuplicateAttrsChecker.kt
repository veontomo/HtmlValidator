package com.veontomo.htmlvalidator.models.checkers

import com.veontomo.htmlvalidator.parser.DOM
import com.veontomo.htmlvalidator.parser.HtmlNode

/**
 * Control that every tag has no duplicate attributes.
 *
 */
class DuplicateAttrsChecker : Checker() {
    override fun check(html: String): List<CheckMessage> {
        val dom = try {
            DOM(html)
        } catch (e: IllegalStateException) {
            return listOf(CheckMessage("malformed document: ${e.message}", false))
        }

        val duplicates = findNodeDeepDuplicateAttributes(dom.nodes, mutableListOf())

        return duplicates.map { it -> CheckMessage("node ${it.first.name} has duplicate attributes ${it.second.toList().joinToString { "${it.first}: ${it.second.joinToString { it }}" }}", true) }
    }

    /**
     * Return a list of duplicates found in given nodes and its descendants.
     * This is a recursive function, it uses an accumulator for storing previously found duplicate attributes.
     * @param nodes
     * @param accumulator
     * @return a list composed of pairs whose keys are html nodes containing the duplicate attributes and values are maps from duplicate attribute names to their values.
     */
    private fun findNodeDeepDuplicateAttributes(nodes: List<HtmlNode>, accumulator: List<Pair<HtmlNode, Map<String, List<String>>>>): List<Pair<HtmlNode, Map<String, List<String>>>> {
        val result = mutableListOf<Pair<HtmlNode, Map<String, List<String>>>>()
        accumulator.forEach { it -> result.add(it) }
        nodes.forEach { node ->
            run {
                val dupl = findNodeShallowDuplicates(node)
                if (dupl.isNotEmpty()) {
                    result += Pair(node, dupl)
                }
                val children = node.nodes
                val duplChildren = findNodeDeepDuplicateAttributes(children, accumulator)
                result.addAll(duplChildren)
            }
        }
        return result
    }

    /**
     * Return a map of duplicate attributes pertinent to the node, not to its descendants.
     * @param node
     * @return a map whose keys are attribute namess, values - attribute values
     */
    private fun findNodeShallowDuplicates(node: HtmlNode): Map<String, List<String>> {
        return node.attributes.groupBy { it.first }
                .filter { it.value.size > 1 }
                .mapValues { it.value.map { it.second } }
    }


    override val descriptor: String
        get() = "Duplicate attributes"
}