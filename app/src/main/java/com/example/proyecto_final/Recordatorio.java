package com.example.proyecto_final;
public class Recordatorio {
    private String id;
    private String fecha;
    private String hora;
    private String mensaje;
    private String mensajeActualizado; // Nuevo campo para el mensaje actualizado

    public Recordatorio() {
        // Constructor vacío requerido
    }

    public Recordatorio(String id, String fecha, String hora, String mensaje) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.mensaje = mensaje;
    }

    public String getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getMensaje() {
        return mensaje;
    }
    // Método para actualizar el mensaje
    public void setMensajeActualizado(String mensajeActualizado) {
        this.mensajeActualizado = mensajeActualizado;
    }

    // Método para obtener el mensaje actualizado
    public String getMensajeActualizado() {
        return mensajeActualizado;
    }

}

