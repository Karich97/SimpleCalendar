package com.example.simplecalendar;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    private LocalDate selectedDate;

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

//    @Override
//    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
//        String dayText = daysOfMonth.get(position);
//        holder.dayOfMonth.setText(dayText);
//
//        // Установка рамки
//        GradientDrawable border = new GradientDrawable();
//        //border.setColor(Color.TRANSPARENT); // Цвет фона
//        border.setStroke(2, Color.LTGRAY); // Толщина и цвет рамки
//
//        if (dayText.isEmpty()) {
//            holder.dayOfMonth.setTextColor(Color.TRANSPARENT);
//        } else {
//            int day = Integer.parseInt(dayText);
//            LocalDate date = selectedDate.withDayOfMonth(day);
//
//            if (date.equals(LocalDate.now())) {
//                holder.dayOfMonth.setTextColor(Color.BLACK);
//                //holder.itemView.setBackgroundColor(Color.BLUE); // Фон для сегодняшней даты
//                border.setStroke(10, Color.BLUE); // Толщина и цвет рамки
//                //border.setColor(Color.BLUE);
//            } else if (date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7) {
//                holder.dayOfMonth.setTextColor(Color.BLUE);
//                //holder.itemView.setBackgroundColor(Color.DKGRAY); // Фон для выходных
//                //border.setStroke(2, Color.WHITE); // Толщина и цвет рамки
//                //border.setColor(Color.DKGRAY);
//            } else {
//                holder.dayOfMonth.setTextColor(Color.BLACK);
//                //holder.itemView.setBackgroundColor(Color.WHITE); // Фон для будних дней
//                border.setColor(Color.WHITE);
//            }
//            holder.itemView.setBackground(border);
//        }
//    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String dayText = daysOfMonth.get(position);
        holder.dayOfMonth.setText(dayText);

        GradientDrawable border = new GradientDrawable();
        float cornerRadius = 10f;
        border.setCornerRadius(cornerRadius); // Радиус скругления
        border.setStroke(2, Color.LTGRAY); // Толщина и цвет рамки
        border.setColor(Color.WHITE); // Цвет фона

        if (dayText.isEmpty()) {
            holder.dayOfMonth.setTextColor(Color.TRANSPARENT);
        } else {
            int day = Integer.parseInt(dayText);
            LocalDate date = selectedDate.withDayOfMonth(day);

            // Для выходных дней
            if (date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7) {
                holder.dayOfMonth.setTextColor(Color.BLUE);
            }
            // Для будних дней
            else {
                holder.dayOfMonth.setTextColor(Color.BLACK);
            }
            // Для сегодняшней даты
            if (date.equals(LocalDate.now())) {
                border.setStroke(14, Color.BLUE); // Толщина и цвет рамки для сегодняшней даты
            }

            holder.itemView.setBackground(border);
        }
    }


    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public void updateDaysInMonth(ArrayList<String> newDaysOfMonth, LocalDate date) {
        this.daysOfMonth = newDaysOfMonth;
        this.selectedDate = date;
    }

    public interface OnItemListener{
        void onItemClick(int position, String dayText);
    }
}
