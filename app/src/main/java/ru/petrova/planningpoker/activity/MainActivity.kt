package ru.petrova.planningpoker.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import io.realm.Realm
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration

import kotlinx.android.synthetic.main.activity_main.*
import ru.petrova.planningpoker.R
import ru.petrova.planningpoker.TAG
import ru.petrova.planningpoker.pokerApp
import ru.petrova.planningpoker.realm.dao.RoomDao
import ru.petrova.planningpoker.realm.dto.Room

class MainActivity : AppCompatActivity() {
    private lateinit var projectRealm: Realm
    private var partition = "space=default"
    private var user: User? = null
    private val roomPartitionNamePrefix = "room="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val anonymousCredentials: Credentials = Credentials.anonymous()
        pokerApp.loginAsync(anonymousCredentials) {
            if (it.isSuccess) {
                Log.v(TAG(), "Successfully authenticated anonymously.")
                init()
            } else {
                Log.e(TAG(), it.error.toString())
                Toast.makeText(applicationContext, R.string.unknown_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun init() {
        user = pokerApp.currentUser()

        val config = SyncConfiguration.Builder(user!!, partition)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()

        Realm.getInstanceAsync(config, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                this@MainActivity.projectRealm = realm
            }
        })

        btnCreateRoom.setOnClickListener {
            val roomName = etRoomName.text.toString()
            var userName = etUserName.text.toString()

            if (isUserAndRoomEntered(userName, roomName)) {
                if (!RoomDao.isRoomExist(projectRealm, roomName)) {
                    var room = RoomDao.saveRoom(projectRealm, roomName)
                    openEstimateView(room, userName)
                } else {
                    Toast.makeText(
                        applicationContext,
                        R.string.room_already_exist,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        btnOpenRoom.setOnClickListener {
            var userName = etUserName.text.toString()
            val roomName = etRoomName.text.toString()

            if (isUserAndRoomEntered(userName, roomName)) {
                val room = RoomDao.getRoom(projectRealm, roomName)
                if (room != null) {
                    openEstimateView(room, userName)
                } else {
                    Toast.makeText(applicationContext, R.string.room_hasnt_exist, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isUserAndRoomEntered(userName: String?, roomName: String?): Boolean {
        if (userName == null || userName.isBlank()) {
            Toast.makeText(applicationContext, R.string.print_user_name, Toast.LENGTH_SHORT).show()
            return false
        } else if (roomName == null || roomName.isBlank()) {
            Toast.makeText(applicationContext, R.string.print_room_name, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun openEstimateView(room: Room, userName: String) {
        val intent = Intent(this, PlanningActivity::class.java).apply {
            putExtra("roomId", room._id.toString())
            putExtra("userName", userName)
            putExtra("partition", roomPartitionNamePrefix+room._id.toString())
            partition = roomPartitionNamePrefix+room._id.toString()
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        projectRealm.close()
    }
}