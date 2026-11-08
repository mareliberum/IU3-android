package com.bmstu.iu3.core.data.source

import android.content.Context
import com.bmstu.iu3.core.common.di.DefaultDispatcher
import com.bmstu.iu3.core.common.di.IoDispatcher
import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.FacultyEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

internal class StubCatalogDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : CatalogDataSource {
    private suspend fun readPayload(payload: CatalogPayload): JsonElement {
        val text = withContext(ioDispatcher) {
            context.assets.open(payload.assetPath).bufferedReader().use { it.readText() }
        }
        return json.parseToJsonElement(text)
    }

    private suspend inline fun <reified T> decode(payload: CatalogPayload): T {
        val root = readPayload(payload)
        val field = payload.field
        return json.decodeFromJsonElement(
            if (field == null) root else root.jsonObject.getValue(
                field
            )
        )
    }

    override suspend fun load(): Catalog = withContext(defaultDispatcher) {
        Catalog(
            contacts = decode(CatalogPayload.Contacts),
            staff = decode(CatalogPayload.Staff),
            courses = decode(CatalogPayload.Courses),
            timetable = decode(CatalogPayload.Schedule),
            labs = decode(CatalogPayload.Laboratories),
            plans = decode(CatalogPayload.StudyPlans),
            events = (decode<List<FacultyEvent>>(CatalogPayload.Events) + decode<List<FacultyEvent>>(
                CatalogPayload.FeaturedEvents
            )).distinctBy { it.id },
            news = decode(CatalogPayload.News),
            honor = decode(CatalogPayload.Honor),
            projects = decode(CatalogPayload.Projects),
            programs = decode(CatalogPayload.Programs),
            achievements = decode(CatalogPayload.Achievements),
            technologies = decode(CatalogPayload.Technologies),
            interests = decode(CatalogPayload.Interests),
            webFaculty = decode(CatalogPayload.Faculty),
            faces = decode(CatalogPayload.Faces),
            scholars = decode(CatalogPayload.Scholars),
            legendWorks = decode(CatalogPayload.LegendWorks),
            images = decode(CatalogPayload.Images),
        )
    }
}
