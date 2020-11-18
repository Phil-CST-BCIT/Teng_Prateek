package com.bcit.teng_prateek;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    LayoutInflater inflater = this.getLayoutInflater();
                    final View view = inflater.inflate(R.layout.update_delete_dialog, null);  // this line

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                    alertDialog.setView(view);

                    final EditText systolicUD = view.findViewById(R.id.editTextSystolicUD);
                    final EditText diastolicUD = view.findViewById(R.id.editTextDiastolicUD);

                    systolicUD.setText("" + readingList.get(getAdapterPosition()).getSystolic());
                    diastolicUD.setText("" + readingList.get(getAdapterPosition()).getDiastolic());

                    DatabaseReference databaseReadings = FirebaseDatabase.getInstance().getReference("users/" + ReadingsActivity.getUser());

                    final DatabaseReference dbRef = databaseReadings.child(readingList.get(getAdapterPosition()).getId());

                    alertDialog.setTitle("Modify / Delete Reading");
                    alertDialog.setPositiveButton("UPDATE",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    int systolic = Integer.parseInt(systolicUD.getText().toString());
                                    int diastolic = Integer.parseInt(diastolicUD.getText().toString());

                                    if(systolic > 180 || diastolic > 120) {
                                        new AlertDialog.Builder(view.getContext())
                                                .setTitle("Hypertensive Crisis")
                                                .setMessage("Consult your doctor immediately")
                                                .setNegativeButton(R.string.dismiss, null)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }


                                    Reading reading = new Reading(readingList.get(getAdapterPosition()).getId(),systolic,diastolic,readingList.get(getAdapterPosition()).getDatetime());

                                    Task setValueTask = dbRef.setValue(reading);

                                    setValueTask.addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            Toast.makeText(view.getContext(),
                                                    "Reading Updated.",Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    setValueTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(view.getContext(),
                                                    "Something went wrong.\n" + e.toString(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                    alertDialog.setNeutralButton("DELETE",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Task setRemoveTask = dbRef.removeValue();
                                    RecyclerAdapter.this.notifyDataSetChanged();
                                    setRemoveTask.addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            Toast.makeText(view.getContext(),
                                                    "Reading Deleted.",Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    setRemoveTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(view.getContext(),
                                                    "Something went wrong.\n" + e.toString(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                    alertDialog.setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
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
