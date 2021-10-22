package ru.petrova.planningpoker.realm.dao

import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import ru.petrova.planningpoker.realm.dto.Estimate

class EstimateDao {
    companion object {
        fun createEstimate(realm: Realm, userName: String, estimate: String) {
            var estimateRealm = Estimate(userName, estimate)
            realm.executeTransactionAsync { realm ->
                realm.insert(estimateRealm)
            }
        }

        fun findAll(realm: Realm): RealmResults<Estimate> {
            return realm.where<Estimate>().sort("_id").findAll()
        }

        fun deleteAll(realm: Realm) {
            realm.executeTransactionAsync { realm ->
                realm.delete(Estimate::class.java)
            }
        }

        fun delete(realm: Realm, userName: String) {
            realm.executeTransaction { r: Realm ->
                val estimates = r.where(Estimate::class.java)
                    .equalTo("userName", userName)
                    .findAll()
                estimates.deleteAllFromRealm()
            }
        }
    }
}