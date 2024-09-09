package artwork.java;

import artwork.java.R.drawable;

public  class TaskManager {
    int[] taskPics = {drawable.image31, drawable.image4, drawable.romb, drawable.image5, drawable.image6};
    String[] taskData = {"image31.svg", "image4.svg", "romb.svg", "image5.svg", "image6.svg"};
    int currTask = 0;

    public void nextTask(int dtask, MainActivity activity) {
        int taskId = this.currTask + dtask;
        if (taskId != -1 && taskId < this.taskData.length) {
            this.currTask = taskId;
            activity.updateTask(new Task(this.taskData[this.currTask], this.taskPics[this.currTask]),
                    taskData.length, currTask);
        }
    }
}
