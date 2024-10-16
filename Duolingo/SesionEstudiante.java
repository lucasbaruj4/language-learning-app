package Duolingo;

public class SesionEstudiante {
    private static SesionEstudiante instancia;
    private int idEstudiante;
    private String nombreUsuario;

    // Constructor privado para el patrón Singleton
    private SesionEstudiante() {
    }

    // Método para obtener la instancia única de SesionEstudiante
    public static SesionEstudiante getInstance() {
        if (instancia == null) {
            instancia = new SesionEstudiante();
        }
        return instancia;
    }

    // Método para iniciar sesión y establecer el ID y nombre del estudiante
    public void iniciarSesion(int id, String nombre) {
        this.idEstudiante = id;
        this.nombreUsuario = nombre;
    }

    // Método para obtener el ID del estudiante
    public int getIdEstudiante() {
        return idEstudiante;
    }

    // Método para obtener el nombre del usuario
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    // Método para cerrar la sesión
    public void cerrarSesion() {
        idEstudiante = -1;
        nombreUsuario = null;
    }
}
