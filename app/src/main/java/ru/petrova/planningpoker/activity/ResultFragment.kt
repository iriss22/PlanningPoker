package ru.petrova.planningpoker.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.activity_estimate.*
import kotlinx.android.synthetic.main.activity_result.*
import ru.petrova.planningpoker.R
import ru.petrova.planningpoker.pokerApp
import ru.petrova.planningpoker.realm.ResultAdapter
import ru.petrova.planningpoker.realm.dao.EstimateDao


class ResultFragment: Fragment() {
    private val viewModel: EstimateViewModel by activityViewModels()

    private lateinit var projectRealm: Realm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val partition = arguments?.getString("partition")

        var user: User? = pokerApp.currentUser()
        val config = SyncConfiguration.Builder(user!!, partition).build()

        Realm.getInstanceAsync(config, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                this@ResultFragment.projectRealm = realm
                setUpRecyclerView(realm)
            }
        })

        btnClearEstimate.setOnClickListener {
            EstimateDao.deleteAll(projectRealm)
            viewModel.estimate(false)
        }

        viewModel.estimated.observe(viewLifecycleOwner, Observer { list ->
            if (viewModel.isEstimated()) {
                view?.visibility = VISIBLE
            } else {
                view?.visibility = GONE
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (resultList != null) {
            resultList.adapter = null
        }
        projectRealm.close()

    }

    private fun setUpRecyclerView(realm: Realm) {
        val estimates = EstimateDao.findAll(realm)
        estimates.addChangeListener { collection, _ ->
            if (collection.isEmpty()) {
                viewModel.estimate(false)
            }
        }

        var adapter = ResultAdapter(estimates)

        resultList.layoutManager = LinearLayoutManager(activity)
        resultList.adapter = adapter
        resultList.setHasFixedSize(true)
        resultList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    }
}