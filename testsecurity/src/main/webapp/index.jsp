<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>viewmodel</title>
    <script src="https://lib.sinaapp.com/js/jquery/2.0.2/jquery-2.0.2.min.js">
    </script>
</head>
<body>
<h1>hello ${message}</h1>
<a href="http://localhost:8080/user/test">跳转链接</a>
<form class="addStu" id="addStu">
    <table>
        <tr>
            <td>姓名：</td>
            <td><input type="text"  name="userName"></td>
        </tr>
        <tr>
            <td>密码：</td>
            <td><input type="text"  name="password"></td>
        </tr>
        <tr>
            <td>
                <input type="button" id="btn" value="提交">
            </td>

            <td><input type="reset"></td>
        </tr>
    </table>
</form>
</body>
</html>
<script>
    var persons = {userName:"admin", password: "1234"};
    $(function () {
        $("#btn").on("click",function () {
            $.ajax({
                type:"Post",
                url:"http://localhost:8080/user/login",
                // 序列化form表单里面的数据传到后台
                data:JSON.stringify(persons),
                // 指定后台传过来的数据是json格式
                dataType:"json",
                contentType:"application/json;charset=utf-8",
                success:function (data) {
                    console.log(data)
                    if (data.code==200){
                        alert("登录成功，token = " + data.data.token);
                    }else{
                        //失败弹窗提示
                        alert("登录失败");
                    }
                },
                error:function (err) {
                    alert("添加失败2");
                }
            })
        })
    })
</script>

