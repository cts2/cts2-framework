<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- Free web templates by http://www.templatesdock.com; released for free under a Creative Commons Attribution 3.0 License -->
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta http-equiv="content-language" content="en" />
	<link rel="stylesheet" media="screen,projection" type="text/css" href="resources/home/css/main.css" />
	<link rel="stylesheet" media="screen,projection" type="text/css" href="resources/home/css/nivo.css" />
	<link rel="stylesheet" media="screen,projection" type="text/css" href="resources/home/css/skin.css" />	
	<!--[if IE 6]><script src="javascript/pngfix.js"></script><script>DD_belatedPNG.fix('#logo img, #nav li a, #nav li a span, #nav-icons img, #twitter, #imac');</script><![endif]--> 			
	<script type="text/javascript" src="resources/home/javascript/jquery.js"></script>
	<script type="text/javascript" src="resources/home/javascript/jquery.nivo.js"></script>
	<script type="text/javascript" src="resources/home/javascript/cufon-yui.js"></script>
	<script type="text/javascript" src="resources/home/javascript/font.font.js"></script>
	<script type="text/javascript">
	Cufon.replace('h1, h2', {hover:true});
	$(window).load(function() {
		$('#slider').nivoSlider({
		effect:'sliceDown',
		slices:20,
		animSpeed:500,
		pauseTime:3000,
		startSlide:0,
		directionNav:false,
		directionNavHide:false,
		controlNav:false,
		controlNavThumbs:false,
		controlNavThumbsFromRel:false,
		keyboardNav:false,
		pauseOnHover:true,
		manualAdvance:false,
		captionOpacity:0.5,
		beforeChange:function(){},
		afterChange:function(){},
		slideshowEnd:function(){}
		});
	});
	</script>
	<title>CTS2 Development Framework</title>
</head>

<body id="hp"> <!-- APPLY CSS STYLES FOR HOMEPAGE -->

