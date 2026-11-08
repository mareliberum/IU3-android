package com.bmstu.iu3.core.model

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class AdmissionProgramId(val code: String) {
    override fun toString(): String = code

    companion object {
        val Bachelor = AdmissionProgramId(code = "bachelor")
        val Fintech = AdmissionProgramId(code = "fintech")
        val Master = AdmissionProgramId(code = "master")
        val GameDevelopment = AdmissionProgramId(code = "gamedev")
        val Postgraduate = AdmissionProgramId(code = "postgraduate")
    }
}
