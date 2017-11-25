package ar.edu.utn.frba.dds.tp.dominio;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.IOUtils;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import ar.edu.utn.frba.dds.tp.herramientas.AdapterJson;
import ar.edu.utn.frba.dds.tp.antlr.CalculadoraLexer;
import ar.edu.utn.frba.dds.tp.antlr.CalculadoraParser;
import ar.edu.utn.frba.dds.tp.antlr.CalculadoraParser.ExpresionContext;
import ar.edu.utn.frba.dds.tp.antlr.dds.*;

public final class  Aplicacion {

	

	public static void cargarEmpresasDesdeJson(String jsonEmpresas) throws FileNotFoundException {
		List<Empresa> listaEmpresas = new ArrayList<Empresa>();
		listaEmpresas = AdapterJson.transformarDeJSONaListaEmpresas(jsonEmpresas);
		Repositorio.getInstance().cargarListaDeEmpresas(listaEmpresas);
		//cargar ind predefinidos
		}
	
	public static void persistirEmpresasDesdeJson(String jsonEmpresas) throws FileNotFoundException {
		List<Empresa> listaEmpresas = new ArrayList<Empresa>();
		listaEmpresas = AdapterJson.transformarDeJSONaListaEmpresas(jsonEmpresas);
		
		//Cargar en memoria las empresas y cuentas. Asi despues Persistir se hace
		//sobre objetos existentes en memoria.
		Repositorio.getInstance().cargarListaDeEmpresas(listaEmpresas);
		
		Repositorio.getInstance().persistirEmpresas();
		
		Repositorio.getInstance().limpiarRepo();
		//cargar ind predefinidos
	}
	
	public static void persistirActualizarEmpresasDesdeJson(String jsonEmpresas) throws FileNotFoundException {
		List<Empresa> listaEmpresas = new ArrayList<Empresa>();
		listaEmpresas = AdapterJson.transformarDeJSONaListaEmpresas(jsonEmpresas);
		Repositorio.getInstance().cargarActualizarListaDeEmpresas(listaEmpresas);
		
		Repositorio.getInstance().persistirActualizarEmpresas();
		
		Repositorio.getInstance().limpiarRepo();
		//cargar ind predefinidos
	}
	
	
	public static double probarUnIndicador2(String indicador, String empresa, Integer periodo){
		
		CalculadoraLexer lexer = new CalculadoraLexer(CharStreams.fromString(Repositorio.getInstance().buscarIndicadorPorNombre(indicador).getFormula()));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CalculadoraParser parser = new CalculadoraParser(tokens);
		CalculadoraParser.ExpresionContext expresionContext = parser.expresion();
		ParserListener listener = new ParserListener();
		
		return listener.probarUnIndicador(expresionContext,Repositorio.getInstance().buscarIndicadorPorNombre(indicador), empresa, periodo);
		
	}
public static double probarUnIndicador(String indicador, String empresa, Integer periodo){
		
		CalculadoraLexer lexer = new CalculadoraLexer(CharStreams.fromString(indicador));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CalculadoraParser parser = new CalculadoraParser(tokens);
		CalculadoraParser.ExpresionContext expresionContext = parser.expresion();
		ParserListener listener = new ParserListener();
		
		return listener.probarUnIndicadorNuevo( expresionContext, empresa, periodo);
		
	}

public static String guardarUnIndicador(String nombreIndicador, String formulaIndicador, Integer usuario){
	
	CalculadoraLexer lexer = new CalculadoraLexer(CharStreams.fromString(formulaIndicador));
	CommonTokenStream tokens = new CommonTokenStream(lexer);
	CalculadoraParser parser = new CalculadoraParser(tokens);
	CalculadoraParser.ExpresionContext expresionContext = parser.expresion();
	ParserListener listener = new ParserListener();

	Usuario user;
	try{
		user = Repositorio.getInstance().buscarUserPorId(usuario);
		listener.guardarUnIndicadorNuevo(expresionContext, nombreIndicador, formulaIndicador, user);
		return Repositorio.getInstance().persistirIndicador(nombreIndicador);
		
	}catch(Exception e){
		System.out.println("Error al intentar guardar indicador");
		return "Error al intentar guardar indicador";
	}
	
	
	
	
	}
}
