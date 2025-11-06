package modelo;

import java.util.Date;

public class MedicionesPorCiudad {

    public String idSensor;
    public String tipo;
    public String ciudad;
    public int anio;
    public int mes;
    public Date fecha;
    public double valor;
    public double monto;
    public String nombre;
    public Double latitud;
    public Double longitud;
    public String pais;
    public String zona;

    public MedicionesPorCiudad(String idSensor, String tipo, String ciudad, int anio, int mes, Date fecha, double valor, double monto, String nombre, Double latitud, Double longitud, String pais, String zona) {
        this.idSensor = idSensor;
        this.tipo = tipo;
        this.ciudad = ciudad;
        this.anio = anio;
        this.mes = mes;
        this.fecha = fecha;
        this.valor = valor;
        this.monto = monto;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.pais = pais;
        this.zona = zona;
    }

    public MedicionesPorCiudad() {
    }

    @Override
    public String toString() {
        String fechaUtc = "";
        if (fecha != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            fechaUtc = sdf.format(fecha);
        }
        return "Medición Ciudad: " + ciudad
                + ", Sensor: " + idSensor
                + ", Tipo: " + tipo
                + ", Fecha UTC: " + fechaUtc
                + ", Año: " + anio
                + ", Mes: " + mes
                + ", Valor: " + valor
                + ", Monto: " + monto;
    }
}
