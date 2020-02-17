/*
frontend/uniboSupports/coapClientToResourceModel
*/
const handle           = require('./qakeventHandler');  
const coap             = require("node-coap-client").CoapClient; 
//var coapAddr           = "coap://192.168.1.8:5683"	//RESOURCE ON RASPBERRY PI
var coapAddr             = "coap://localhost:5683"
var robotCommandResourceAddr   	 = coapAddr + "/wroom/robotCommand"
var positionResourceAddr = coapAddr + "/wroom/detectorPosition"
var mapResourceAddr = coapAddr + "/wroom/roomMap"
/*
coap
    .tryToConnect( coapAddr )
    .then((result ) => { //  true or error code or Error instance  
        console.log("coap | connection done"); // do something with the result  
    })
    ;
*/
/* -------------------------------------------
function handeData( response ){
	console.log("		coapClientToResourceModel | handeData " + response.payload);
}
*/

function observe( resourceAddr, prefix ){
console.log("		coapClientToResourceModel | createCoapClient "  );
coap
    .observe(
        resourceAddr /* string */,
        "get" /* "get" | "post" | "put" | "delete" */,
		(res) => handle.handeData(res, prefix) //handeData /* function */
        //[payload /* Buffer */,]
        //[options /* RequestOptions */]
    )
    .then(() => { console.log("		coapClientToResourceModel | observe setup " ); /* observing was successfully set up */})
    .catch(err => { console.log("		coapClientToResourceModel | observe error " + err )  /* handle error */ })
    ;
}

 
exports.setcoapAddr = function ( addr ){
	//coapAddr = "coap://"+ addr + ":5683";
	//coapResourceAddr   = coapAddr + "/robot/pos" // coap://localhost:5683/robot/pos
	coapResourceAddr = addr
	console.log("coap | coapResourceAddr=" + coapResourceAddr);
	observe( robotCommandResourceAddr, "command");
	observe( positionResourceAddr );
	observe( mapResourceAddr );
}

exports.updateData = function() {
	exports.coapGet(robotCommandResourceAddr, (res) => handle.handeData(res, "command"))
	exports.coapGet(positionResourceAddr, (res) => handle.handeData(res))
	exports.coapGet(mapResourceAddr, (res) => handle.handeData(res))
}

exports.coapGet = function ( resourceAddr, handler ){
	coap
	    .request(
			resourceAddr,
	        "get" /* "get" | "post" | "put" | "delete" */
 	        //[payload /* Buffer */,
	        //[options /* RequestOptions */]]
	    )
	    .then(response => { 			/* handle response */
			console.log("coap get done> " + response.payload );
			if (handler) {
				handler(response)
			}
		})
	    .catch(err => { /* handle error */ 
	    	console.log("coap get error> " + err );}
	    )
}//coapPut

exports.coapPut = function (  cmd, path ){ 
console.log("PUT " + coapResourceAddr + path);
	
	var resAddress = coapResourceAddr
	if (path) {
		resAddress += path
	}
	coap
	    .request(
			resAddress,     
	        "put" ,			                          // "get" | "post" | "put" | "delete"   
	        new Buffer(cmd)                          // payload Buffer 
 	        //[options]]							//  RequestOptions 
	    )
	    .then(response => { 			// handle response  
	    	console.log("coap put done> " + cmd + " " + response);} 
	     )
	    .catch(err => { // handle error  
	    	console.log("coap | put error> " + err + " for cmd=" + cmd);}
	    )
}

const myself   = require('./coapClientToResourceModel');

function test(){
 	//console.log("GET");
  	myself.coapGet();
 	//console.log("PUT");
 	// myself.coapPut("r")
 	myself.coapGet();
}

//test()
 

/*
 * ========= EXPORTS =======
 */

//module.exports = coap;
