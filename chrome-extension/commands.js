function loadScript() {
	if (annyang) {
	console.log("Started");
	// Let's define our first command. First the text we expect, and then the function it should call
  var commands = {
    'govno': function() {
	console.log("Add item");
      document.body.innerHTML += '<span>ldlf</span>';
    },
	'test': function() {
		console.log("TEST");
	}
  };

  // Add our commands to annyang
  annyang.addCommands(commands);

  // Start listening. You can call this here, or attach this call to an event, button, etc.
  annyang.start();
	};