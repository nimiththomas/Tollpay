<%-- 
    Document   : state.jsp
    Created on : Feb 28, 2020, 12:07:31 PM
    Author     : Justin Varghese
--%>

<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean class="Database.ConnectionClass" id="con"></jsp:useBean>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>District</title>
    </head>
    <%@include file="index.html" %>
    <body>
    <%
        String state="", State_name="";
        if(request.getParameter("btn_submit")!=null){
            state = request.getParameter("txt_state");
            if(request.getParameter("eid")!=null){
                //update
                String update = "update tbl_district set district_name='"+state+"' where district_id='"+request.getParameter("eid")+"'";
                con.executeCommand(update);
                response.sendRedirect("DistrictDetails.jsp");
            }
            else{
                String ins = "insert into tbl_district(district_name)  values('"+state+"')";
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
            String del = "delete from tbl_district where district_id='"+request.getParameter("del")+"'";
            con.executeCommand(del);
            response.sendRedirect("DistrictDetails.jsp");
        }
        if(request.getParameter("eid")!=null){
            String sel = "select * from tbl_district where district_id='"+request.getParameter("eid")+"'";
            ResultSet re = con.selectCommand(sel);
            if(re.next()){
                State_name = re.getString("district_name");
            }
        }
        %>
        <table>
            <form method="post">
                <tr>
                    <td>
                        DistrictName
                    </td>
                    <td>
                        <input type="text" name="txt_state" value="<%= State_name %>" required="required">
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
                    DistrictName
                </th>
                <th colspan="2">
                    Action
                </th>
            </tr>
            <%
                String sel = "select * from tbl_district";
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
                     <%= rs.getString("district_name") %>
                 </td>
                 <td>
                     <a href="DistrictDetails.jsp?del=<%= rs.getString("district_id") %>">Delete</a>
                 </td>
                 <td>
                     <a href="DistrictDetails.jsp?eid=<%= rs.getString("district_id") %>">Edit</a>
                 </td>
             </tr>
             <%
                }
            %>
        </table>
    </body>
</html>
