package br.com.transferr.activities

import android.os.Bundle
import br.com.transferr.R
import br.com.transferr.extensions.setupToolbar

class PlainTourActivity : SuperClassActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plain_tour)
        setupToolbar(R.id.toolbar,"Passeios",true)
    }
}
