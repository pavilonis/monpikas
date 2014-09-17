<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.servletContext.contextPath}/rest/"/>

<html>
<head>
   <title>Monpikas</title>
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
   <link rel="shortcut icon" href="${path}resources/images/favicon.ico" type="image/x-icon"/>
   <link rel="stylesheet" type="text/css" href="${path}resources/css/style.css"/>
   <script src="${path}resources/js/cufon-yui.js" type="text/javascript"></script>
   <script src="${path}resources/js/ChunkFive_400.font.js" type="text/javascript"></script>
   <script type="text/javascript">
      Cufon.replace('h1', { textShadow: '1px 1px #fff'});
      Cufon.replace('h3', { textShadow: '1px 1px #000'});
      Cufon.replace('.back');
   </script>
   <style>
      em {
         font-style: normal;
         text-decoration: underline;
      }
   </style>
</head>
<body>
<div class="wrapper">
   <div style="text-align: center; margin-top:5%; margin-bottom: 5%">

      <h1>MONPIKAS</h1>
      <h2><em>MO</em>kinių <em>N</em>emokamų <em>PI</em>etų <em>K</em>ontrolės ir <em>A</em>pskaitos <em>S</em>istema</h2>
   </div>
   <div class="content">
      <div id="form_wrapper" class="form_wrapper">
         <form class="login active" action="${path}j_spring_security_check" method="POST">
            <h3>Prisijungimo forma</h3>

            <div>
               <label>Vartotojo vardas:</label>
               <input type="text" name="j_username"/>
            </div>
            <div>
               <label>Slaptažodis: </label>
               <input type="password" name="j_password"/>
            </div>
            <div class="bottom">
               <input type="submit" value="Jungtis" />
               <div class="clear"></div>
            </div>
         </form>
      </div>
      <div class="clear"></div>
   </div>
</div>
</body>
</html>