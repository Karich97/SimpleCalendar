package com.example.simplecalendar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    private final LocalDate selectedDate;
    private boolean currentMonth = true;

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        selectedDate = LocalDate.now();
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String dayText = daysOfMonth.get(position);
        holder.dayOfMonth.setText(dayText);

        // Проверяем, является ли день выходным или сегодняшним
        if (dayText.isEmpty()) {
            holder.dayOfMonth.setTextColor(Color.TRANSPARENT); // Скрываем текст для пустых ячеек
        } else {
            int day = Integer.parseInt(dayText);
            LocalDate date = selectedDate.withDayOfMonth(day);

            if (currentMonth) {
                if (date.equals(selectedDate)) {
                    holder.dayOfMonth.setTextColor(Color.RED); // Выделяем сегодняшний день
                } else if (date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7) {
                    holder.dayOfMonth.setTextColor(Color.BLUE); // Выделяем выходные (суббота и воскресенье)
                } else {
                    holder.dayOfMonth.setTextColor(Color.BLACK); // Обычный цвет для будних дней
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public void updateDaysInMonth(ArrayList<String> newDaysOfMonth, boolean currentMonth) {
        this.daysOfMonth = newDaysOfMonth;
        this.currentMonth = currentMonth;
    }

    public interface OnItemListener{
        void onItemClick(int position, String dayText);
    }
}