<div id="main">

	<!-- HEADER -->
	<div id="header" class="box">

		<p id="logo"><a href="./" title="[Go to homepage]"></a></p>

		<img src="resources/images/dflogo-small.png" alt="" />

		<!-- NAVIGATION -->
		<ul id="nav">
			<li class="current"><a href="http://informatics.mayo.edu/cts2/framework"><span>WebSite</span></a></li>
			<li><a href="system/console"><span>Admin Console</span></a></li>
			<li><a href="service"><span>Services</span></a></li>
			<li><a href="http://informatics.mayo.edu/cts2"><span>CTS2</span></a></li>
			<li><a href="http://informatics.mayo.edu/cts2/framework/forum"><span>Get Help</span></a></li>
			
			
		</ul>		
		
		<!-- SOCIAL ICONS -->
		<p id="nav-icons"><a href="https://twitter.com/#!/cts2framework"><img src="resources/home/design/nav-twitter.png" alt="" /></a></p>
		
	</div> <!-- /header -->

	<!-- WELCOME -->
	<div id="welcome" class="box">

		<div id="welcome-inner">

			<h2>Welcome to the CTS2 Development Framework!</h2>

			<p>To find out more about this service, visit the 
				<a href="service">service</a> page. This will give you an indication of what is available, as well as how to 
				use this service. For Administrators of this service, visit the <a href="system/console"><span>admin console</span></a>
				to add or remove a plugin. For more informatation about what makes up CTS2, visit the <a href="#content"><span>links below</span></a>.
			</p>
		
		</div> <!-- /welcome-inner -->

		<!-- TWITTER -->
		<div id="twitter" class="box">
		
			<ul>
				<li>Follow us on Twitter! &ndash; <a href="https://twitter.com/#!/cts2framework">cts2framework</a></li>
			</ul>

		</div> <!-- /twitter -->
		
		<div id="imac">
		
			<!-- REPLACE THESE 3 IMAGES WITH YOUR OWN IMAGES (WIDTH:371, HEIGHT:211) -->
			<div id="slider" class="nivoSlider">
				<img src="resources/home/images/cts2slide.png" alt="" />
				<img src="resources/home/images/dflogo.png" alt="" />
				<img src="resources/home/images/omg.jpg" alt="" />	
			</div> <!-- /slider -->
			
		</div> <!-- /imac -->
		
	</div> <!-- /welcome -->
	
	<!-- COLUMNS -->
	<div id="section" class="box">

		<!-- CONTENT -->
		<div id="content">

			<h2 class="nom">Learn More about CTS2</h2>		
			
			<ul class="products">
				<li class="box">
					<div class="products-img"><img src="resources/home/images/cts2.png" alt="" /></div>
					<!-- Photo: (c) Nic McPhee -->
					<div class="products-desc">
						<h3><a href="http://informatics.mayo.edu/cts2">CTS2</a></h3>
						<p>Common Terminology Services 2 (CTS2) is an Object Management Group (OMG) specification for representing, accessing and disseminating terminological content.
						Learn more about CTS2, the specification, and what it can do for you.</p>
					</div> <!-- /products-desc -->
				</li>
				<li class="box">
					<div class="products-img"><img src="resources/home/images/dflogo.png" alt="" /></div>
					<div class="products-desc">
						<h3><a href="http://informatics.mayo.edu/cts2/framework">CTS2 Development Framework</a></h3>
						<p>The CTS2 Development Framework is a development kit for rapidly creating CTS2 compliant applications. The Development Framework allows for users to create plugins which may be loaded into the 
						Development Framework to provide REST and SOAP Web Services that use CTS2 compliant paths and model objects.</p>
					</div> <!-- /products-desc -->
				</li>
				<li class="last box">
					<div class="products-img"><img src="resources/home/images/omg.jpg" alt="" /></div>
					<div class="products-desc">
						<h3><a href="http://www.omg.org/spec/CTS2/1.0/Beta1/">Official OMG© Specification</a></h3>
						<p>CTS2 is an Official Object Management Group© (OMG) Specification. Go here to see the specification, which includes Platfrom Independent UML® Models, as well as Platform Specific bindings
						to REST and SOAP implementations.</p>
					</div> <!-- /products-desc -->
				</li>
			</ul>
		
		</div> <!-- /content -->

		<!-- SIDEBAR -->
		<div id="aside">

			<h2 class="nom">Where to go from here?</h2>
			
			<ul class="articles">
				<li>
					<h3><a href="system/console">Admin Console</a></h3>
					<p>The Admin Console is where the CTS2 Development Framework may be configured. Go here to add new plugins and extensions,
					activate/deactivate components. Note that this is an administrative page and will require a login.</p>
				</li>
				<li>
					<h3><a href="service">Learn more about this service</a></h3>
					<p>Learn more about this CTS2 Implementation, what it provides, and what parts of CTS2 it implements.</p>
				</li>
				<li class="last">
					<h3><a href="http://informatics.mayo.edu/cts2/framework">Join the Community</a></h3>
					<p>Interact with users and developers of the CTS2 Development Framework, get help, or take a tutorial.</p>
				</li>
			</ul>
		
		</div> <!-- /aside -->

	</div> <!-- /section -->

	<!-- CONTACT -->
	<div id="contact">

		<address>
			CTS2 Development Framework <span>|</span>
			<a href="http://informatics.mayo.edu/cts2/framework">http://informatics.mayo.edu/cts2/framework</a> <span>|</span>
			<a href="mailto:cts2framework@informatics.mayo.edu">cts2framework@informatics.mayo.edu</a> <span>|</span>
		</address>		

	</div> <!-- /contact -->	
	
	<!-- FOOTER -->
	<div id="footer" class="box">

		<!-- Would you like to remove this line? Visit www.templatesdock.com/terms.php -->
		<p class="f-right t-right"><a href="http://www.templatesdock.com/">Free web templates</a> by TemplatesDock<br /><span class="smaller">Tip: <a href="http://www.nuvio.cz/">Webdesign</a></span></p>
		<!-- Would you like to remove this line? Visit www.templatesdock.com/terms.php -->
	
		<p class="f-left">Copyright &copy;&nbsp;2011 <a href="#">Bestfolio</a>, All Rights Reserved &reg;</p>

	</div> <!-- /footer -->

</div> <!-- /main -->

<script type="text/javascript"> Cufon.now(); </script>

</body>
</html>
