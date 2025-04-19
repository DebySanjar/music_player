package com.codezone.musicplayersimple

import java.io.File
import java.io.Serializable

data class Song(
    val title: String,
    val artist: String,
    val file: File
) : Serializable
