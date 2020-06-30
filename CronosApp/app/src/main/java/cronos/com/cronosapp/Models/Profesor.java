package cronos.com.cronosapp.Models;

public class Profesor {
    public String status;
    public String error;
    public String matricula;
    public String nombre;
    public String apellidos;


    public Profesor(String status, String error, String matricula, String nombre, String apellidos) {
        this.status = status;
        this.error = error;
        this.matricula = matricula;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
