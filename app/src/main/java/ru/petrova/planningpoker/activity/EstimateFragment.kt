package ru.petrova.planningpoker.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import io.realm.Realm
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.activity_estimate.*
import ru.petrova.planningpoker.R
import ru.petrova.planningpoker.pokerApp
import ru.petrova.planningpoker.realm.dao.EstimateDao

class EstimateFragment: Fragment() {

    private val viewModel: EstimateViewModel by activityViewModels()

    private val partitionNamePrefix = "room="

    private lateinit var projectRealm: Realm
    private lateinit var userName: String

    private var partition = "default"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_estimate, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val roomId = arguments?.getString("roomId")
        userName = arguments?.getString("userName").orEmpty()

        partition = partitionNamePrefix+roomId
        var user: User? = pokerApp.currentUser()
        val config = SyncConfiguration.Builder(user!!, partition)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()

        Realm.getInstanceAsync(config, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                this@EstimateFragment.projectRealm = realm
                EstimateDao.delete(projectRealm, userName)
            }
        })

        ivxs.setOnClickListener {
            estimateAndShowResult("XS", it)
        }

        ivs.setOnClickListener {
            estimateAndShowResult("S", it)
        }

        ivm.setOnClickListener {
            estimateAndShowResult("M", it)
        }

        ivl.setOnClickListener {
            estimateAndShowResult("L", it)
        }

        viewModel.estimated.observe(viewLifecycleOwner, Observer { set ->
            if (!set) {
                ivxs.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                ivs.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                ivm.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                ivl.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            }
        })
    }

    private fun estimateAndShowResult(estimate: String, button: View) {
        if (viewModel.isEstimated()) {
            Toast.makeText(activity, R.string.has_estimated_already, Toast.LENGTH_SHORT).show()
        } else {
            button.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            EstimateDao.createEstimate(projectRealm, userName, estimate)
            viewModel.estimate(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        projectRealm.close()
    }
}