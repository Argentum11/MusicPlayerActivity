package lincyu.musicplayeractivity

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MainActivity : AppCompatActivity() {

    private lateinit  var player: MediaPlayer
    private lateinit var btnPlayAndPause : Button
    private lateinit var btnStop: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPlayAndPause = findViewById(R.id.play_pause)
        btnPlayAndPause.setOnClickListener {  playAndPause()}

        btnStop = findViewById(R.id.btn_stop)
        btnStop.setOnClickListener { stop() }

        val songListView = findViewById<ListView>(R.id.songList)

        val songs = listOf(
            ListItem(getString(R.string.ambient_classical_guitar), R.drawable.ambient_classical_guitar, R.raw.ambient_classical_guitar),
            ListItem(getString(R.string.eco_technology), R.drawable.eco_technology, R.raw.eco_technology),
            ListItem(getString(R.string.futuristic_beat), R.drawable.futuristic_beat, R.raw.futuristic_beat),
            ListItem(getString(R.string.modern_vlog), R.drawable.modern_vlog, R.raw.modern_vlog),
            ListItem(getString(R.string.reflected_light), R.drawable.reflected_light, R.raw.reflected_light)
        )
        val adapter = ListAdapter(this, songs)
        songListView.adapter = adapter

        songListView.setOnItemClickListener { _, _, i, _ ->
            stop()
            setPlayerToSong(songs[i].mp3ResId)
            start()
        }
        init()
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

    data class ListItem(val songName: String,val imageResId: Int, val mp3ResId:Int)
    class ListAdapter(private val context: Context, private val items: List<ListItem>) :
        ArrayAdapter<ListItem>(context, R.layout.list_item, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            val item = items[position]

            view.findViewById<ImageView>(R.id.image_view).setImageResource(item.imageResId)
            view.findViewById<TextView>(R.id.songName).text = item.songName

            /* get metadata */
            val retriever = MediaMetadataRetriever()
            val resourceId = item.mp3ResId
            val uri = Uri.parse("android.resource://" + context.packageName + "/" + resourceId)
            retriever.setDataSource(context, uri)
            val artist: String? = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            view.findViewById<TextView>(R.id.artist).text = artist

            return view
        }
    }

}