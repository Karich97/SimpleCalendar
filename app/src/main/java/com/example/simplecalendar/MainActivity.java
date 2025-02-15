package com.example.simplecalendar;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends ComponentActivity implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private Switch themeSwitch;
    private LinearLayout rootView;
    private LinearLayout topLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWigets();
        selectedDate = LocalDate.now();
        setMonthView();
        initThems();
    }

    private void initWigets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        themeSwitch = findViewById(R.id.themeSwitch);
        rootView = findViewById(R.id.rootLayout);
        topLayout = findViewById(R.id.topLayout);
    }

    private void initThems() {
        // Загружаем сохранённую тему
        SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        boolean isDarkMode = preferences.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        themeSwitch.setChecked(isDarkMode);
        updateColors(isDarkMode);

        // Устанавливаем слушатель для переключателя
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Сохраняем состояние переключателя
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("dark_mode", isChecked);
                editor.apply();

                // Меняем тему
                AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                updateColors(isChecked);
            }
        });
    }

    // Метод для обновления цветов
    private void updateColors(boolean isDarkMode) {
        int backgroundColor = getResources().getColor(isDarkMode ? R.color.background_color_dark : R.color.background_color_light);
        int textColor = getResources().getColor(isDarkMode ? R.color.text_color_dark : R.color.text_color_light);
        int bordersColor = getResources().getColor(isDarkMode ? R.color.background_color_dark : R.color.border_color_light);
        int buttonsColor = getResources().getColor(isDarkMode ? R.color.button_color_dark : R.color.button_color_light);
        int buttonsTextColor = getResources().getColor(isDarkMode ? R.color.button_text_color_dark : R.color.button_text_color_light);
        int switchColor = getResources().getColor(isDarkMode ? R.color.switch_text_color_dark : R.color.switch_text_color_light);

        rootView.setBackgroundColor(backgroundColor);
        topLayout.setBackgroundColor(backgroundColor);

        // Устанавливаем цвет текста
        monthYearText.setTextColor(textColor);
        themeSwitch.setThumbResource(isDarkMode ? R.drawable.switch_thumb : R.drawable.switch_thumb);
        themeSwitch.setTrackTintList(ColorStateList.valueOf(textColor));
        for (int i = 0; i < topLayout.getChildCount(); i++) {
            View child = topLayout.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(textColor);
            }
        }

//        calendarRecyclerView.setBackgroundColor(textColor);
//
//        // Обновляем цвет текста для кнопок
//        Button backButton = findViewById(R.id.backButton); // Замените на ваш ID
//        Button forwardButton = findViewById(R.id.forwardButton); // Замените на ваш ID
//        backButton.setTextColor(buttonTextColor);
//        forwardButton.setTextColor(buttonTextColor);
//
//        // Обновляем цвет рамки
//        LinearLayout borderLayout = findViewById(R.id.borderLayout); // Замените на ваш ID
//        borderLayout.setBackgroundColor(borderColor);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonth(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        calendarAdapter.updateDaysInMonth(daysInMonth, selectedDate);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonth(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() - 1;

        for (int i = 1; i < 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }

        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.isEmpty()){
            String message = dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
