package com.codezone.musicplayersimple

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.codezone.musicplayersimple.databinding.ActivityMainBinding
import com.example.musicplayer.MusicAdapter
import java.io.File

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var songList: ArrayList<Song>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        MyMediaPlayer.instance?.stop()
        MyMediaPlayer.instance?.release()
        MyMediaPlayer.instance = null

        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.READ_MEDIA_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO), 1
            )
        } else {
            loadSongs()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadSongs()
        } else {
            Toast.makeText(this, "Ruxsat berilmagan!", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("Range")
    private fun loadSongs() {
        songList = ArrayList()

        val contentResolver: ContentResolver = contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA
        )


        val selection = ("${MediaStore.Audio.Media.DATA} LIKE ? OR " +
                "${MediaStore.Audio.Media.DATA} LIKE ? OR " +
                "${MediaStore.Audio.Media.DATA} LIKE ?")
        val selectionArgs = arrayOf("%.mp3", "%.wav", "%.aac")

        val cursor: Cursor? = contentResolver.query(
            uri, projection, selection, selectionArgs, null
        )

        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val title = it.getString(it.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val artist = it.getString(it.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val data = it.getString(it.getColumnIndex(MediaStore.Audio.Media.DATA))

                    val song = Song(title, artist, File(data))
                    songList.add(song)
                } while (it.moveToNext())
            }
            it.close()
        }

        if (songList.isEmpty()) {
            Toast.makeText(this, "Musiqalar topilmadi oops!!", Toast.LENGTH_SHORT).show()
        } else {
            binding.recy.adapter = MusicAdapter(songList) { position ->
                val intent = Intent(this, PlayActivity::class.java)
                intent.putExtra("songs", ArrayList(songList))
                intent.putExtra("position", position)
                startActivity(intent)
            }
        }
    }

}
