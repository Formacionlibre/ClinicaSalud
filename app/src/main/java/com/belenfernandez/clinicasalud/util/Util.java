package com.belenfernandez.clinicasalud.util;

import android.util.Log;

import com.belenfernandez.clinicasalud.modelo.Cita;
import com.belenfernandez.clinicasalud.modelo.Especialista;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Util {

    //clase para conversiones de fechas y horas. Tambien incluye los huecos disponibles para citas con especialista

    public static final String PATRON_FECHA_SOLO_ENG = "yyyy-MM-dd";
    public static final String PATRON_FECHA = "dd-MM-yyyy (HH:mm)";
    public static final String PATRON_FECHA_BBDD = "yyyy-MM-dd HH:mm:ss";

    private static DateFormat df_eng_solo_fecha = new SimpleDateFormat(PATRON_FECHA_SOLO_ENG);
    private static DateFormat df = new SimpleDateFormat(PATRON_FECHA);
    private static DateFormat dfBBDD = new SimpleDateFormat(PATRON_FECHA_BBDD);

    public static String dateToString(Date d) {
        return df.format(d);
    }

    public static Date stringToDate(String s) {
        try {
            return df.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date convertirFechaHoraBBDD(String s) {
        try {
            return dfBBDD.parse(s);
        } catch (ParseException e) {
            Log.d("XXX","Error parseo: " + e.getMessage());
            return null;
        }
    }

    public static List<String> obtenerEspecialidadesDiferentes(List<Especialista> especialistas) {

        List<String> especialidades = new ArrayList<>();
        for(Especialista e : especialistas) {
            if(!especialidades.contains(e.getEspecialidad()))
                especialidades.add(e.getEspecialidad());
        }
        return especialidades;

    }

    //metodo que sirve para generar cadenas que representan fechas y horas correspondientes a huecos disponibles de un especialista entre
    //los rangos elegidos
    public static List<String> huecosDisponibles(String inicio, String fin, List<Cita> citasEspecialista) {

        List<String> huecosDisponibles = new ArrayList<>();

        // inicio y fin deben estar en formato dd-MM-yyyy
        int diaInicio = Integer.valueOf(inicio.split("-")[0]);
        int mesInicio = Integer.valueOf(inicio.split("-")[1]);
        int anyoInicio = Integer.valueOf(inicio.split("-")[2]);

        int diaFin = Integer.valueOf(fin.split("-")[0]);
        int mesFin = Integer.valueOf(fin.split("-")[1]);
        int anyoFin = Integer.valueOf(fin.split("-")[2]);

        // restamos 1 a los meses, pues para Calendar, el mes 0 es enero....
        Calendar calInicio = Calendar.getInstance();
        calInicio.set(anyoInicio,mesInicio-1,diaInicio);
        Calendar calFinal = Calendar.getInstance();
        calFinal.set(anyoFin,mesFin-1,diaFin);
        // inicio calendario
        while(calInicio.before(calFinal)) {

            for(int i = 9; i<=16; i++) {
                for(int j = 0; j<=45; j+=15) {
                    String horario = "";
                    if(i<10)
                        horario = "0" + i + ":";
                    else
                        horario = i + ":";

                    if(j<10)
                        horario =  horario + "0" + j;
                    else
                        horario = horario + j;

                    //mirar si tiene cita pendiente
                    String dia = df_eng_solo_fecha.format(calInicio.getTime());
                    //String dia = calInicio.get(Calendar.YEAR)+"-"+calInicio.get(Calendar.MONTH)+"-"+calInicio.get(Calendar.DAY_OF_MONTH);
                    String hora = horario + ":00";

                    if(!tieneCitaPendiente(dia,hora,citasEspecialista)) {
                        horario = dia + " " + horario;
                        huecosDisponibles.add(horario);
                        Log.d("XXX", "Fecha y hora disponible: " + horario);
                    }

                }
            }

            // sumar hasta fecha final indicada. Omite dias de find de semana
            calInicio.add(Calendar.DATE,1);
            while(calInicio.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || calInicio.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
                calInicio.add(Calendar.DATE,1);

        }

        return huecosDisponibles;
    }

    // metodo que me dice si una fecha/hora ya esta cogida en una lista de citas pendientes de un especialista
    private static boolean tieneCitaPendiente(String fecha, String hora, List<Cita> citasEspecialista) {
        boolean tieneCita = false;
        String cadena_fecha = fecha + " " + hora;
        Date fecha_cita = Util.convertirFechaHoraBBDD(cadena_fecha);
        for(Cita c : citasEspecialista) {
            if(c.getFecha().equals(fecha_cita))
                tieneCita = true;
        }
        return tieneCita;
    }
}
