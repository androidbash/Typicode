package com.harsh.typicode.data.remote.task

import java.util.*

/**
 * Created by Harsh Mittal on 2019-06-07.
 */
class AlbumsTaskManager {
    companion object {
        const val SAVE_ALBUMS_TASK = "SAVE_ALBUMS_TASK"
    }

    private val map: MutableMap<String, Boolean> = Collections.synchronizedMap(mutableMapOf())

    fun registerTask(taskTag: String) {
        map[taskTag] = true
    }

    fun unregisterTask(taskTag: String) {
        map[taskTag] = false
    }

    fun isRegistered(taskTag: String): Boolean {
        return map[taskTag] ?: false
    }
}