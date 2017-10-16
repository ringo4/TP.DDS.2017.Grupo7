package ar.edu.utn.frba.dds.tp.server;

import java.util.List;

import ar.edu.utn.frba.dds.tp.antlr.dds.*;
import ar.edu.utn.frba.dds.tp.dominio.*;
import spark.ModelAndView;
import spark.Spark;
import spark.template.handlebars.*;

public class Server {

	public static Integer idUsuarioActivo;

	public static void main(String[] args) {
		Repositorio.getInstance().cargarIndicadoresDesdeBD();

		HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();

		/***** E N D P O I N T S *******/

		Spark.get("/indicadores/:idUsuario", (req, res) -> {

			List<Indicador> indicadores = Repositorio.getInstance()
					.buscarIndicadorPorUser(Integer.parseInt(req.params("idUsuario")));

			Repositorio.getInstance().cargarListaDeIndicadores(indicadores);

			return new ModelAndView(Repositorio.getInstance(), "listaIndicadores.hbs");
		}, engine);

		Spark.get("indicador/cargar", (req, res) -> {

			return new ModelAndView(null, "cargarIndicador.hbs");
		}, engine);

		Spark.get("/login", (req, res) -> {
			return new ModelAndView(null, "login.hbs");

		}, engine);

		Spark.post("/index", (req, res) -> {
			String username = req.queryParams("login");
			String pwd = req.queryParams("password");

			// Autenticación contra la BD
			Boolean aut = validarUsuario(username, pwd);

			// TODO: Mandar nombre de usuario a Index y grabarlo en label en
			// form, a evaluar.
			if (aut) {

				Usuario u = new Usuario(1, req.queryParams("login"));
				idUsuarioActivo = u.getId();
				return new ModelAndView(u, "index.hbs");
			}

			else {
				return new ModelAndView("El usuario o la contrasena son incorrectos", "login.hbs");
			}

		}, engine);

		Spark.get("/index", (req, res) ->

		{
			return new ModelAndView(Repositorio.getInstance().buscarUserPorId(idUsuarioActivo), "index.hbs");

		}, engine);

		Spark.get("/empresas", (req, res) -> {
			return new ModelAndView(null, "empresas.hbs");
		}, engine);

		Spark.get("/empresa/consultaDeValores", (req, res) -> {

			return new ModelAndView(null, "consultaDeValores.hbs");
		}, engine);

		Spark.get("/metodologias/:idUsuario", (req, res) -> {

			// List<Metodologia> metodologias = Repositorio.getInstance()
			// .buscarMetodologiaPorUser(Integer.parseInt(req.params("idUsuario")));
			//
			// Repositorio.getInstance().cargarListaDeMetodologias(metodologias);

			return new ModelAndView(Repositorio.getInstance(), "metodologias.hbs");
		}, engine);

		Spark.get("/logout", (req, res) -> {

			return new ModelAndView(null, "login.hbs");

		});

		/***** E N D ******/

	}

	/**
	 * @param usuario
	 * @param pwd
	 * @returns true si el usuario es autenticado contra la BD.
	 */
	public static Boolean validarUsuario(String usuario, String pwd) {
		Usuario u = Repositorio.getInstance().getUsuarioByUserAndPwd(usuario, pwd);

		return (u != null);

	}

	public static void loguearUsuario(String usuario, String pass) {

	}

	/**** GETTER y SETTER ****/
	public Integer getIdUsuarioActivo() {
		return idUsuarioActivo;
	}

	public void setIdUsuarioActivo(Integer idUsuarioActivo) {
		this.idUsuarioActivo = idUsuarioActivo;
	}
}
