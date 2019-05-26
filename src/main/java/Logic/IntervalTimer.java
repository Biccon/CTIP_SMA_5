package Logic;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalDate;
import java.time.LocalTime;

import GUI.DigitalWatch;
import sun.audio.*;    //import the sun.audio package

import java.io.*;


public class IntervalTimer extends TimerTask {
    private int iteration;
    private LocalDateTime savedIntervalTimer;
    private Boolean isEnabled;
    private LocalDateTime remainedIntervalTimer;
    private Timer m_timer;

    public LocalDateTime getSavedIntervalTimer() {
        return savedIntervalTimer;
    }

    public void setSavedIntervalTimer(LocalDateTime savedIntervalTimer) {
        this.savedIntervalTimer = savedIntervalTimer;
    }

    public LocalDateTime getRemainedIntervalTimer() {
        return remainedIntervalTimer;
    }

    public void setRemainedIntervalTimer(LocalDateTime remainedIntervalTimer) {
        this.remainedIntervalTimer = remainedIntervalTimer;
    }

    public IntervalTimer(Timer m_timer) {
        this.m_timer = m_timer;
        this.iteration = 0;
        this.isEnabled = false;
        LocalDateTime initDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        this.savedIntervalTimer = initDateTime;
        this.remainedIntervalTimer = savedIntervalTimer;
        m_timer.schedule(this, 0, 1000);
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public void enable() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");

        if (!sdf.format(Date.from(this.savedIntervalTimer.atZone(ZoneId.systemDefault()).toInstant())).equals("000000")) { // 저장된 intervaltimer이 000000이면 동작하지않음 말도안돼 십라
            this.isEnabled = true;
        }
        if (sdf.format(Date.from(this.remainedIntervalTimer.atZone(ZoneId.systemDefault()).toInstant())).equals("000000"))
            this.remainedIntervalTimer = LocalDateTime.of(this.savedIntervalTimer.toLocalDate(), this.savedIntervalTimer.toLocalTime());
    }

    public void disable() {
        this.isEnabled = false;
    }

    public LocalDateTime loadIntervalTimer() {
        return remainedIntervalTimer;
    }

    public void saveIntervalTimer(LocalDateTime data) {
        if (!isEnabled) {
            this.savedIntervalTimer = data;
            this.remainedIntervalTimer = data;
            this.iteration = 0;
        }
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public void ring() {
        System.out.println("Ring in interval timer");
        DigitalWatch.getInstance().beep();
    }

    @Override
    public void run() {
        SimpleDateFormat formatTime = new SimpleDateFormat("HHmmss");
        if (this.isEnabled) {
            if (formatTime.format(Date.from(this.remainedIntervalTimer.atZone(ZoneId.systemDefault()).toInstant())).equals("000000")) {
                this.remainedIntervalTimer = savedIntervalTimer.minusSeconds(1);
                iteration++;
            } else {
                this.remainedIntervalTimer = this.remainedIntervalTimer.minusSeconds(1);
                if (formatTime.format(Date.from(this.remainedIntervalTimer.atZone(ZoneId.systemDefault()).toInstant())).equals("000000")) { // 깎인 것이 0이면
                    ring();
                }
            }
        }
    }

    public int getIteration() {
        return iteration;
    }

}