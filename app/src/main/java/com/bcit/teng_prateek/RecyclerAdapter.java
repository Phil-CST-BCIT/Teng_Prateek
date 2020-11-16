package com.bcit.teng_prateek;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    List<Reading> readingList;

    public RecyclerAdapter(List<Reading> readingList) {
        this.readingList = readingList;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView sysdias;
        TextView note;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.readingRowDateTextView);
            sysdias = itemView.findViewById(R.id.readingRowSysDiasTextView);
            note = itemView.findViewById(R.id.readingRowNoteTextView);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.reading_row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(readingList.get(position).getDatetime());
        holder.sysdias.setText(readingList.get(position).getSystolic() + "/" + readingList.get(position).getDiastolic());
        holder.note.setText(getNote(readingList.get(position).getSystolic(), readingList.get(position).getDiastolic()));
    }

    private String getNote(int systolic, int diastolic) {
        if ((systolic >= 180) || (diastolic >= 120)) return "! CRISIS";
        else if ((systolic >= 140) || (diastolic >= 90)) return "High BP (Stage II)";
        else if ((systolic >= 130) || (diastolic >= 80)) return "High BP (Stage I)";
        else if (systolic >= 120 && systolic <= 129) return "Elevated";
        else return "Normal";
    }


    @Override
    public int getItemCount() {
        return readingList.size();
    }
}
