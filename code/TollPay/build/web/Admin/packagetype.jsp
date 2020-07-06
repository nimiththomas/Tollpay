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
            <title>PackageType</title>
        </head>
    <%@include file="index.html" %>
        <body>
        <%
            String packagename = "", packageclass = "", packageduration = "", packagetype_name = "", packagetype_class = "", packagetype_duration = "";
            if (request.getParameter("btn_submit") != null) {
                packagename = request.getParameter("txt_packagename");
                packageclass = request.getParameter("txt_packageclass");
                packageduration = request.getParameter("txt_packageduration");

                if (request.getParameter("eid") != null) {
                    //update
                    String update = "update tbl_packagetype set packagetype_name='" + packagename + "', packagetype_class='" + packageclass + "', packagetype_duration='" + packageduration + "' where packagetype_id='" + request.getParameter("eid") + "'";
                    con.executeCommand(update);
                    response.sendRedirect("packagetype.jsp");
                } else {
                    String ins = "insert into tbl_packagetype(packagetype_name,packagetype_class,packagetype_duration)  values('" + packagename + "','" + packageclass + "','" + packageduration + "')";
                    out.print(ins);
                    boolean success = con.executeCommand(ins);
                    if (success) {
                        out.println("<script>alert('Successfully inserted')</script>");
                    } else {
                        out.println("<script>alert('Failed')</script>");
                    }
                }
            }
            if (request.getParameter("del") != null) {
                String del = "delete from tbl_packagetype where packagetype_id='" + request.getParameter("del") + "'";
                con.executeCommand(del);
                response.sendRedirect("packagetype.jsp");
            }
            String epktype = "", epkname = "", epkclass = "", epkduration = "";
            if (request.getParameter("eid") != null) {
                String sel = "select * from tbl_packagetype where packagetype_id=" + request.getParameter("eid");
                ResultSet re = con.selectCommand(sel);
                if (re.next()) {

                    epkname = re.getString("packagetype_name");
                    epkclass = re.getString("packagetype_class");
                    epkduration = re.getString("packagetype_duration");
                }
            }
        %>
        <form method="post">
            <table>

                <tr>
                    <td>
                        Packagename
                    </td>
                    <td>
                        <input type="text" name="txt_packagename" value="<%=epkname%>" required="required" />
                    </td>
                </tr>
                <tr>
                    <td>
                        Packageclass
                    </td>
                    <td>
                        <input type="text" name="txt_packageclass" value="<%=epkclass%>" required="required" />
                    </td>
                </tr>
                <tr>
                    <td>
                        Package duration in days 
                    </td>
                    <td>
                        <input type="text" name="txt_packageduration" value="<%=epkduration%>" required="required"/>
                    </td>
                </tr>
                <tr>  
                    <td>

                    </td>
                    <td>
                        <input type="submit" name="btn_submit" />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <table border="1" cellspacing="0" cellpadding="5" width="300">
                            <tr>
                                <th>
                                    Sl.no
                                </th>
                                <th>
                                    Packagetypename
                                </th>
                                <th>Packagetypeclass</th>
                                <th>Packagetypeduration</th>
                                <th colspan="2">
                                    Action
                                </th>
                            </tr>
                            <%
                                String sel = "select * from tbl_packagetype";
                                ResultSet rs = con.selectCommand(sel);
                                int i = 0;
                                while (rs.next()) {
                                    i++;
                            %>
                            <tr>
                                <td>
                                    <%=i%>
                                </td>
                                <td>
                                    <%= rs.getString("packagetype_name")%>
                                </td>
                                <td>
                                    <%= rs.getString("packagetype_class")%>
                                </td>
                                <td>
                                    <%= rs.getString("packagetype_duration")%>
                                </td>
                                <td>
                                    <a href="packagetype.jsp?del=<%= rs.getString("packagetype_id")%>">Delete</a>
                                </td>
                                <td>
                                    <a href="packagetype.jsp?eid=<%= rs.getString("packagetype_id")%>">Edit</a>
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
