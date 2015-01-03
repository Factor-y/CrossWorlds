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
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
	
	<!-- Optional theme -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
	
	<link rel="stylesheet" href="codemirror-4.10/lib/codemirror.css">
	<link rel="stylesheet" href="codemirror-4.10/addon/display/fullscreen.css">
	<link rel="stylesheet" href="codemirror-4.10/theme/eclipse.css">
	<link rel="stylesheet" href="css/bs-callout.css" />
	
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/images/favicon.ico" /> 

	<!-- CodeMirror -->
	<script src="codemirror-4.10/lib/codemirror.js"></script>
	<script src="codemirror-4.10/addon/display/fullscreen.js"></script>
	<script src="codemirror-4.10/addon/search/search.js"></script>
	<script src="codemirror-4.10/mode/groovy/groovy.js"></script>
	
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
	<div class="container">
		<div class="row">
			<div class="col-sm-12">
			<h1>Play with CrossWorlds</h1>
			<form method="post" action="demo/play/runscript" id="scriptform" >
				<div class="form-group">
					<label>Script body</label>
					<textarea id="scriptbody" name="scriptbody" >/**
 *
 * Type here your script using Java or Groovy Syntax
 * The full power of ODA & CrossWorlds is at your service.
 *
 */
 
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.Session;
import org.openntf.domino.Database;
import org.openntf.domino.Document;

Session s = Factory.getSession(SessionType.NATIVE);

out.println(s.getUserName());

return s.getUserName();

</textarea>
				</div>
				<div class="form-group">
					<input type="submit" id="btnRunScript" class="btn btn-submit" value="Run &gt;&gt;">
				</div>
			</form>
			
			<div class="bs-callout bs-callout-success" id="script-success">
				<h4>Success</h4>
				<p>
					Output:<br/>
					<span id="success-out" class="text-info"></span><br/>
				</p>
				<p>
					Return value:<br/>
					<span id="success-value" class="text-info"></span> 
				
				</p>
			</div>
			<div class="bs-callout bs-callout-warning" id="script-failure">
				<h4>Failure</h4>
				<p>
					Your script failed with error:
					<strong><span id="fail-error" class="text-warning" ></span></strong><br/>
					<br>
					<strong>Stack trace:</strong><br/>
					<span id="fail-stack"></span> 
				</p>
			</div>
			
			</div>
<!-- 			<div class="col-sm-3 col-sm-offset-1"> -->
<!-- 			<h2>Info</h2> -->
<!-- 			</div> -->
		</div>
	</div>
	<footer>
	</footer>
	
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<!-- Latest compiled and minified JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>

	<script>
		var cwplay = {
				
				editor : null,

				// Setup the editor
				startup : function() {

					var _self = this;

					
					$("#script-success").hide();
					$("#script-failure").hide();

					this.editor = CodeMirror.fromTextArea($("#scriptbody")[0], {
						lineNumbers: true,
						theme : 'eclipse',
						fullScreen : false,
					});
					this.editor.setSize(null, 400);
					window.scripteditor = this.editor;
					
					// Setup the submission code 
					
					$("#btnRunScript").on("click", function() {
						
						$("#btnRunScript").prop('disabled', true);
						$("#btnRunScript").val('Running >>');
						
						$.ajax('demo/play/runscript', {
							type : "POST",
							cache : false,
							// data : $("#scriptform").serialize()
							data : { scriptbody : _self.editor.getDoc().getValue() }
						}).done(function(data,status,xhr) {
							_self.showSuccess(data);
						}).fail(function(xhr) {
							console.log(xhr);
							_self.showFailure(xhr.responseJSON);
						}).always(function() {
							$("#btnRunScript").val('Run >>');
							$("#btnRunScript").prop('disabled', false);
						});
						
						return false;
					});		
					
				},
				
				showSuccess : function(data) {
					$("#success-out").html(data.out.split('\n').join('<br/>'));
					$("#success-value").html(data.value.split('\n').join('<br/>'));
					$("#script-success").show();
					$("#script-failure").hide();
				},
				
				showFailure : function(data) {
					$("#fail-error").html(data.errormessage.split('\n').join('<br/>'));
					$("#fail-stack").html(data.exceptionStack.split('\n').join('<br/>'));
					$("#script-success").hide();
					$("#script-failure").show();
				}
		}
	
		$(document).ready(function () {
	
			cwplay.startup();
			
		});
	</script>
  </body>
</html>