<!DOCTYPE html>
<html>
<head>
	<title>Action monitor</title>
	<script src="js/sockjs-0.3.4.js"></script>
	<script src="js/stomp.js"></script>
	<script type="text/javascript">
		var stompClient = null;
	
		function connect() {
			var socket = new SockJS('/action-monitor');
			stompClient = Stomp.over(socket);
			stompClient.connect({}, function(frame) {
				stompClient.subscribe('/actions', function(calResult) {
					print(calResult.body);
				});
				
				print("Connected");
			});
		}
	
		function disconnect() {
			stompClient.disconnect();
		}
	
		function print(response) {
			console.log(response);
			var actions = document.getElementById('actions');
			var div = document.createElement('div');
			div.innerHTML = response;
			actions.appendChild(div);
		}
	
		window.onbeforeunload = disconnect;
		connect();
	</script>
</head>
<body>
	<h1>Action monitor</h1>
	<div>
		<div id="actions">
		</div>
	</div>
</body>
</html>