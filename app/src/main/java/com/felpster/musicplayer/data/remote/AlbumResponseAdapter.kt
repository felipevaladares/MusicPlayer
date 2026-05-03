package com.felpster.musicplayer.data.remote

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader

class AlbumResponseAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): AlbumResponse {
        var resultCount = 0
        val songs = mutableListOf<RemoteSong>()
        var album: RemoteAlbum? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "resultCount" -> resultCount = reader.nextInt()
                "results" -> {
                    reader.beginArray()
                    var isFirst = true
                    while (reader.hasNext()) {
                        if (isFirst) {
                            // Peek at wrapper type to determine if it's an album or song
                            val peekReader = reader.peekJson()
                            peekReader.beginObject()
                            var wrapperType = ""
                            var collectionType = ""

                            while (peekReader.hasNext()) {
                                val key = peekReader.nextName()
                                when (key) {
                                    "wrapperType" -> wrapperType = peekReader.nextString()
                                    "collectionType" -> collectionType = peekReader.nextString()
                                    else -> peekReader.skipValue()
                                }
                            }

                            if (wrapperType == "collection" || collectionType == "Album") {
                                album = parseAlbumFromJson(reader)
                            } else {
                                songs.add(parseSongFromJson(reader))
                            }
                            isFirst = false
                        } else {
                            // Rest are always songs
                            songs.add(parseSongFromJson(reader))
                        }
                    }
                    reader.endArray()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return AlbumResponse(resultCount, songs, album)
    }


    private fun parseAlbumFromJson(reader: JsonReader): RemoteAlbum {
        var wrapperType = ""
        var collectionType = ""
        var artistId = 0L
        var collectionId = 0L
        var amgArtistId: Long? = null
        var artistName = ""
        var collectionName = ""
        var collectionCensoredName = ""
        var artistViewUrl = ""
        var collectionViewUrl = ""
        var artworkUrl60 = ""
        var artworkUrl100 = ""
        var collectionPrice: Double? = null
        var collectionExplicitness = ""
        var trackCount = 0
        var copyright = ""
        var country = ""
        var currency = ""
        var releaseDate = ""
        var primaryGenreName = ""

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "wrapperType" -> wrapperType = reader.nextString()
                "collectionType" -> collectionType = reader.nextString()
                "artistId" -> artistId = reader.nextLong()
                "collectionId" -> collectionId = reader.nextLong()
                "amgArtistId" -> amgArtistId = reader.nextLong()
                "artistName" -> artistName = reader.nextString()
                "collectionName" -> collectionName = reader.nextString()
                "collectionCensoredName" -> collectionCensoredName = reader.nextString()
                "artistViewUrl" -> artistViewUrl = reader.nextString()
                "collectionViewUrl" -> collectionViewUrl = reader.nextString()
                "artworkUrl60" -> artworkUrl60 = reader.nextString()
                "artworkUrl100" -> artworkUrl100 = reader.nextString()
                "collectionPrice" -> collectionPrice = reader.nextDouble()
                "collectionExplicitness" -> collectionExplicitness = reader.nextString()
                "trackCount" -> trackCount = reader.nextInt()
                "copyright" -> copyright = reader.nextString()
                "country" -> country = reader.nextString()
                "currency" -> currency = reader.nextString()
                "releaseDate" -> releaseDate = reader.nextString()
                "primaryGenreName" -> primaryGenreName = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return RemoteAlbum(
            wrapperType = wrapperType,
            collectionType = collectionType,
            artistId = artistId,
            collectionId = collectionId,
            amgArtistId = amgArtistId,
            artistName = artistName,
            collectionName = collectionName,
            collectionCensoredName = collectionCensoredName,
            artistViewUrl = artistViewUrl,
            collectionViewUrl = collectionViewUrl,
            artworkUrl60 = artworkUrl60,
            artworkUrl100 = artworkUrl100,
            collectionPrice = collectionPrice,
            collectionExplicitness = collectionExplicitness,
            trackCount = trackCount,
            copyright = copyright,
            country = country,
            currency = currency,
            releaseDate = releaseDate,
            primaryGenreName = primaryGenreName
        )
    }

