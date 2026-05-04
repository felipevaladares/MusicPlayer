package com.felpster.musicplayer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.felpster.musicplayer.domain.model.Song

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val artist: String,
    val albumId: Long,
    val albumArtUrl: String,
    val durationMillis: Long,
    val lastPlayedAt: Long? = null
)

fun SongEntity.toDomain() = Song(
    id = id,
    title = title,
    artist = artist,
    albumId = albumId,
    albumArtUrl = albumArtUrl,
    durationMillis = durationMillis
)

fun Song.toEntity(lastPlayedAt: Long? = null) = SongEntity(
    id = id,
    title = title,
    artist = artist,
    albumId = albumId,
    albumArtUrl = albumArtUrl,
    durationMillis = durationMillis,
    lastPlayedAt = lastPlayedAt
)
