/*
qakeventHandler.js
*/
var io  ; 	//Upgrade for socketIo;

exports.setIoSocket = function ( iosock ) {	//called by
 	io    = iosock;
	console.log("		qakeventHandler | SETIOSOCKET (to update the page) "  );
}

exports.handeData = function ( response, prefix ){
	var msgStr = "("+response.payload+")";
	if (prefix) {
		msgStr = prefix + msgStr
	}
	console.log("		qakeventHandler | handeData: " + msgStr);
	io.sockets.send( msgStr );
}
