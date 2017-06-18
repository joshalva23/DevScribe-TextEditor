package rabbit.document.tool;

public class EstadoArchivo {
    private String rutaAbsoluta, contenido;
    private boolean actualizado;

    public EstadoArchivo (String ruta, String contenido) {
        rutaAbsoluta = ruta;
        this.contenido = contenido;
        this.actualizado = contenido == null;
    }

    public EstadoArchivo (String ruta) {
        this (ruta, null);
    }

    public String getRutaAbsoluta() {
        return rutaAbsoluta;
    }

    public void setRutaAbsoluta(String rutaAbsoluta) {
        this.rutaAbsoluta = rutaAbsoluta;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean isActualizado() {
        return actualizado;
    }

    public void setActualizado(boolean actualizado) {
        this.actualizado = actualizado;
    }

    @Override
    public String toString() {
        return "EstadoArchivo{" +
                "rutaAbsoluta='" + rutaAbsoluta + '\'' +
                ", contenido='" + contenido + '\'' +
                ", actualizado=" + actualizado +
                '}';
    }
}
