package ru.petrova.planningpoker.realm.dto

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Estimate(_userName: String = "", _estimate: String = ""): RealmObject(){
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var userName: String = _userName
    var estimate: String = _estimate
}