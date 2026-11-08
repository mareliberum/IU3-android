package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ScholarProfileKind(val code: String) {
    override fun toString(): String = code

    companion object {
        val ResearchGate = ScholarProfileKind(code = "researchgate")
        val Orcid = ScholarProfileKind(code = "orcid")
        val Dblp = ScholarProfileKind(code = "dblp")
        val GoogleScholar = ScholarProfileKind(code = "scholar")
        val Elibrary = ScholarProfileKind(code = "elibrary")
    }
}
