var socket = new WebSocket("ws://localhost:8080/SocketSendToken/actions");
socket.onmessage = onMessage;

function onMessage(event) {
	var response = JSON.parse(event.data);
	if (response.action === "initSession") {
		console.log(response);
	} else if (response.action === "sendMessageToType") {
		console.log(response);
	}
}

function initSession(){
	var inputToken = document.getElementById("inputToken");
	var inputType = document.getElementById("inputType");
	var Message = {
	        action: "initSession",
	        token: inputToken.value,
	        type: inputType.value
	};
	socket.send(JSON.stringify(Message));
}

function sendMessageToType(){
	var input2Message = document.getElementById("input2Message");
	var input2Type = document.getElementById("input2Type");
	var Message = {
	        action: "sendMessageToType",
	        message: input2Message.value,
	        type: input2Type.value
	};
	socket.send(JSON.stringify(Message));
}

function broadcast(){
	var Message = {
	        action: "broadcast",
	        message: "test",
	        type: "all"
	};
	socket.send(JSON.stringify(Message));
}