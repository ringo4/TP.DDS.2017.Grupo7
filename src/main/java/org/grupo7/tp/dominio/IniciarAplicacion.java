/*
 * 
 * CLASE DE PRUEBA
 * 
 */


package org.grupo7.tp.dominio;

import java.util.Scanner;

public class IniciarAplicacion {
	private AdapterJson converterJsonaListaEmpresas;
	
	
	public void cargarEmpresasDesdeJson(String jsonEmpresas) {
		this.converterJsonaListaEmpresas
		Repositorio.getInstance();
		
	}

	public static void main(String[] args) {
		System.out.print("Ingresar el valor de una cuenta \n");
		
		Scanner sc = new Scanner(System.in);
		
		Repositorio repo = Repositorio.getInstance();
		
		do {
		
			float valor = sc.nextFloat();
			
			Cuenta cuenta = new Cuenta();
		
			cuenta.setValor(valor);
		
			repo.agregarCuenta(cuenta);
		
		} while (sc.hasNextFloat());
		
		repo.consultarCuentas();
		
		sc.close();
		
		

	}

}
