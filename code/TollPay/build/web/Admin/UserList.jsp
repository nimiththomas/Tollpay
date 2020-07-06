<%-- 
    Document   : UserList
    Created on : Mar 1, 2020, 2:02:40 PM
    Author     : Justin Varghese
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="java.sql.ResultSet"%>

<jsp:useBean class="Database.ConnectionClass" id="con"></jsp:useBean>
    <!DOCTYPE html>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>UserList</title>
        </head>
        <%@include file="index.html" %>
        <body>
            <table border="1" cellspacing="0" cellpadding="5" width="400">
                <tr>
                    <th>Sl.no</th>
                    <th>UserName</th>
                    <th>Gender</th>
                    <th>DOB</th>
                    <th>Address</th>
                    <th>ContactNumber</th>
                      <th>PinCode</th>
                    <th>Email</th>
                    <th>PlaceName</th>
                    <th>DistrictName</th>
                    
                </tr>
            <%
                String select = "select * from tbl_user p inner join tbl_place t on p.place_id=t.place_id inner join tbl_district v on v.district_id=t.district_id";
                ResultSet ra1 = con.selectCommand(select);
                int i = 0;
                while (ra1.next()) {
                    i++;
            %>
            <tr>
                <td>
                    <%= i%>
                </td>
                <td><%= ra1.getString("u_name")%></td>
                <td><%= ra1.getString("u_gender")%></td>
                <td><%= ra1.getString("u_dob")%></td>
                <td>
                    <%= ra1.getString("u_address")%>
                </td>
                <td>
                    <%= ra1.getString("u_contact")%>
                </td>
                <td>
                    <%= ra1.getString("U_pin_number")%>
                </td>
                <td>
                    <%= ra1.getString("email")%>
                </td>
                <td>
                    <%= ra1.getString("place_name")%>
                </td>
                <td>
                    <%= ra1.getString("district_name")%>
                </td>

            </tr>
            <%
                }
            %>
        </table>

    </body>
</html>
