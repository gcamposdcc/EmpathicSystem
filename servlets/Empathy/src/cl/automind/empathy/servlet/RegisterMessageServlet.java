package cl.automind.empathy.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.automind.empathy.controller.RegisterMessageController;
import cl.automind.empathy.model.EmpathyData;
import cl.automind.empathy.model.SagdeData;
import cl.automind.empathy.model.UserData;

/**
 * Servlet implementation class RegisterMessageServlet
 */
@WebServlet("/RegisterMessage")
public class RegisterMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public RegisterMessageServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 	?src=1&type=2&eval=true&ans=true&like=true&sec=1&eje=2&cmo=3&oc=4&niv=5&user=1&cour=2&est=3
		
		EmpathyData empathyData = new EmpathyData();
		SagdeData sagdeData = new SagdeData();
		UserData userData = new UserData();
		
		empathyData.setIdSource(
				Integer.parseInt(ifNullOrEmpty(request.getParameter("src"),"1")));
		empathyData.setIdType(
				Integer.parseInt(ifNullOrEmpty(request.getParameter("type"),"1")));
		empathyData.setEvaluated(
				Boolean.parseBoolean(ifNullOrEmpty(request.getParameter("eval"),"false")));
		empathyData.setAnswered(
				Boolean.parseBoolean(ifNullOrEmpty(request.getParameter("ans"), "false")));
		empathyData.setLiked(
				Boolean.parseBoolean(ifNullOrEmpty(request.getParameter("like"), "false")));
		
		sagdeData.setIdSector(
				Integer.parseInt(ifNullOrEmpty(request.getParameter("sec"), "1")));
		sagdeData.setIdAxis(
				Integer.parseInt(ifNullOrEmpty(request.getParameter("eje"), "1")));
		sagdeData.setIdCmo(
				Integer.parseInt(ifNullOrEmpty(request.getParameter("cmo"), "1")));
		sagdeData.setIdKo(
				Integer.parseInt(ifNullOrEmpty(request.getParameter("oc"), "1")));
		sagdeData.setIdLevel(
				Integer.parseInt(ifNullOrEmpty(request.getParameter("niv"), "1")));
		
		userData.setIdEstablishment(
				Integer.parseInt(ifNullOrEmpty(request.getParameter("user"), "1")));
		userData.setIdCourse(
				Integer.parseInt(ifNullOrEmpty(request.getParameter("cour"), "1")));
		userData.setIdUser(
				Integer.parseInt(ifNullOrEmpty(request.getParameter("est"), "1")));
		
		RegisterMessageController controller = new RegisterMessageController();
		controller.doTask(empathyData, userData, sagdeData);
		
	}
	private boolean isNullOrEmpty(String s){
		if (s == null) return true;
		return s.length() == 0;
	}
	private String ifNullOrEmpty(String s, String replace){
		if (isNullOrEmpty(s)){
			return replace;
		}
		return s;
	}
}
