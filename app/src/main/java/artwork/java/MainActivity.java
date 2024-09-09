package artwork.java;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import artwork.kotlin.ProgressListener;
import artwork.kotlin.UpdatePathsListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, UpdatePathsListener,
        CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener, ProgressListener {

    boolean firstStage = false, lastStage = false;
    ImageView undo, redo, status, back, forward;
    TextView text;
    Switch switch1, switch2, switch3;
    SeekBar seekBar1;
    ProgressBar progressBar;
    Preview preview;
    GameView gameField;
    TaskManager taskManager = new TaskManager();

    @Override
    protected void onStart() {
        super.onStart();
        taskManager.nextTask(0, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);
        progressBar = findViewById(R.id.progressbar);
        preview = findViewById(R.id.preview);
        redo = findViewById(R.id.redo); redo.setOnClickListener(this);
        undo = findViewById(R.id.undo); undo.setOnClickListener(this);
        back = findViewById(R.id.back); back.setOnClickListener(this);
        forward = findViewById(R.id.forward); forward.setOnClickListener(this);
        status = findViewById(R.id.status); status.setOnClickListener(this);
        gameField = findViewById(R.id.view2);
        text = findViewById(R.id.text);
        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);
        switch3 = findViewById(R.id.switch3);
        seekBar1 = findViewById(R.id.seekBar1);
        switch1.setChecked(1==App.getInstance().getInt(c.option1, 1));
        switch2.setChecked(1==App.getInstance().getInt(c.option2, 0));
        boolean showtip = 1==App.getInstance().getInt(c.option3, 0);
        switch3.setChecked(showtip);
        switch1.setOnCheckedChangeListener(this);
        switch2.setOnCheckedChangeListener(this);
        switch3.setOnCheckedChangeListener(this);
        seekBar1.setMax(100);
        seekBar1.setOnSeekBarChangeListener(this);
        seekBar1.setEnabled(showtip);
        tipSize = App.getInstance().getInt(c.seek1, c.seek1def);
        seekBar1.setProgress(tipSize);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void onClick(View view) {
        if (view.equals(undo)&&!firstStage){
            gameField.undo();
        }else if (view.equals(redo)&&!lastStage){
            gameField.redo();
        }else if (view.equals(back)){
            taskManager.nextTask(-1, this);
        }else if (view.equals(forward)){
            taskManager.nextTask(1, this);
        }else if (view.equals(status)){
            //gameField.update();
        }
    }
    public void updateTask(Task task, int taskCnt, int currTask) {
        preview.setImageResource(task.taskResId);
        preview.invalidate();
        back.setImageResource(currTask==0? R.drawable.ic_arrow_back_grey600_36dp:R.drawable.ic_arrow_back_black_36dp);
        forward.setImageResource(currTask==taskCnt-1? R.drawable.ic_arrow_forward_grey600_36dp: R.drawable.ic_arrow_forward_black_36dp);
        progressBar.setProgress(0);
        gameField.changeTask(task);
    }

    public void updatePaths(int pathsSize, int currentPathId) {
        firstStage = 0==currentPathId;
        undo.setImageResource(firstStage ? R.drawable.ic_undo_grey600_36dp : R.drawable.ic_undo_black_36dp);
        lastStage = currentPathId==pathsSize;
        redo.setImageResource(lastStage ? R.drawable.ic_redo_grey600_36dp : R.drawable.ic_redo_black_36dp);
    }
    public void updateProgress(int i){
        progressBar.setProgress(i);
    }
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        int val = isChecked ? 1 : 0;
        if (R.id.switch1==id) {
            App.getInstance().putInt(c.option1, val);
            gameField.updateBrush();
        }else if (R.id.switch2==id){
            App.getInstance().putInt(c.option2, val);
            gameField.updateBrush();
        }else if (R.id.switch3==id){
            App.getInstance().putInt(c.option3, val);
            gameField.updateTips();
            seekBar1.setEnabled(1==val);
        }
    }
    int tipSize = 0;
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.tipSize = progress;
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        App.getInstance().putInt(c.seek1, tipSize);
        System.out.println("progress="+ tipSize);
        gameField.updateTips();
        progressBar.setProgress(0);
    }
}
