package cronos.com.cronosapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Materia implements Parcelable {

    private int num_materia;
    private String nombre_materia;

    public Materia() {
    }

    public int getNum_materia() {
        return num_materia;
    }

    public void setNum_materia(int num_materia) {
        this.num_materia = num_materia;
    }

    public String getNombre_materia() {
        return nombre_materia;
    }

    public void setNombre_materia(String nombre_materia) {
        this.nombre_materia = nombre_materia;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(num_materia);
        dest.writeString(nombre_materia);
    }

    protected Materia(Parcel in) {
        num_materia = in.readInt();
        nombre_materia = in.readString();
    }

    public static final Creator<Materia> CREATOR = new Creator<Materia>() {
        @Override
        public Materia createFromParcel(Parcel in) {
            return new Materia(in);
        }

        @Override
        public Materia[] newArray(int size) {
            return new Materia[size];
        }
    };
}
