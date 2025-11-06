package modelo;

import java.util.Date;

public class Mediciones {

    private String id;
    private String idSensor;
    private Date fecha;
    private double valor;
    private double monto;

    public Mediciones() {
    }

    public Mediciones(String id, String idSensor, Date fecha, double valor, double monto) {
        this.id = id;
        this.idSensor = idSensor;
        this.fecha = fecha;
        this.valor = valor;
        this.monto = monto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(String idSensor) {
        this.idSensor = idSensor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    @Override
    public String toString() {
        return "Mediciones{"
                + "id='" + id + '\''
                + ", idSensor='" + idSensor + '\''
                + ", fecha=" + fecha
                + ", valor=" + valor
                + ", monto=" + monto
                + '}';
    }
}
