package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ProgramSectionType(val code: String) {
    override fun toString(): String = code

    companion object {
        val Journey = ProgramSectionType(code = "journey")
        val Cards = ProgramSectionType(code = "cards")
        val Chips = ProgramSectionType(code = "chips")
        val Disciplines = ProgramSectionType(code = "disciplines")
        val Companies = ProgramSectionType(code = "companies")
        val Scores = ProgramSectionType(code = "scores")
        val ScoreHistory = ProgramSectionType(code = "scoreHistory")
    }
}
