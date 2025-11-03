package modelo;

import java.io.Serializable;
import java.util.Date;

public class Sesion implements Serializable {

    private String idSesion;
    private String idUsuario;
    private Date fechaHoraInicio;
    private Date fechaHoraCierre;
    private String estado;

    public Sesion() {
    }

    public Sesion(String idSesion, String idUsuario, Date fechaHoraInicio, String estado) {
        this.idSesion = idSesion;
        this.idUsuario = idUsuario;
        this.fechaHoraInicio = fechaHoraInicio;
        this.estado = estado;
    }

    public String getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(String idSesion) {
        this.idSesion = idSesion;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(Date fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Date getFechaHoraCierre() {
        return fechaHoraCierre;
    }

    public void setFechaHoraCierre(Date fechaHoraCierre) {
        this.fechaHoraCierre = fechaHoraCierre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
