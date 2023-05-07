package lincyu.musicplayeractivity

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    private lateinit  var player: MediaPlayer
    private lateinit var btnPlayAndPause : Button
    private lateinit var btnStop: Button
    private val mapOfSongs = mapOf(R.raw.ambient_classical_guitar to "ambient classical guitar",
        R.raw.eco_technology to "eco technology",
        R.raw.futuristic_beat to "futuristic beat",
        R.raw.modern_vlog to "modern vlog",
        R.raw.reflected_light to "reflected light")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPlayAndPause = findViewById(R.id.play_pause)
        btnPlayAndPause.setOnClickListener {  playAndPause()}

        btnStop = findViewById(R.id.btn_stop)
        btnStop.setOnClickListener { stop() }

        val songList  = mutableListOf<String>()
        for(song in mapOfSongs){
            songList.add(song.value)
        }
        val songs = songList.toTypedArray()
        val songListView = findViewById<ListView>(R.id.songList)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, songs)
        songListView.adapter = arrayAdapter
        songListView.setOnItemClickListener { _, _, i, _ ->
            stop()
            val songFileID = getSongFileId(songs[i])
            setPlayerToSong(songFileID)
            start()
        }
        init()
    }

    private fun getSongFileId(songName:String):Int{
        for(song in mapOfSongs){
            if (song.value == songName){
                return song.key
            }
        }
        return R.string.defaultSong
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    private fun init() {
        setPlayerToSong(R.raw.dontdownloadthissong)
    }

    private fun setPlayerToSong(songId:Int){
        try {
            player = MediaPlayer.create(this,
                songId)

            player.setOnCompletionListener {
                try {
                    player.stop()
                    player.prepare()
                } catch (e: Exception) {
                    Log.e("player error", e.toString())
                }
            }
        } catch (e: Exception) {
            Log.e("player error", e.toString())
        }
    }

    private fun playAndPause(){
        if(player.isPlaying){
            player.pause()
        }
        else{
            start()
        }
    }

    private fun start() {
        player.start()
    }

    private fun stop() {
        try {
            player.stop()
            player.prepare()
        } catch (e: Exception) {
            Log.e("player error", e.toString())
        }
    }

}