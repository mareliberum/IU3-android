package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Attestation(val code: String) {
    override fun toString(): String = code

    companion object {
        val None = Attestation(code = "")
        val Exam = Attestation(code = "экз")
        val RatingExam = Attestation(code = "р.экз")
        val Pass = Attestation(code = "зач")
        val GradedPass = Attestation(code = "д.зач")
    }
}
