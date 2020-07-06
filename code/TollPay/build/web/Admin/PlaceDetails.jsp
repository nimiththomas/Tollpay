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
        <title>Place</title>
    </head>
    <%@include file="index.html" %>
    <body>
        <%
        // ins = "insert into tbl_state(place_name, district_id) values()"
            // update tbl_state set place_name = re, district_id = re where place_id=url

            String district = "", place_name = "";
            int a = 0;
            if (request.getParameter("btn_submit") != null) {
                String sid = request.getParameter("slct_state");
                district = request.getParameter("txt_district");
                if (request.getParameter("eid") != null) {
                    //update
                    String update = "update tbl_place set place_name='" + district + "' where place_id='" + request.getParameter("eid") + "'";
                    con.executeCommand(update);
                    response.sendRedirect("PlaceDetails.jsp");
                } else {
                    String ins = "insert into tbl_place(place_name,district_id)  values('" + district + "'," + sid + ")";
                    boolean success = con.executeCommand(ins);
                    if (success) {
                        out.println("<script>alert('Successfully inserted')</script>");
                    } else {
                        out.println("<script>alert('Failed')</script>");
                    }
                    response.sendRedirect("PlaceDetails.jsp");
                }
            }
            if (request.getParameter("del") != null) {
                String del = "delete from tbl_place where place_id='" + request.getParameter("del") + "'";
                con.executeCommand(del);
                response.sendRedirect("PlaceDetails.jsp");
            }
            String edname = "", estid = "";
            if (request.getParameter("eid") != null) {
                edname = request.getParameter("dname");
                estid = request.getParameter("sid");
            }
        %>
        <form method="post">
            <table>

                <tr>
                    <td>
                        DistrictName
                    </td>
                    <td>
                        <select name="slct_state" required="required">
                            <option value="" selected disabled>-------Select District-------</option>
                            <%
                                String sel = "select * from tbl_district";
                                ResultSet rs = con.selectCommand(sel);
                                while (rs.next()) {
                            %>
                            <option value="<%=rs.getString("district_id")%>" <% if (estid.equals(rs.getString("district_id"))) { %> selected="selected" <%}%>><%=rs.getString("district_name")%></option>

                            <%
                                }
                            %>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        PlaceName
                    </td>
                    <td>
                        <input type="text" name="txt_district" value="<%=edname%>" required="required">
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
                                <th>DistrictName</th>
                                <th>PlaceName</th>
                                <th colspan="2">Action</th>
                            </tr>
                            <%
                                String select = "select * from tbl_place d inner join tbl_district s on d.district_id=s.district_id";
                                ResultSet ra = con.selectCommand(select);
                                int i = 0;
                                while (ra.next()) {
                                    i++;
                            %>
                            <tr>
                                <td>
                                    <%= i%>
                                </td>
                                <td><%= ra.getString("district_name")%></td>
                                <td>
                                    <%= ra.getString("place_name")%>
                                </td>
                                <td>
                                    <a href="PlaceDetails.jsp?del=<%= ra.getString("place_id")%>">Delete</a>
                                </td>
                                <td>
                                    <a href="PlaceDetails.jsp?eid=<%= ra.getString("place_id")%>&sid=<%=ra.getString("district_id")%>&dname=<%= ra.getString("place_name")%>">Edit</a>
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
