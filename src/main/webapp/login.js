$(document).on("click", "#login", function() {
   let xmlHttp = new XMLHttpRequest();
   xmlHttp.onreadystatechange = function() {
	   if (xmlHttp.readyState == 4) {		//if response received
	        if (xmlHttp.status == 200) {	//received HTTP OK response
	        	callback(xmlHttp.responseText); //when data received call another function to handle it
	       	}
	       	else {
	       		store(xmlHttp.responseText);
	       	}
	   }
	};
    xmlHttp.open('POST', 'http://localhost:8080/Servlet2/login.jsp', true);
	let data = {
			"username": document.getElementById("username").value,
			"password" : document.getElementById("password").value
			};
	xmlHttp.send(data);
});

//when login credentials are not correct
function store(text) {
	httpGetAsync("http://localhost:8080/Servlet2/login", storeit);	//tell html to wait for response, when response received call logit function
	function storeit (series) {
		const jvar = JSON.parse(series);
		$("#wrong").text(responseJson);
	}
}

//when login credentials are correct
function callback(text) {
	httpGetAsync("http://localhost:8080/Servlet2/login", logit);	//tell html to wait for response, when response received call logit function
	function logit (series) {
		//call Stat Servlet with username and receive a HTTP OK response (happens everytime)
		//redirect to profile.html
	}
}

$(document).on("click", "#register", function() {
	window.location = "http://localhost:8080/Servlet2/register.jsp";
});