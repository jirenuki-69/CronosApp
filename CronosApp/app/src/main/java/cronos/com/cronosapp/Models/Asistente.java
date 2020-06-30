package cronos.com.cronosapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Asistente implements Parcelable {
    private String matricula;
    private String nombre;
    private String apellidos;
    private String contraseña;
    private String num_grupo;

    public Asistente() {
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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getNum_grupo() {
        return num_grupo;
    }

    public void setNum_grupo(String num_grupo) {
        this.num_grupo = num_grupo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(matricula);
        dest.writeString(nombre);
        dest.writeString(apellidos);
        dest.writeString(contraseña);
        dest.writeString(num_grupo);
    }

    protected Asistente(Parcel in) {
        matricula = in.readString();
        nombre = in.readString();
        apellidos = in.readString();
        contraseña = in.readString();
        num_grupo = in.readString();
    }

    public static final Creator<Asistente> CREATOR = new Creator<Asistente>() {
        @Override
        public Asistente createFromParcel(Parcel in) {
            return new Asistente(in);
        }

        @Override
        public Asistente[] newArray(int size) {
            return new Asistente[size];
        }
    };
}
