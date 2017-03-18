package in.indiaBridal.servlets;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysql.jdbc.RandomBalanceStrategy;
import in.indiaBridal.UtilityClasses.EnDecryptor;
import in.indiaBridal.commands.DBUtility;
import in.indiaBridal.commands.SendEmailCommand;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.Random;

/**
 * Created by Karthick_S19 on 10/15/2016.
 */
public class ForgotPasswordActionCommand extends HttpServlet {
    final static Logger logger = Logger.getLogger(ForgotPasswordActionCommand.class);
    private ObjectMapper mapper =  new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("GET Forgot password");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response)  {
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json");
        PrintWriter out = null;
        ObjectNode outputNode = JsonNodeFactory.instance.objectNode();
        try {
            out = response.getWriter();

            String emailId = getMailId(req);

            hasValidEmailID(emailId);

            String generatedPassword = RandomStringUtils.randomAlphabetic(10);

            String encPwd = getEncryptedPassword(generatedPassword);

            updateNewPassword(emailId, encPwd);

            sendMailToUser(emailId, generatedPassword);

            outputNode.put("status","success");
            out.print(outputNode);

        }catch (Exception ex){
            ex.printStackTrace();
            outputNode.put("status","failure");
            outputNode.put("message",ex.getMessage());
            out.print(outputNode);
        }

    }

    private String getMailId(HttpServletRequest req) throws IOException {
        String postData = IOUtils.toString(req.getReader());
        JsonNode forgotPasswordPayload = mapper.readValue(postData, JsonNode.class);
        return forgotPasswordPayload.get("emailId").asText();
    }

    private void sendMailToUser(String emailId, String generatedPassword) {
        SendEmailCommand emailObj = new SendEmailCommand();
        String emailBody = "Hello,<br><br>"
                + "we received a request for password change for this user."
                +"Please find the new password for the IndiazBridal Account: <b>"+generatedPassword+"</b> <br><br>"
                + " Regards, <br>IndiazBridal Team";
        emailObj.sendEmail(emailId, "Password Reset of IndiazBridal Account",emailBody);
    }

    private void updateNewPassword(String emailId, String encPwd) throws SQLException {
        Connection connection = DBUtility.getConn();
        PreparedStatement updateUserPassword =  connection.prepareStatement("update authentication set password = ? where emailID = ?");
        updateUserPassword.setString(1,encPwd);
        updateUserPassword.setString(2,emailId);
        updateUserPassword.executeUpdate();
    }

    private String getEncryptedPassword(String generatedPassword) {
        EnDecryptor encObj = new EnDecryptor();
        return encObj.encryption(generatedPassword,true);
    }

    private void hasValidEmailID(String emailId) throws SQLException {
        Connection connection = DBUtility.getConn();
        PreparedStatement getUserFromAuthentication =  connection.prepareStatement("select * from authentication where emailID = ?");
        getUserFromAuthentication.setString(1,emailId);
        ResultSet getUserResult = getUserFromAuthentication.executeQuery();
        if(!getUserResult.next()){
            logger.error("The email id "+emailId+" is not registered in indiabridal");
            throw new RuntimeException("The email id "+emailId+" is not registered in indiabridal");
        }
    }
}
