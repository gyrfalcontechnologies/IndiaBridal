package in.indiaBridal.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import in.indiaBridal.UtilityClasses.GenerateFailureJSONResponse;
import in.indiaBridal.commands.PerformActionCommand;

/**
 * Servlet implementation class UserActionCommand
 */
public class UserActionCommand extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(UserActionCommand.class); 
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserActionCommand()
	{
		super();
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PerformActionCommand perfActionObj = new PerformActionCommand();
		logger.debug("request object == "+request.getParameterNames());
		String requestJSON = (String) request.getParameter("requestJSON");
		logger.debug("request received from FRONT END. REQUEST JSON = "+requestJSON);
		JSONArray retJsonObj = null;
		if(requestJSON!=null)
		{
			retJsonObj = perfActionObj.performAction(requestJSON);
			response.setContentType("text/html");
			logger.debug("RESPONSE SENT TO FRONT END. RESPONSE JSON = "+retJsonObj);
			response.getWriter().println(retJsonObj.toString());
		}
		else
		{
			GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
			retJsonObj = genFailJSONObj.generateFailedProfileCreation("INVALID JSON REQUEST");
			logger.debug("RESPONSE SENT TO FRONT END. RESPONSE JSON = "+retJsonObj);
			response.getWriter().println(retJsonObj.toString());
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PerformActionCommand perfActionObj = new PerformActionCommand();
		logger.debug("request objcet data == "+request.getParameter("requestJSON"));
		logger.debug("request objcet key == "+request.getParameter("memberID"));
		String requestJSON = (String) request.getParameter("requestJSON");
		logger.debug("request received from FRONT END. REQUEST JSON = "+requestJSON);
		JSONArray retJsonObj = null;
		if(requestJSON!=null)
		{
			retJsonObj = perfActionObj.performAction(requestJSON);
			response.setContentType("text/html");
			logger.debug("RESPONSE SENT TO FRONT END. RESPONSE JSON = "+retJsonObj);
			response.getWriter().println(retJsonObj.toString());
		}
		else
		{
			GenerateFailureJSONResponse genFailJSONObj = new GenerateFailureJSONResponse();
			retJsonObj = genFailJSONObj.generateFailedProfileCreation("INVALID JSON REQUEST");
			logger.debug("RESPONSE SENT TO FRONT END. RESPONSE JSON = "+retJsonObj);
			response.getWriter().println(retJsonObj.toString());
		}
	}
}
