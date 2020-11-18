package com.bcit.teng_prateek;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    List<Reading> readingList;

    public RecyclerAdapter(List<Reading> readingList) {
        this.readingList = readingList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView date;
        TextView sysdias;
        TextView note;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.readingRowDateTextView);
            sysdias = itemView.findViewById(R.id.readingRowSysDiasTextView);
            note = itemView.findViewById(R.id.readingRowNoteTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(itemView.getContext(), "SHORT" + getAdapterPosition(),Toast.LENGTH_SHORT).show();

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    Toast.makeText(itemView.getContext(), "LONG" + getAdapterPosition(),Toast.LENGTH_SHORT).show();

                    LayoutInflater inflater = this.getLayoutInflater();
                    View view = inflater.inflate(R.layout.update_delete_dialog, null);  // this line

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                    alertDialog.setView(view);

                    EditText systolicUD = view.findViewById(R.id.editTextSystolicUD);
                    EditText diastolicUD = view.findViewById(R.id.editTextDiastolicUD);


                    systolicUD.setText("" + readingList.get(getAdapterPosition()).getSystolic());
                    diastolicUD.setText("" + readingList.get(getAdapterPosition()).getDiastolic());

                    alertDialog.setTitle("Modify / Delete Reading");
                    alertDialog.setPositiveButton("UPDATE",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    alertDialog.setNeutralButton("DELETE",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    alertDialog.setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    alertDialog.show();
                    return false;
                }

                private LayoutInflater getLayoutInflater() {
                    return LayoutInflater.from(itemView.getContext());
                }
            });

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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
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
