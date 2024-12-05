package org.iesvdm.jsp_servlet_jdbc.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesvdm.jsp_servlet_jdbc.dao.SocioDAO;
import org.iesvdm.jsp_servlet_jdbc.dao.SocioDAOImpl;
import org.iesvdm.jsp_servlet_jdbc.model.Socio;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

//PLANTILLA DE CÓDIGO PARA SERVLETs EN INTELLIJ
//https://www.jetbrains.com/help/idea/creating-and-configuring-web-application-elements.html

//1A APROX. PATRÓN MVC -> M(dao, model y bbdd), V(jsp) & C(servlet)

//                      v--NOMBRE DEL SERVLET           v--RUTAS QUE ATIENDE, PUEDE SER UN ARRAY {"/GrabarSociosServlet", "/grabar-socio"}
@WebServlet(name = "EditarSociosServlet", value = "/EditarSociosServlet")
public class EditarSociosServlet extends HttpServlet {

    //EL SERVLET TIENE INSTANCIADO EL DAO PARA ACCESO A BBDD A LA TABLA SOCIO
    //                                  |
    //                                  V
    private SocioDAO socioDAO = new SocioDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/formularioEditarSocio.jsp");
        Optional<Socio> socio = socioDAO.find(Integer.parseInt(request.getParameter("codigo")));
        if (socio.isPresent()) {
            request.setAttribute("socio", socio.get());
        } else {
            response.sendRedirect("ListarSociosServlet?err-cod=2");
        }

        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher dispatcher = null;

        Integer id = null;
        String nombre = null;
        Integer estatura = null;
        Integer edad = null;
        String localidad = null;

        try {
            id = Integer.parseInt(request.getParameter("id"));
            nombre = request.getParameter("nombre");
            estatura = Integer.parseInt(request.getParameter("estatura"));
            edad = Integer.parseInt(request.getParameter("edad"));
            localidad = request.getParameter("localidad");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (id != null) {
            socioDAO.update(new Socio(id, nombre, estatura, edad, localidad));
            // response.sendRedirect("ListarSociosServlet");
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/listadoSociosB.jsp");
            List<Socio> listado = this.socioDAO.getAll();
            request.setAttribute("listado", listado);
            dispatcher.forward(request,response);
        } else {
            response.sendRedirect("ListarSociosServlet?err-cod=1");
        }


    }

}