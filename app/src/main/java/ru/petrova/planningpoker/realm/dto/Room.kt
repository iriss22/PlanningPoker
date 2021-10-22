package ru.petrova.planningpoker.realm.dto;

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId

open class Room(_name: String = "Room") : RealmObject() {
        @PrimaryKey var _id: ObjectId = ObjectId()
        @Required
        var name: String = _name
}
