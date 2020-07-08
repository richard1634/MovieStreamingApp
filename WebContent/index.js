/*
 * CS 122B Project 4. Autocomplete Example.
 * 
 */
var list = {};
function handleLookup(query, doneCallback) {
	if (Object.keys(list).length == 0){
		console.log("autocomplete initiated")
		console.log("Ajax Being Used")
		
		// TODO: if you want to check past query results first, you can do it here
		
		// sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
		// with the query data
		jQuery.ajax({
			"method": "GET",
			// generate the request url from the query.
			// escape the query string to avoid errors caused by special characters 
			"url": "hero-suggestion?query=" + escape(query),
			"success": function(data) {
				// pass the data, query, and doneCallback function into the success handler
				handleLookupAjaxSuccess(data, query, doneCallback) 
			},
			"error": function(errorData) {
				console.log("lookup ajax error")
				console.log(errorData)
			}
		})
	}
	else{
		if (query in list){
			console.log("Cache is being used")
			console.log(list[query]);
			doneCallback( { suggestions: list[query] });
		}
	}
}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 * 
 * data is the JSON data string you get from your Java Servlet
 * 
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful")
	
	// parse the string into JSON
	var jsonData = JSON.parse(data);
	console.log(jsonData)
	list[query] = jsonData;
	// TODO: if you want to cache the result into a global variable you can do it here

	// call the callback function provided by the autocomplete library
	// add "{suggestions: jsonData}" to satisfy the library response format according to
	//   the "Response Format" section in documentation
	doneCallback( { suggestions: jsonData } );
	console.log(jsonData);

}

function handleSelectSuggestion(suggestion) {
	// TODO: jump to the specific result page based on the selected suggestion
	
	console.log("you select " + suggestion["value"] + " with ID " + suggestion["data"]["heroID"])
	window.location.replace("/your-project-name/SingleMovie?id=" + suggestion["data"]["heroID"]);

}



$('#autocomplete').autocomplete({
	minLength:0,
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback);
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    // set delay time
    deferRequestBy: 300,
    minChars:3
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as minimum characters
});


/*
 * do normal full text search if no suggestion is selected 
 */
function handleNormalSearch(query) {
	console.log("doing normal search with query: " + query);
	// TODO: you should do normal search here
	window.location.replace("/your-project-name/SearchResultsServlet?title=" + query + "&display=50&sort=rating");
}


// bind pressing enter key to a handler function
//$('#autocomplete2').keypress(function(event) {
//	// keyCode 13 is the enter key
//	// pass the value of the input box to the handler function
//	handleNormalSearch($('#autocomplete2').val());
//})

// TODO: if you have a "search" button, you may want to bind the onClick event as well of that button


