package cronos.com.cronosapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Clase implements Parcelable {
    private int id_clase;
    private String matricula_profesor;
    private String num_materia;
    private String num_grupo;
    private String hora_generadaqr;
    private String hora_limiteqr;

    public Clase() {
    }

    public int getId_clase() {
        return id_clase;
    }

    public void setId_clase(int id_clase) {
        this.id_clase = id_clase;
    }

    public String getMatricula_profesor() {
        return matricula_profesor;
    }

    public void setMatricula_profesor(String matricula_profesor) {
        this.matricula_profesor = matricula_profesor;
    }

    public String getNum_materia() {
        return num_materia;
    }

    public void setNum_materia(String num_materia) {
        this.num_materia = num_materia;
    }

    public String getNum_grupo() {
        return num_grupo;
    }

    public void setNum_grupo(String num_grupo) {
        this.num_grupo = num_grupo;
    }

    public String getHora_generadaqr() {
        return hora_generadaqr;
    }

    public void setHora_generadaqr(String hora_generadaqr) {
        this.hora_generadaqr = hora_generadaqr;
    }

    public String getHora_limiteqr() {
        return hora_limiteqr;
    }

    public void setHora_limiteqr(String hora_limiteqr) {
        this.hora_limiteqr = hora_limiteqr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_clase);
        dest.writeString(matricula_profesor);
        dest.writeString(num_materia);
        dest.writeString(num_grupo);
        dest.writeString(hora_generadaqr);
        dest.writeString(hora_limiteqr);
    }

    protected Clase(Parcel in) {
        id_clase = in.readInt();
        matricula_profesor = in.readString();
        num_materia = in.readString();
        num_grupo = in.readString();
        hora_generadaqr = in.readString();
        hora_limiteqr = in.readString();
    }

    public static final Creator<Clase> CREATOR = new Creator<Clase>() {
        @Override
        public Clase createFromParcel(Parcel in) {
            return new Clase(in);
        }

        @Override
        public Clase[] newArray(int size) {
            return new Clase[size];
        }
    };
}
