package com.ieseljust.ad.myDBMS;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;  
/**
 *
 * @author joange
 */
public class Leer {
	private final static InputStreamReader isr = new InputStreamReader(System.in);
	private final static BufferedReader entradaConsola = new java.io.BufferedReader(isr);

	/**
	 * Llig un text del teclat.
	 * 
	 * @param mensaje Text que es passa a l'usuaro
	 * @return el text introduit. Blanc en cas d'error
	 */
	public static String leerTexto(String mensaje) {
		limpiarBuffer();
		String respuesta = null;
		do {
			try {
				System.out.print(mensaje);
				respuesta = entradaConsola.readLine();
			} // ()
			catch (IOException ex) {
				return "";
			}
		} while (respuesta == null);
		return respuesta;

	} // ()

	/**
	 * Introducció de numeros enters
	 * 
	 * @param mensaje Missatge que es dona a l'usuari
	 * @return un enter amb el valor
	 */
	public static int leerEntero(String mensaje) {
		int n = 0;
		boolean aconseguit = false;
		while (!aconseguit) {
			try {
				String text = leerTexto(mensaje);
				if(!text.equals("")) {
					n = Integer.parseInt(text);
				} else {
					return 0;
				}
				aconseguit = true;
			} catch (NumberFormatException ex) {
				ConsoleColors.printError("Deus posar un numero correcte");
			}
		}
		return n;
	}

	/**
	 * 
	 * @param mensaje
	 * @param limite  desde 0 hasta X, no se permiten limites negativos.
	 * @return
	 */
	public static int leerEntero(String mensaje, int limite) {
		int n = leerEntero(mensaje);
		if (!(n < limite && n > 0)) {
			ConsoleColors.printError("Deus posar un numero correcte maxim ="+ limite);
			return leerEntero(mensaje, limite);
		}
		return n;
	}
	/**
	 * Lee un entero entre A y B.
	 * @param mensaje
	 * @param minimo
	 * @param maximo 
	 * @return
	 */
	public static int leerEntero(String mensaje, int minimo, int maximo) {
		int n = leerEntero(mensaje);
		if (!(n > minimo && n < maximo)) {
			ConsoleColors.printError(String.format("Tienes que elegir un numero entre %d y %d",minimo,maximo));
			return leerEntero(mensaje);
		}
		return n;
	}
	/**
	 * Introducció de numeros enters
	 * 
	 * @param mensaje Missatge que es dona a l'usuari
	 * @return un enter amb el valor
	 */
	public static double leerDouble(String mensaje) {
		double dNumber = leerBigDecimal(mensaje).doubleValue();
		if(dNumber != Double.NEGATIVE_INFINITY && dNumber != Double.POSITIVE_INFINITY && dNumber != Double.NaN) {
			return dNumber;
		} else {
			ConsoleColors.printError("Error con el numero Double insertado, vuelve a intentarlo.");
			return leerDouble(mensaje);
		}
	}
	
	public static boolean leerBoolean(String mensaje) {
		String bool = leerTexto(mensaje);
		if(bool.equalsIgnoreCase("Si") || bool.equalsIgnoreCase("S") || bool.equalsIgnoreCase("Y") || bool.equalsIgnoreCase("Yes")) {
			return true;
		}
		else if(bool.equalsIgnoreCase("no") || bool.equalsIgnoreCase("n")) {
			return false;
		}
		else {
			ConsoleColors.printError("Error en la introduccion, opciones S/N.");
			return leerBoolean(mensaje);
		}
	}
	
	public static int leerCaracter(String mensaje, int minimo, int maximo) {
		int c = 1;
		System.out.print(mensaje);
		try {
			c =  entradaConsola.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(c == '0') {
			return 0;
		}
		if(c < minimo || c > maximo) {
			String msg = String.format("Caracter minimo %c maximo %c",(char) minimo,(char) maximo);
			ConsoleColors.printError(msg);
			limpiarBuffer();
			return leerCaracter(mensaje,minimo,maximo);
		}
		return c;
	}
	
	private static boolean limpiarBuffer() {
		try {
			if(entradaConsola.ready()) {
				entradaConsola.readLine();
				return true;
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static java.util.Date leerFecha(String mensaje) {
		try {
			String posibleFecha = leerTexto(mensaje + " Formato: yyyy/MM/dd -> ");
			if(posibleFecha.equals("")) {
				return new Date(0L);
			} else {
				return new SimpleDateFormat("yyyy/MM/dd").parse(posibleFecha);
			}
		} catch (ParseException e) {
			return leerFecha("Error, intentalo de nuevo");
		}
	}
	public static java.sql.Date leerFechaSQL(String mensaje) {
		return new java.sql.Date(leerFecha(mensaje).getTime());
	}

	public static float leerFloat(String mensaje) {
		return (float) Leer.leerDouble(mensaje);
	}

	public static BigDecimal leerBigDecimal(String mensaje) {
		BigDecimal n = null;
		boolean aconseguit = false;
		while (!aconseguit) {
			try {
				String texto = leerTexto(mensaje);
				if(texto.isBlank()) {
					n = new BigDecimal("0.0");
				} else {
					n = new BigDecimal(texto);
				}
				aconseguit = true;
			} catch (NumberFormatException ex) {
				ConsoleColors.printError("Deus posar un numero correcte");
				n = leerBigDecimal(mensaje);
			}
		}
		return n;
	}

	public static long leerLong(String mensaje) {
		long n = 0;
		boolean aconseguit = false;
		while (!aconseguit) {
			try {
				String texto = leerTexto(mensaje);
				if(texto.isBlank()) {
					n = 0L;
				} else {
					n = Long.parseLong(texto);
				}
				aconseguit = true;
			} catch (NumberFormatException ex) {
				ConsoleColors.printError("Deus posar un numero correcte");
				n = leerLong(mensaje);
			}
		}
		return n;
	}
	
	
}
