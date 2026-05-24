package com.backend.data.mapper

import com.backend.domain.model.Chapter
import com.backend.presentation.request.CreateChapterRequest

object ChaptersMapper {
    fun CreateChapterRequest.toDomain(): Chapter {
        return Chapter(
            title = this.title,
            chapterNumber = this.chapterNumber
        )
    }
}