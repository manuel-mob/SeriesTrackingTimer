package cl.mmoscoso.seriestrackingtimer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import cl.mmoscoso.seriestrackingtimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val pomodoroTime: Long = 25 * 60 * 1000 // 25 minutes in milliseconds
    val countdownInterval: Long = 1000 // 1 second interval

    private lateinit var binding: ActivityMainBinding
    private lateinit var timer: CountDownTimer
    private lateinit var myTimer: TextView
    private lateinit var myButton: Button
    private var isTimerRunning = false
    private var remainingTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myTimer = binding.timer
        myButton = binding.startButton

        myButton.setOnClickListener {
            //startTimer(pomodoroTime)
            //timer.start()
            startSettings()
        }

        if (savedInstanceState == null) {
            startTimer(pomodoroTime) // You can define pomodoroTime as per your needs
        }
        else {
            // Restore timer state if savedInstanceState is not null
            isTimerRunning = savedInstanceState.getBoolean("isTimerRunning", false)
            remainingTime = savedInstanceState.getLong("remainingTime", 0)
            if (isTimerRunning) {
                startTimer(remainingTime)
            }
        }


        savedInstanceState?.let {
            isTimerRunning = it.getBoolean("isTimerRunning", false)
            remainingTime = it.getLong("remainingTime", 0)
            if (isTimerRunning) {
                startTimer(remainingTime)
            }
        }




//        val timer = object : CountDownTimer(pomodoroTime, countdownInterval) {
//            override fun onTick(millisUntilFinished: Long) {
//                // Update UI with the remaining time in millisUntilFinished
//                val minutes = millisUntilFinished / (60 * 1000)
//                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
//                val timeText = String.format("%02d:%02d", minutes, seconds)
//                // Update TextView with timeText
//                myTimer.setText(timeText)
//            }
//
//            override fun onFinish() {
//                // Timer completed, handle actions (e.g., play a sound, show notification)
//            }
//        }
//        timer.start()


    }
    override fun onResume() {
        super.onResume()

        // Start or resume the timer based on the saved state
        if (isTimerRunning) {
            startTimer(remainingTime)
        }
    }

    fun startSettings () {
        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
        intent.putExtra("key", "Hello from PomodoroActivity!")
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (isTimerRunning) {
            // Move the app to the background instead of closing the activity
            moveTaskToBack(true)
        } else {
            super.onBackPressed() // Close the activity if timer is not running
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isTimerRunning", isTimerRunning)
        outState.putLong("remainingTime", remainingTime)
    }
    private fun startTimer(timeInMillis: Long) {
        timer = object : CountDownTimer(timeInMillis, countdownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                // Update UI with remaining time
                remainingTime = millisUntilFinished
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                val timeText = String.format("%02d:%02d", minutes, seconds)
                // Update TextView with timeText
                myTimer.setText(timeText)
            }

            override fun onFinish() {
                // Handle timer completion
            }
        }
        timer.start()
        isTimerRunning = true
    }

    private fun stopTimer() {
        timer.cancel()
        isTimerRunning = false
    }
}