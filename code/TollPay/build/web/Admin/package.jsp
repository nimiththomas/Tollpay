<%-- 
    Document   : district
    Created on : Feb 28, 2020, 3:57:26 PM
    Author     : Justin Varghese
--%>

<%@page import="java.sql.ResultSet"%>
<jsp:useBean class="Database.ConnectionClass" id="con"></jsp:useBean>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Package</title>
    </head>
    <%@include file="index.html" %>
    <body>
        <%

            String district = "", district_name = "";

            if (request.getParameter("btn_submit") != null) {
                String sid = request.getParameter("slct_state");
                district = request.getParameter("txt_district");

                String ins = "insert into tbl_package(packagetype_id,vehicletype_id,package_rate)values('" + request.getParameter("slct_packagetype") + "'," + request.getParameter("slct_state") + ",'" + request.getParameter("txt_packagerate") + "')";
                boolean success = con.executeCommand(ins);
                if (success) {
                    out.println("<script>alert('Successfully inserted')</script>");
                } else {
                    out.println("<script>alert('Failed')</script>");
                }
                response.sendRedirect("package.jsp");
            }

            if (request.getParameter("del") != null) {
                String del = "delete from tbl_package where package_id='" + request.getParameter("del") + "'";
                con.executeCommand(del);
                response.sendRedirect("package.jsp");
            }

        %>
        <form method="post">
            <table>

                <tr>
                    <td>
                        PackageType
                    </td>
                    <td>
                        <select name="slct_packagetype" required="required">
                            <option value="" selected disabled>-------Select Package Type-------</option>
                            <%                                String sel = "select * from tbl_packagetype";
                                ResultSet rs = con.selectCommand(sel);
                                while (rs.next()) {
                            %>
                            <option value="<%=rs.getString("packagetype_id")%>"><%=rs.getString("packagetype_name")%></option>

                            <%
                                }
                            %>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        VehicleType
                    </td>
                    <td>
                        <select name="slct_state" required="required">
                            <option value="" selected disabled>-------Select VechicleType-------</option>
                            <%
                                String sele = "select * from tbl_vehicletype";
                                ResultSet ra = con.selectCommand(sele);
                                while (ra.next()) {
                            %>
                            <option value="<%=ra.getString("vehicletype_id")%>"><%=ra.getString("vehicletype_name")%></option>

                            <%
                                }
                            %>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        Package Rate
                    </td>
                    <td>
                        <input type="text" name="txt_packagerate"  required="required">
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td colspan="2">
                        <input type="submit" name="btn_submit">
                    </td>
                </tr>                    
                <tr>
                    <td colspan="2">

                        <table border="1" cellspacing="0" cellpadding="5" width="400">
                            <tr>
                                <th>Sl.no</th>
                                <th>PackageType</th>
                                <th>PackageClass</th>
                                <th>PackageDuration</th>
                                <th>VechicleType</th>
                                <th>Rate</th>
                                <th colspan="2">Action</th>
                            </tr>
                            <%
                                String select = "select * from tbl_package p inner join tbl_packagetype t on p.packagetype_id=t.packagetype_id inner join tbl_vehicletype v on v.vehicletype_id=p.vehicletype_id";
                                ResultSet ra1 = con.selectCommand(select);
                                int i = 0;
                                while (ra1.next()) {
                                    i++;
                            %>
                            <tr>
                                <td>
                                    <%= i%>
                                </td>
                                <td><%= ra1.getString("packagetype_name")%></td>
                                <td><%= ra1.getString("packagetype_class")%></td>
                                <td><%= ra1.getString("packagetype_duration")%></td>
                                <td>
                                    <%= ra1.getString("vehicletype_name")%>
                                </td>
                                <td>
                                    <%= ra1.getString("package_rate")%>
                                </td>
                                <td>
                                    <a href="package.jsp?del=<%= ra1.getString("package_id")%>">Delete</a>
                                </td>

                            </tr>
                            <%
                                }
                            %>
                        </table>
                    </td>
                </tr>
            </table>

        </form>


    </body>
</html>

