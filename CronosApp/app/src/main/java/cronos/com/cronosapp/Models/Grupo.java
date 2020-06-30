package cronos.com.cronosapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Grupo implements Parcelable {
    private int num_grupo;
    private String semestre;
    private String carrera;

    public Grupo() {
    }

    public int getNum_grupo() {
        return num_grupo;
    }

    public void setNum_grupo(int num_grupo) {
        this.num_grupo = num_grupo;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(num_grupo);
        dest.writeString(semestre);
        dest.writeString(carrera);
    }

    protected Grupo(Parcel in) {
        num_grupo = in.readInt();
        semestre = in.readString();
        carrera = in.readString();
    }

    public static final Creator<Grupo> CREATOR = new Creator<Grupo>() {
        @Override
        public Grupo createFromParcel(Parcel in) {
            return new Grupo(in);
        }

        @Override
        public Grupo[] newArray(int size) {
            return new Grupo[size];
        }
    };
}
