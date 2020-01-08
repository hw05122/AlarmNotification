package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class btnClickListener implements View.OnClickListener {
    private Context context;
    private TimePicker timePicker;

    public btnClickListener(Context context, TimePicker timePicker) {
        this.context = context;
        this.timePicker = timePicker;
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = setTime();

        Intent alarmIntent = new Intent(context, AlarmNotification.class);
        // 특정 시점에 이 Intent 실행
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

    public Calendar setTime() {
        int hour_24, minute;

        if (Build.VERSION.SDK_INT >= 23) { // 현재 기기의 SDK 버전이 23이상이면
            hour_24 = timePicker.getHour();
            minute = timePicker.getMinute();
        } else { // 현재 기기의 SDK 버전이 23미만이면
            hour_24 = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }

        // 현재 지정된 시간으로 알람 시간 설정
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour_24);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        Date currentDateTime = calendar.getTime();
        String date = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
        Toast.makeText(context, date + "으로 알림이 설정되었습니다!", Toast.LENGTH_SHORT).show();

        return calendar;
    }

}
