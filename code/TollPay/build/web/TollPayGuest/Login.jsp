<%-- 
    Document   : Login
    Created on : 12 Jan, 2020, 10:33:53 AM
    Author     : zaya
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean class="Database.ConnectionClass" id="con_obj"></jsp:useBean>
<%@page import="java.sql.ResultSet"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login page</title>
    </head>
    <body>
        <%@include file="header.jsp" %>
        <%
            if (request.getParameter("Login") != null) {
                String email = "";
                String password = "";
                boolean b = false;

                email = request.getParameter("txtemail");
                password = request.getParameter("txtpassword");

                String sel = "select * from  tbl_user where email='" + email + "' and u_password='" + password + "'";
                ResultSet rs = con_obj.selectCommand(sel);
                int i = 0;
                if (rs.next()) {

                    session.setAttribute("user_id", rs.getString("user_id"));
                    session.setAttribute("user_name", rs.getString("u_name"));
                    response.sendRedirect("../User/HomePage.jsp");

                }

                String s = "select * from  tbl_admin where admin_username='" + email + "' and admin_password='" + password + "'";
                ResultSet rs1 = con_obj.selectCommand(s);
                if (rs1.next()) {

                    session.setAttribute("user_id", rs1.getString("admin_id"));
                    session.setAttribute("user_name", rs1.getString("admin_username"));
                    response.sendRedirect("../Admin/HomePage.jsp");

                }
            }
        %>

        <form method="post" name="frmlogin">
            <div id="tab" align="center">
                <h2>Login</h2>
                <table width="428" border="0" cellpadding="3" align="center">

                    <table>
                        <tr>
                            <td>UserName</td>

                            <td> <input type="text"name="txtemail"id="txtemail"></td>   

                        </tr>
                        <td>Password</td> 
                        <td>
                            <input type="password" name="txtpassword" id="password"></td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center">
                            <td colspan="2" align="center"><input type="submit" name="Login" id="Login" value="Login" /></td>
                            </td>
                        </tr>
                    </table>
            </div>
        </form>

    </body>
</html>
<%@include file="footer.jsp" %>