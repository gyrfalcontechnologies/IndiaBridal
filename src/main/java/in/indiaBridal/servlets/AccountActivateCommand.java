package in.indiaBridal.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import in.indiaBridal.commands.DBUtility;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Karthick_S19 on 10/16/2016.
 */
public class AccountActivateCommand extends HttpServlet {
    final static Logger logger = Logger.getLogger(AccountActivateCommand.class);
    private ObjectMapper mapper =  new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) {
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json");
        PrintWriter out = null;
        ObjectNode outputNode = JsonNodeFactory.instance.objectNode();
        try{
            out = response.getWriter();

            String accountActivateToken = getActivateToken(req);

            String memberId=getMemberId(accountActivateToken);

            activateUserAccount(memberId);

            outputNode.put("status","success");
            out.print(outputNode);

        }catch (Exception ex){
            ex.printStackTrace();
            outputNode.put("status","failure");
            outputNode.put("message",ex.getMessage());
            out.print(outputNode);
        }
    }

    private void activateUserAccount(String memberId) throws SQLException {
        Connection connection = DBUtility.getConn();
        PreparedStatement updateUserPassword =  connection.prepareStatement("update authentication set userActive = ?, activateId=? where MemberID = ?");
        updateUserPassword.setString(1,"YES");
        updateUserPassword.setString(2,"");
        updateUserPassword.setString(3,memberId);
        updateUserPassword.executeUpdate();
    }

    private String getMemberId(String accountActivateToken) throws SQLException {
        Connection connection = DBUtility.getConn();
        PreparedStatement getUserUsingActivateToken =  connection.prepareStatement("select * from authentication where userActive = ? and activateId = ?");
        getUserUsingActivateToken.setString(1,"NO");
        getUserUsingActivateToken.setString(2,accountActivateToken);
        ResultSet getUserResult = getUserUsingActivateToken.executeQuery();
        if(!getUserResult.next()){
            logger.error("The account activation token is invalid. Please contact indiabridal administrator");
            throw new RuntimeException("The account activation token is invalid. Please contact indiabridal administrator");
        }
        return getUserResult.getString("MemberID");
    }

    private String getActivateToken(HttpServletRequest req) throws IOException {
        String postData = IOUtils.toString(req.getReader());
        JsonNode accountActivatePayload = mapper.readValue(postData, JsonNode.class);
        return accountActivatePayload.get("accountActivateToken").asText();
    }
}
