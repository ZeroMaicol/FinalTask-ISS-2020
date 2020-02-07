package kotlincode

import org.eclipse.californium.core.CoapResource
import org.eclipse.californium.core.server.resources.CoapExchange
import org.eclipse.californium.core.coap.CoAP.ResponseCode.CHANGED;
import org.eclipse.californium.core.coap.CoAP.ResponseCode.CREATED;
import org.eclipse.californium.core.coap.CoAP.ResponseCode.DELETED;
import org.eclipse.californium.core.CoapServer
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.launch
import org.eclipse.californium.core.coap.CoAP


enum class State( ){
	EXPLORE, SUSPEND, TERMINATE, HOME
}

class resRobotCommand(name : String) : CoapResource( name ){
	var state  = State.HOME
	
	init{
		setObservable(true)
		println("resource $name  | created  " );		
	}
	override fun handleGET( exchange : CoapExchange ) {
		//println("resource $name  | GET: ${exchange.getRequestText()} pos=$pos moving=$moving" )
		exchange.respond( "$state" )  // moving=$moving" , $pos dir($direction)
	}
	override fun handlePUT( exchange : CoapExchange) {
		val prevState = state
		val msg = exchange.getRequestText()
		//println("resource $name  | PUT: $msg")
		when( msg ){
			"explore" 	->  { state = State.EXPLORE }
			"suspend" 	-> 	{ state = State.SUSPEND }
			"terminate" ->	{ state = State.TERMINATE }
			"home"		->  { state = State.HOME }
 			//else -> println("")
		}
		if (prevState != state) {
			changed()	// notify all CoAp observers
		}
		println("put: $msg")
 		exchange.respond(CHANGED)
	}

	private class UpdateTask
}
