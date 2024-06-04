package com.example.proyecto_final;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Layout;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecordatorioAdapter extends RecyclerView.Adapter<RecordatorioAdapter.RecordatorioViewHolder> {
    private Context context;
    private List<Recordatorio> recordatorios;

    public RecordatorioAdapter(Context context, List<Recordatorio> recordatorios) {
        this.context = context;
        this.recordatorios = recordatorios;
    }

    @Override
    public RecordatorioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recordatorio_item, parent, false);
        return new RecordatorioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordatorioViewHolder holder, int position) {
        Recordatorio recordatorio = recordatorios.get(position);
        holder.txtRecordatorio.setText(recordatorio.getMensaje());
        holder.txtFecha.setText("Fecha: " + recordatorio.getFecha());
        holder.txtHora.setText("Hora: " + recordatorio.getHora());
    }

    @Override
    public int getItemCount() {
        return recordatorios.size();
    }

    class RecordatorioViewHolder extends RecyclerView.ViewHolder {
        TextView txtRecordatorio, txtFecha, txtHora;
        ImageView btnEditar, btnBorrar, btnClose;
        private String stringDateSelected;


        public RecordatorioViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRecordatorio = itemView.findViewById(R.id.txtRecordatorio);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtHora = itemView.findViewById(R.id.txtHora);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
            btnClose = itemView.findViewById(R.id.btnClose);

//BORRAR DATOS
            btnBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Comprueba si la lista está vacía antes de intentar eliminar un elemento
                    if (!recordatorios.isEmpty()) {
                        // Obtén la posición del elemento en el RecyclerView
                        int position = getAdapterPosition();

                        // Comprueba si la posición es válida
                        if (position != RecyclerView.NO_POSITION && position < recordatorios.size()) {
                            // Obtén el ID y el objeto Recordatorio que se va a eliminar
                            String id = recordatorios.get(position).getId();
                            Recordatorio recordatorio = recordatorios.get(position);

                            // Crea una referencia a la base de datos de Firebase
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");

                            // Elimina el recordatorio de la base de datos
                            databaseReference.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(v.getContext(), "Recordatorio eliminado", Toast.LENGTH_SHORT).show();
                                        // Si la eliminación fue exitosa, actualiza el RecyclerView
                                        int index = recordatorios.indexOf(recordatorio);
                                        if (index != -1) {
                                            recordatorios.remove(index);
                                            notifyItemRemoved(index);
                                            notifyItemRangeChanged(index, recordatorios.size());
                                            //Toast.makeText(v.getContext(), "Recordatorio eliminado", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // Si hubo un error, muestra un mensaje
                                        Toast.makeText(context, "Error al eliminar el recordatorio", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(context, "No hay recordatorios para eliminar", Toast.LENGTH_SHORT).show();
                    }
                }
            });


//EDITAR DATOS
            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Crea un nuevo diálogo usando el contexto de la actividad
                    Dialog dialog = new Dialog(context);

                    // Infla el layout para el diálogo
                    dialog.setContentView(R.layout.recordatorio_editar);

                    // Encuentra los campos de entrada en el layout del diálogo
                    EditText edtdlgEditarMensaje = dialog.findViewById(R.id.edtdlgEditarMensaje);
                    CalendarView dlgEditarcalendarView = dialog.findViewById(R.id.dlgEditarcalendarView);
                    TimePicker dlgEditartimePicker = dialog.findViewById(R.id.dlgEditartimePicker);

                    // Obtén la posición del elemento en el RecyclerView
                    int position = getAdapterPosition();

                    // Obtén el recordatorio que se va a editar
                    Recordatorio recordatorio = recordatorios.get(position);

                    // Llena los campos de entrada con los datos actuales del recordatorio
                    edtdlgEditarMensaje.setText(recordatorio.getMensaje());

                    // Convierte la fecha del recordatorio a milisegundos y la establece en el CalendarView
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    String fechaRecordatorio = recordatorio.getFecha();
                    if (fechaRecordatorio != null) {
                        try {
                            Date date = sdf.parse(fechaRecordatorio);
                            if (date != null) {
                                dlgEditarcalendarView.setDate(date.getTime());
                                dlgEditarcalendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                                    stringDateSelected = dayOfMonth + "-" + (month + 1) + "-" + year;
                                });
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }


                    // Encuentra el botón "Guardar" en el layout del diálogo
                    ImageView btndlgEditarGuardar = dialog.findViewById(R.id.btndlgEditarGuardar);

                    btndlgEditarGuardar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Obtén los datos actualizados de los campos de entrada
                            String mensajeActualizado = edtdlgEditarMensaje.getText().toString();
                            String fechaActualizada = stringDateSelected;
                            String horaActualizada = dlgEditartimePicker.getHour() + ":" + dlgEditartimePicker.getMinute();

                            // Verifica si los datos están vacíos o son null
                            if(mensajeActualizado == null || mensajeActualizado.isEmpty() ||
                                    fechaActualizada == null || fechaActualizada.isEmpty() ||
                                    horaActualizada == null || horaActualizada.isEmpty()) {
                                Toast.makeText(context, "Por favor, completa todos los campos antes de guardar.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Actualiza los datos en Firebase
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");
                                databaseReference.child(recordatorio.getId()).child("mensaje").setValue(mensajeActualizado);
                                databaseReference.child(recordatorio.getId()).child("fecha").setValue(fechaActualizada);
                                databaseReference.child(recordatorio.getId()).child("hora").setValue(horaActualizada);

                                // Actualiza los datos en la lista recordatorios y notifica al adaptador del cambio
                                recordatorio.setMensajeActualizado(mensajeActualizado);
                                // Aquí debes actualizar la fecha y la hora en el objeto recordatorio
                                notifyItemChanged(position);

                                // Muestra un Toast confirmando que se editó con éxito
                                Toast.makeText(context, "Recordatorio editado con éxito", Toast.LENGTH_SHORT).show();

                                // Cierra el diálogo
                                dialog.dismiss();
                            }
                        }
                    });


                    // Muestra el diálogo
                    dialog.show();


                    // Crea un nuevo diálogo usando el contexto de la actividad
                    //final Dialog dialog = new Dialog(context);

                    // Inflar el layout para el diálogo
                    //dialog.setContentView(R.layout.recordatorio_editar);

                    // Encuentra el botón "Cerrar" en el layout del diálogo
                    ImageView btnClose = dialog.findViewById(R.id.btnClose);

                    // Establece el OnClickListener para el botón "Cerrar"
                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Cierra el diálogo
                            dialog.dismiss();
                        }
                    });

                }
            });


        }
    }
}