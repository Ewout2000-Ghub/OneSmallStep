package com.example.onesmallstep

import android.app.Application
import com.example.onesmallstep.data.database.PhobiaDatabase
import com.example.onesmallstep.data.repository.PhobiaRepository

class OneSmallStepApplication : Application() {

    val database by lazy { PhobiaDatabase.getDatabase(this) }
    val repository by lazy {
        PhobiaRepository(database.phobiaDao(), database.progressDao())
    }
}