    private fun parseSongFromJson(reader: JsonReader): RemoteSong {
        var wrapperType = ""
        var kind: String? = null
        var artistId = 0L
        var collectionId = 0L
        var trackId = 0L
        var artistName = ""
        var collectionName = ""
        var trackName = ""
        var collectionCensoredName = ""
        var trackCensoredName = ""
        var artistViewUrl = ""
        var collectionViewUrl = ""
        var trackViewUrl = ""
        var previewUrl: String? = null
        var artworkUrl30 = ""
        var artworkUrl60 = ""
        var artworkUrl100 = ""
        var collectionPrice: Double? = null
        var trackPrice: Double? = null
        var releaseDate = ""
        var collectionExplicitness = ""
        var trackExplicitness = ""
        var discCount = 0
        var discNumber = 0
        var trackCount = 0
        var trackNumber = 0
        var trackTimeMillis = 0L
        var country = ""
        var currency = ""
        var primaryGenreName = ""
        var isStreamable: Boolean? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "wrapperType" -> wrapperType = reader.nextString()
                "kind" -> kind = reader.nextString()
                "artistId" -> artistId = reader.nextLong()
                "collectionId" -> collectionId = reader.nextLong()
                "trackId" -> trackId = reader.nextLong()
                "artistName" -> artistName = reader.nextString()
                "collectionName" -> collectionName = reader.nextString()
                "trackName" -> trackName = reader.nextString()
                "collectionCensoredName" -> collectionCensoredName = reader.nextString()
                "trackCensoredName" -> trackCensoredName = reader.nextString()
                "artistViewUrl" -> artistViewUrl = reader.nextString()
                "collectionViewUrl" -> collectionViewUrl = reader.nextString()
                "trackViewUrl" -> trackViewUrl = reader.nextString()
                "previewUrl" -> previewUrl = reader.nextString()
                "artworkUrl30" -> artworkUrl30 = reader.nextString()
                "artworkUrl60" -> artworkUrl60 = reader.nextString()
                "artworkUrl100" -> artworkUrl100 = reader.nextString()
                "collectionPrice" -> collectionPrice = reader.nextDouble()
                "trackPrice" -> trackPrice = reader.nextDouble()
                "releaseDate" -> releaseDate = reader.nextString()
                "collectionExplicitness" -> collectionExplicitness = reader.nextString()
                "trackExplicitness" -> trackExplicitness = reader.nextString()
                "discCount" -> discCount = reader.nextInt()
                "discNumber" -> discNumber = reader.nextInt()
                "trackCount" -> trackCount = reader.nextInt()
                "trackNumber" -> trackNumber = reader.nextInt()
                "trackTimeMillis" -> trackTimeMillis = reader.nextLong()
                "country" -> country = reader.nextString()
                "currency" -> currency = reader.nextString()
                "primaryGenreName" -> primaryGenreName = reader.nextString()
                "isStreamable" -> isStreamable = reader.nextBoolean()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return RemoteSong(
            wrapperType = wrapperType,
            kind = kind,
            artistId = artistId,
            collectionId = collectionId,
            trackId = trackId,
            artistName = artistName,
            collectionName = collectionName,
            trackName = trackName,
            collectionCensoredName = collectionCensoredName,
            trackCensoredName = trackCensoredName,
            artistViewUrl = artistViewUrl,
            collectionViewUrl = collectionViewUrl,
            trackViewUrl = trackViewUrl,
            previewUrl = previewUrl,
            artworkUrl30 = artworkUrl30,
            artworkUrl60 = artworkUrl60,
            artworkUrl100 = artworkUrl100,
            collectionPrice = collectionPrice,
            trackPrice = trackPrice,
            releaseDate = releaseDate,
            collectionExplicitness = collectionExplicitness,
            trackExplicitness = trackExplicitness,
            discCount = discCount,
            discNumber = discNumber,
            trackCount = trackCount,
            trackNumber = trackNumber,
            trackTimeMillis = trackTimeMillis,
            country = country,
            currency = currency,
            primaryGenreName = primaryGenreName,
            isStreamable = isStreamable
        )
    }
}

