<%@page import="org.openntf.domino.utils.Factory.SessionType"%>
<%@page import="org.openntf.domino.utils.Factory"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>CrossWorlds Demo</title>

	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="webjars/bootstrap/3.3.2/css/bootstrap.min.css">
	
	<!-- Optional theme -->
	<link rel="stylesheet" href="webjars/bootstrap/3.3.2/css/bootstrap-theme.min.css">
	
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/images/favicon.ico" /> 
  </head>
  <body>
  <nav class="navbar navbar-inverse">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="<%= request.getContextPath() %>">CrossWorlds for <strong>Domino</strong></a>
        </div>
        <ul class="nav navbar-nav navbar-right">
       		<li><p class="navbar-text">Running as: <%= Factory.getSession(SessionType.NATIVE).getCommonUserName() %></p></li>
       	</ul>
      </div>
    </nav>

    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron">
      <div class="container">
        <h1>Hello, <strong>CrossWorlds!</strong></h1>
        <p>This application shows you the possibilities that the <a href="http://www.openntf.org/main.nsf/project.xsp?r=project/OpenNTF%20Domino%20API"  target="_blank"><strong>OpenNTF Domino API</strong></a> and <a href="https://developer.ibm.com/wasdev/" target="_blank"><strong>WAS Liberty</strong></a> bring to Domino developers</p>
<!--         <p><a class="btn btn-primary btn-lg" href="#" role="button">Learn more &raquo;</a></p> -->
      </div>
    </div>

    <div class="container">
      <!-- Example row of columns -->
      <div class="row">
        <div class="col-md-4">
          <h2>Why</h2>
          <p>XPages brought IBM Domino up to date for web development. XPages are intended to bring Domino Applications.</p>
          <p>But what happens if we're looking to use the power of domino for other kind of applications ?</p>
          <p>XPages just is not the right tool, we felt <strong>J2EE 6.0+ with Domino</strong> could be a wedding made in heaven. <strong>CrossWorlds</strong> is just about this</p>
          <p><a class="btn btn-default" href="#" role="button">Why CrossWorlds &raquo;</a></p>
        </div>
        <div class="col-md-4">
          <h2>What</h2>
          <p>IBM has been including for a long time a license of WebSphere in all Domino licenses.</p>
          <p>We took advantage of this to create an easy-to-setup, easy-to-use application server exploiting IBM WebSphere Application Server Liberty on top of IBM Domino</p>
          <p><strong>CrossWorlds</strong> is a <strong>lightweight</strong>, <strong>modern</strong>, <strong>extensible</strong> <strong>J2EE application server</strong> embedding the full power of Domino.</p>
          <p>Thanks to <a title="OpenNTF Domino API" href="http://www.openntf.org/main.nsf/project.xsp?r=project/OpenNTF%20Domino%20API" >ODA</a> we've <strong>Domino native performance <i>inside</i> a full J2EE stack</strong>. The best of both worlds.</p>
          <p><a class="btn btn-default" href="#" role="button">What's CrossWorlds &raquo;</a></p>
       </div>
        <div class="col-md-4">
          <h2>Play</h2>
          <p>This sample application is built on CrossWorlds to show you how this works for you.</p>
          <p>Use the embedded code editor to write code using the full domino API and get the results fast</p>
          <p>This app will evolve to become a full API / Explorer and eventually a Web IDE</p>
          <p>Our mission is to bring Domino developers and Java developers to easily exploit the advantages of J2EE and Domino together.</p>  
          <p><a class="btn btn-success" href="play.jsp" role="button">Play with CrossWorlds &raquo;</a></p>
        </div>
      </div>

      <hr>

      <footer>
        <p>&copy; TBD/Factor-y/OpenNTF 2014</p>
      </footer>
    </div> <!-- /container -->
	
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="webjars/jquery/1.11.2/jquery.min.js"></script>
	<!-- Latest compiled and minified JavaScript -->
	<script src="webjars/bootstrap/3.3.2/js/bootstrap.min.js"></script>
  </body>
</html>