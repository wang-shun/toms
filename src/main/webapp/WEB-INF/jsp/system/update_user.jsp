<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/5/22
  Time: 9:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<html>
<head>
    <title>修改员工基本信息</title>
    <script src='<%=basePath%>/assets/js/jquery-2.0.3.min.js'></script>
</head>
<body>
<div class="page-content">
    <div class="page-header">
        <h1>
            修改信息
        </h1>
    </div>
    <!-- /.page-header -->

    <div class="row">
        <div class="col-xs-12">
            <!-- PAGE CONTENT BEGINS -->

            <form class="form-horizontal" action="<c:url value="/user/update_user"/>" method="post" role="form">
                <input type="hidden" name="id" value="${data.id}"/>

                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 登录账号 </label>

                    <div class="col-sm-9">
                        <input type="text" id="form-field-1" name="loginName" readonly="readonly"
                               value="${data.loginName}" placeholder="登录账号" class="col-xs-10 col-sm-5 login-name"/>
            <span class="help-inline col-xs-12 col-sm-7">

											</span>

                    </div>
                </div>

                <div class="space-4"></div>

                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right " for="form-field-2"> 密 码 </label>

                    <div class="col-sm-9">
                        <input type="password" name="password" id="form-field-2" placeholder="密码"
                               class="col-xs-10 col-sm-5 ace password"/>
                        <span class="help-password col-xs-12 col-sm-7">

											</span>
                    </div>
                </div>

                <div class="space-4"></div>

                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="form-input-readonly"> 联系电话 </label>

                    <div class="col-sm-9">
                        <input type="text" class="col-xs-10 col-sm-5 ace tel" id="form-input-readonly" name="telephone"
                               value="${data.telephone}"/>
                        <span class="help-tel col-xs-12 col-sm-7">

											</span>
                    </div>
                </div>

                <div class="space-4"></div>

                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="form-field-4">姓 名</label>

                    <div class="col-sm-9">
                        <input class="col-xs-10 col-sm-5 ace user-name" type="text" name="userName"
                               value="${data.userName}"
                               id="form-field-4" placeholder="真实姓名"/>
                        <span class="help-name col-xs-12 col-sm-7">

											</span>

                        <div class="space-2"></div>

                        <div class="help-block" id="input-size-slider"></div>
                    </div>
                </div>
                <div class="space-4"></div>

                <div class="clearfix form-actions">
                    <div class="col-md-offset-3 col-md-9">
                        <button class="btn btn-info" type="submit">
                            <i class="icon-ok bigger-110"></i>
                            Submit
                        </button>

                        &nbsp; &nbsp; &nbsp;
                        <button class="btn" type="reset">
                            <i class="icon-undo bigger-110"></i>
                            Reset
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    jQuery(function ($) {
        $('.login-name').on('blur', function () {
            var value = $(this).val();
            var name = $(this).attr('name');
            var url = "<c:url value="/user/check_user.json"/>"
            $.ajax({
                url: url,
                type: 'post',
                dataType: 'json',
                data: name + '=' + value + '&id=${data.id}',
                success: function (data) {
                    var span = '<span class="middle" name="middle" disabled="false" style="color: red">登录名已经存在</span>';
                    if (!data.status) {
                        $('.help-inline').append(span);
                    } else {
                        $('.help-inline .middle').remove();
                    }
                },
                error: function () {
                    //do same thing!
                }
            });
        });
        var span = '<span class="middle" name="middle" disabled="false" style="color: red">此项必填</span>';
        $('.btn-info').on('click', function () {
            $('.help-password .middle').remove();
            $('.help-tel .middle').remove();
            $('.help-name .middle').remove();
            if ($('.password').val() == null || $('.password').val() == '') {
                $('.help-password').append(span);
                return false;
            }
            if ($('.tel').val() == null || $('.tel').val() == '') {
                $('.help-tel').append(span);
                return false;
            }
            if ($('.user-name').val() == null || $('.user-name').val() == '') {
                $('.help-name').append(span);
                return false;
            }
//        return false;
        });
    });
</script>
</body>
</html>
