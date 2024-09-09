package artwork.kotlin

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import artwork.java.R
import artwork.java.App
import artwork.java.c

class MainActivity : AppCompatActivity(), View.OnClickListener, ProgressListener,
    CompoundButton.OnCheckedChangeListener,
    OnSeekBarChangeListener, UpdatePathsListener {
    lateinit var undo: ImageView
    lateinit var redo: ImageView
    lateinit var status: ImageView
    lateinit var back: ImageView
    lateinit var forward: ImageView
    lateinit var text: TextView
    lateinit var switch1: Switch
    lateinit var switch2: Switch
    lateinit var switch3: Switch
    lateinit var seekBar1: SeekBar
    lateinit var progressBar: ProgressBar
    lateinit var preview: Preview
    lateinit var gameView: GameView
    var taskManager: TaskManager = TaskManager()
    override fun onStart() {
        super.onStart()
        taskManager.nextTask(0, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        progressBar = findViewById(R.id.progressbar)
        preview = findViewById(R.id.preview)
        redo = findViewById(R.id.redo)
        redo.setOnClickListener(this)
        undo = findViewById(R.id.undo)
        undo.setOnClickListener(this)
        back = findViewById(R.id.back)
        back.setOnClickListener(this)
        forward = findViewById(R.id.forward)
        forward.setOnClickListener(this)
        status = findViewById(R.id.status)
        status.setOnClickListener(this)
        gameView = findViewById(R.id.view2)
        text = findViewById(R.id.text)
        switch1 = findViewById(R.id.switch1)
        switch2 = findViewById(R.id.switch2)
        switch3 = findViewById(R.id.switch3)
        seekBar1 = findViewById(R.id.seekBar1)
        switch1.setChecked(1 == App.getInstance().getInt(c.option1, 1))
        switch2.setChecked(1 == App.getInstance().getInt(c.option2, 0))
        val showtip = 1 == App.getInstance().getInt(c.option3, 0)
        switch3.setChecked(showtip)
        switch1.setOnCheckedChangeListener(this)
        switch2.setOnCheckedChangeListener(this)
        switch3.setOnCheckedChangeListener(this)
        seekBar1.setMax(100)
        seekBar1.setOnSeekBarChangeListener(this)
        seekBar1.setEnabled(showtip)
        tipSize = App.getInstance().getInt(c.seek1, c.seek1def)
        seekBar1.setProgress(tipSize)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
    override fun onClick(view: View) {
        if (view == undo) {
            gameView.undo()
        } else if (view == redo) {
            gameView.redo()
        } else if (view == back) {
            taskManager.nextTask(-1, this)
        } else if (view == forward) {
            taskManager.nextTask(1, this)
        } else if (view == status) {
            //gameField.update();
        }
    }
    fun updateTask(task: Task, taskCnt: Int, currTask: Int) {
        preview.setImageResource(task.taskResId)
        preview.invalidate()
        back.setImageResource(if (currTask == 0) R.drawable.ic_arrow_back_grey600_36dp else R.drawable.ic_arrow_back_black_36dp)
        forward.setImageResource(if (currTask == taskCnt - 1) R.drawable.ic_arrow_forward_grey600_36dp else R.drawable.ic_arrow_forward_black_36dp)
        progressBar.setProgress(0)
        gameView.changeTask(task)
    }

    override fun updatePaths(pathsSize: Int, currentPathId: Int) {
        undo.setImageResource(if (0 == currentPathId) R.drawable.ic_undo_grey600_36dp else R.drawable.ic_undo_black_36dp)
        redo.setImageResource(if (currentPathId == pathsSize) R.drawable.ic_redo_grey600_36dp else R.drawable.ic_redo_black_36dp)
    }

    override fun updateProgress(i: Int) {
        progressBar.setProgress(i)
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        val id = buttonView.id
        val v = if (isChecked) 1 else 0
        if (R.id.switch1 == id) {
            App.getInstance().putInt(c.option1, v)
            gameView.updateBrush()
        } else if (R.id.switch2 == id) {
            App.getInstance().putInt(c.option2, v)
            gameView.updateBrush()
        } else if (R.id.switch3 == id) {
            App.getInstance().putInt(c.option3, v)
            gameView.updateTips()
            seekBar1.isEnabled = 1 == v
        }
    }

    var tipSize: Int = 0
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        this.tipSize = progress
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        App.getInstance().putInt(c.seek1, tipSize)
        gameView.updateTips()
        progressBar.setProgress(0)
    }
}
