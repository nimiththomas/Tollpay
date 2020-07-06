<%-- 
    Document   : vehicletype
    Created on : Feb 29, 2020, 12:24:45 PM
    Author     : Justin Varghese
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="java.sql.ResultSet"%>

<jsp:useBean class="Database.ConnectionClass" id="con"></jsp:useBean>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>VehicleType</title>
    </head>
<%@include file="index.html" %>
    <body>
        <%
        String vehicletype="", vehicletype_name="";
        if(request.getParameter("btn_submit")!=null){
            vehicletype = request.getParameter("txt_vehicletype");
            if(request.getParameter("eid")!=null){
                //update
                String update = "update tbl_vehicletype set vehicletype_name='"+vehicletype+"' where vehicletype_id='"+request.getParameter("eid")+"'";
                con.executeCommand(update);
                response.sendRedirect("vehicletype.jsp");
            }
            else{
                String ins = "insert into tbl_vehicletype(vehicletype_name)  values('"+vehicletype+"')";
                boolean success = con.executeCommand(ins);
                if(success){
                    out.println("<script>alert('Successfully inserted')</script>");
                }
                else{
                    out.println("<script>alert('Failed')</script>");
                }
            }
        }
        if(request.getParameter("del")!=null){
            String del = "delete from tbl_vehicletype where vehicletype_id='"+request.getParameter("del")+"'";
            con.executeCommand(del);
            response.sendRedirect("vehicletype.jsp");
        }
        if(request.getParameter("eid")!=null){
            String sel = "select * from tbl_vehicletype where vehicletype_id='"+request.getParameter("eid")+"'";
            ResultSet re = con.selectCommand(sel);
            if(re.next()){
                vehicletype_name = re.getString("vehicletype_name");
            }
        }
        %>
        <table>
            <form method="post">
                <tr>
                    <td>
                        Vehicletype
                    </td>
                    <td>
                        <input type="text" name="txt_vehicletype" value="<%= vehicletype_name %>" required="required">
                    </td>
                </tr>
                <tr>
                    <td>
                        
                    </td>
                    <td>
                        <input type="submit" name="btn_submit">
                    </td>
                </tr>
            </form>
        </table>
        
        
        <table border="1" cellspacing="0" cellpadding="5" width="300">
            <tr>
                <th>
                    Sl.no
                </th>
                <th>
                    Vehicletype
                </th>
                <th colspan="2">
                    Action
                </th>
            </tr>
            <%
                String sel = "select * from tbl_vehicletype";
                ResultSet rs = con.selectCommand(sel);
                int i = 0;
                while(rs.next()){
                    i++;
             %>
             <tr>
                 <td>
                     <%= i %>
                 </td>
                 <td>
                     <%= rs.getString("vehicletype_name") %>
                 </td>
                 <td>
                     <a href="vehicletype.jsp?del=<%= rs.getString("vehicletype_id") %>">Delete</a>
                 </td>
                 <td>
                     <a href="vehicletype.jsp?eid=<%= rs.getString("vehicletype_id") %>">Edit</a>
                 </td>
             </tr>
             <%
                }
            %>
        </table>
    </body>
</html>
