package artwork.kotlin

import artwork.java.R

class TaskManager {
    val taskPics: IntArray = intArrayOf(R.drawable.image31, R.drawable.image4, R.drawable.romb,
        R.drawable.image5, R.drawable.image6)
    val taskData: Array<String> = arrayOf("image31.svg", "image4.svg", "romb.svg", "image5.svg", "image6.svg")
    var currTask: Int = 0
    fun nextTask(dtask : Int, activity: MainActivity){
        val taskId = currTask + dtask;
        if (taskId!=-1 && taskId<taskData.size){
            currTask = taskId
            activity.updateTask(Task(taskData[currTask], taskPics[currTask]), taskData.size, currTask)
        }
    }
}
