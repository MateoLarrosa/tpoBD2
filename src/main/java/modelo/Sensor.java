package modelo;

import java.util.Date;

public class Sensor {

    private String id;
    private String nombre;
    private TipoSensor tipo;
    private double latitud;
    private double longitud;
    private String ciudad;
    private String pais;
    private String zona;
    private EstadoSensor estado;
    private Date fechaInicioEmision;

    public Sensor() {
    }

    public Sensor(String id, String nombre, TipoSensor tipo, double latitud, double longitud, String ciudad, String pais, String zona, EstadoSensor estado, Date fechaInicioEmision) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ciudad = ciudad;
        this.pais = pais;
        this.zona = zona;
        this.estado = estado;
        this.fechaInicioEmision = fechaInicioEmision;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoSensor getTipo() {
        return tipo;
    }

    public void setTipo(TipoSensor tipo) {
        this.tipo = tipo;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public EstadoSensor getEstado() {
        return estado;
    }

    public void setEstado(EstadoSensor estado) {
        this.estado = estado;
    }

    public Date getFechaInicioEmision() {
        return fechaInicioEmision;
    }

    public void setFechaInicioEmision(Date fechaInicioEmision) {
        this.fechaInicioEmision = fechaInicioEmision;
    }
}
