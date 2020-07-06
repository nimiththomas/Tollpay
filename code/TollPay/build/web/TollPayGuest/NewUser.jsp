<%-- 
    Document   : NewUser
    Created on : 12 Jan, 2020, 10:32:30 AM
    Author     : zaya
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean class="Database.ConnectionClass" id="con_obj"></jsp:useBean>
<%@page import="java.sql.ResultSet"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>New User</title>
    </head>
    <body>
        <%@include file="header.jsp" %>
        <%

            String usname = "", plname = "", ucontact = "", uemail = "", upassword = "", udob = "",
                    upin = "", uaddress = "", ugender = "";

            boolean b = false;

            if (request.getParameter("btnsubmit") != null) {
                usname = request.getParameter("txtuname");
                plname = request.getParameter("lstplace");
                ucontact = request.getParameter("txtcontact");
                uemail = request.getParameter("txtemail");
                upassword = request.getParameter("txtpassword");
                udob = request.getParameter("txtdob");
                upin = request.getParameter("txtpinnumber");
                uaddress = request.getParameter("txtaddress");
                ugender = request.getParameter("txtgender");

                String ins = "insert into  tbl_user(u_name,u_gender,u_dob,u_address,u_contact,email,u_pin_number,place_id,u_password)values('" + usname + "','" + ugender + "' ,'" + udob + "' ,'" + uaddress + "','" + ucontact + "','" + uemail + "','" + upin + "','" + plname + "','" + upassword + "')";
                b = con_obj.executeCommand("insert into  tbl_user(u_name,u_gender,u_dob,u_address,u_contact,email,u_pin_number,place_id,u_password)values('" + usname + "','" + ugender + "' ,'" + udob + "' ,'" + uaddress + "','" + ucontact + "','" + uemail + "','" + upin + "','" + plname + "','" + upassword + "')");
                if (b == true) {
                    out.print("save");

                } else {

                    out.print(ins);
                    out.print("error");
                }

            }


        %>
        <form method="post"action=""name="frmnewuser">
            <table align="center">
                <tr>
                    <td>Name</td>

                    <td> <input type="text"name="txtuname"id="txtuname"value="" ></td>   

                </tr>
                <tr>
                    <td>Gender</td>
                    <td> <input type="radio"name="txtgender"id="txtgender"value="male">Male
                        <input type="radio"name="txtgender"id="txtgender"value="female">Female</td>
                </tr>
                <tr>
                    <td>DOB</td>

                    <td> <input type="date"name="txtdob"id="txtdob"value="" ></td>   

                </tr>
                <tr>
                    <td>Address</td>

                    <td> <textarea name="txtaddress"id="txtaddress"value="" ></textarea></td>   

                </tr>
                <tr>
                    <td>Contact</td>

                    <td> <input type="textarea"name="txtcontact"id="txtcontact"value="" ></td>   

                </tr>
                <tr>
                    <td>EmailID</td>

                    <td> <input type="text"name="txtemail"id="txtemail"value="" ></td>   

                </tr>
                <tr>
                    <td>PinCode</td>

                    <td> <input type="text"name="txtpinnumber"id="txtpinnumber"value="" ></td>   

                </tr>
                <tr>
                    <td>PlaceName</td>

                    <td>
                        <select name="lstplace" required="required">
                            <option value="" selected disabled>-------Select Place-------</option>
                            <%                                String sel = "select * from tbl_place";
                                ResultSet rs = con_obj.selectCommand(sel);
                                while (rs.next()) {
                            %>
                            <option value="<%=rs.getString("place_id")%>"><%=rs.getString("place_name")%></option>

                            <%
                                }
                            %>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td>Password</td>

                    <td> <input type="password"name="txtpassword"id="txtpassword"value="" ></td>   

                </tr>

                <td colspan="2" align="center">
                    <input type="submit" name="btnsubmit" id="btnsubmit" value="save">
                </td> 
                </tr>



            </table>
        </form>
    </body>
</html>
<%@include file="footer.jsp" %>