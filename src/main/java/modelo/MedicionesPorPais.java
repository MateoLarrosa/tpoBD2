package modelo;

import java.util.Date;

public class MedicionesPorPais {

    public String idSensor;
    public String tipo;
    public String pais;
    public int anio;
    public int mes;
    public Date fecha;
    public double valor;

    public MedicionesPorPais(String idSensor, String tipo, String pais, int anio, int mes, Date fecha, double valor) {
        this.idSensor = idSensor;
        this.tipo = tipo;
        this.pais = pais;
        this.anio = anio;
        this.mes = mes;
        this.fecha = fecha;
        this.valor = valor;
    }

    public MedicionesPorPais() {
    }

    @Override
    public String toString() {
        String fechaUtc = "";
        if (fecha != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            fechaUtc = sdf.format(fecha);
        }
        return "Medición País: " + pais
                + ", Sensor: " + idSensor
                + ", Tipo: " + tipo
                + ", Fecha UTC: " + fechaUtc
                + ", Año: " + anio
                + ", Mes: " + mes
                + ", Valor: " + valor;
    }
}
