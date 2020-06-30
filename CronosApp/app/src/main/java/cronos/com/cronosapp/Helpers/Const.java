package cronos.com.cronosapp.Helpers;

import cronos.com.cronosapp.R;

public class Const {

    public static final String DATA_URL = "http://192.168.0.7/ApiCronos/Api/";
    public static final String URL_LOGIN_PROFESOR = DATA_URL + "loginprofesor";
    public static final String URL_LOGIN_ALUMNO = DATA_URL + "loginalumno";
    public static final String URL_REGISTRO = DATA_URL + "registro";
    public static final String URL_MATERIAS = DATA_URL + "materias/iddocente/";
    public static final String URL_ACTUALIZAR_MATERIA = DATA_URL + "actualizarmateria";
    public static final String URL_GRUPOS = DATA_URL + "grupos";
    public static final String URL_GRUPOS_REGISTRO = DATA_URL + "gruposregistro";
    public static final String URL_GENERAR_CLASE = DATA_URL + "crearclase";
    public static final String URL_SOLICITAR_ASISTENCIA = DATA_URL + "generarasistencia";
    public static final String URL_CLASES = DATA_URL + "clases/";
    public static final String URL_ASISTENTES = DATA_URL + "asistentes/idclase/";

    //PARA EL JSON DE materias
    public static final String TAG_NUM_MATERIA = "num_materia";
    public static final String TAG_NOMBRE_MATERIA = "nombre_materia";

    //PARA EL JSON DE GRUPOS
    public static final String TAG_NUM_GRUPO = "num_grupo";
    public static final String TAG_SEMESTRE = "semestre";
    public static final String TAG_CARRERA = "carrera";

    //PARA EL JSON DE CLASES
    public static final String TAG_ID_CLASE = "id_clase";
    public static final String TAG_MATRICULA_PROFESOR_CLASE = "matricula_profesor";
    public static final String TAG_NUM_MATERIA_CLASE = "num_materia";
    public static final String TAG_NUM_GRUPO_CLASE = "num_grupo";
    public static final String TAG_HORA_GENERADA_CLASE = "hora_generadaqr";
    public static final String TAG_HORA_LIMITE_CLASE = "hora_limiteqr";

    //PARA EL JSON DE ASISTENTES
    public static final String TAG_MATRICULA_ASISTENTE = "matricula";
    public static final String TAG_NOMBRE_ASISTENTE = "nombre";
    public static final String TAG_APELLIDO_ASISTENTE = "apellidos";
    public static final String TAG_CONTRASEÑA_ASISTENTE = "contraseña";
    public static final String TAG_NUM_GRUPO_ASISTENTE = "num_grupo";
}
