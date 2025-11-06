package modelo;

import java.util.Date;

public class MedicionesPorZona {

    public String idSensor;
    public String tipo;
    public String zona;
    public int anio;
    public int mes;
    public Date fecha;
    public double valor;
    public double monto;
    public String nombre;
    public Double latitud;
    public Double longitud;
    public String ciudad;
    public String pais;

    public MedicionesPorZona(String idSensor, String tipo, String zona, int anio, int mes, Date fecha, double valor, double monto, String nombre, Double latitud, Double longitud, String ciudad, String pais) {
        this.idSensor = idSensor;
        this.tipo = tipo;
        this.zona = zona;
        this.anio = anio;
        this.mes = mes;
        this.fecha = fecha;
        this.valor = valor;
        this.monto = monto;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    public MedicionesPorZona() {
    }

    @Override
    public String toString() {
        String fechaUtc = "";
        if (fecha != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            fechaUtc = sdf.format(fecha);
        }
        return "Medición Zona: " + zona
                + ", Sensor: " + idSensor
                + ", Tipo: " + tipo
                + ", Fecha UTC: " + fechaUtc
                + ", Año: " + anio
                + ", Mes: " + mes
                + ", Valor: " + valor
                + ", Monto: " + monto;
    }
}
