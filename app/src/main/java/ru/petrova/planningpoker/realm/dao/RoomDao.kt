package ru.petrova.planningpoker.realm.dao

import io.realm.Realm
import io.realm.kotlin.where
import ru.petrova.planningpoker.realm.dto.Room

class RoomDao {
    companion object {
        fun isRoomExist(realm: Realm, name: String): Boolean {
            var room = getRoom(realm, name)
            return room != null
        }

        fun saveRoom(realm: Realm, name: String): Room {
            var room = Room(name)
            realm.executeTransactionAsync { realm ->
                realm.insert(room)
            }
            return room
        }

        fun getRoom(realm: Realm, name: String): Room? {
            return realm.where<Room>().equalTo("name", name).findFirst()
        }
    }
}