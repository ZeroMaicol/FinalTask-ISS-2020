package kotlincode

import org.eclipse.californium.core.CoapResource
import org.eclipse.californium.core.server.resources.CoapExchange
import org.eclipse.californium.core.coap.CoAP.ResponseCode.CHANGED;
import org.eclipse.californium.core.coap.CoAP.ResponseCode.CREATED;
import org.eclipse.californium.core.coap.CoAP.ResponseCode.DELETED;
import org.eclipse.californium.core.CoapServer
import it.unibo.kactor.ActorBasic

enum class WroomState( ){
	IDLE, ALARM
}

class resWroom( val owner: ActorBasic, name : String) : CoapResource( name ){
	
	var state = WroomState.IDLE
 	
 	fun init(){
		setObservable(true)
		println("resource $name  | created  " );		
	}
	
	override fun handleGET( exchange : CoapExchange ) {
		exchange.respond("$state")
	}

	override fun handlePUT( exchange : CoapExchange) {
		val msg = exchange.getRequestText()
		when( msg ){
			"alarm" -> state = WroomState.ALARM
			else -> state = WroomState.IDLE
		}
		changed()	// notify all CoAp observers
		exchange.respond(CHANGED)
	}
}