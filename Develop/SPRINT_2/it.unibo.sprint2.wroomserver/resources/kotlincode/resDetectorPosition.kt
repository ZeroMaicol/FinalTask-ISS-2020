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

class resDetectorPosition( val owner: ActorBasic, name : String) : CoapResource( name ){
 	var pos        = "(0,0)"
	var direction  = "SUD"
	var moving     = "false"
	
	init{
		setObservable(true)
		println("resource $name  | created  " );		
	}
	override fun handleGET( exchange : CoapExchange ) {
		//println("resource $name  | GET: ${exchange.getRequestText()} pos=$pos moving=$moving" )
		exchange.respond( "pos$pos,dir($direction),moving$moving" )  // moving=$moving" , $pos dir($direction)
	}
	override fun handlePUT( exchange : CoapExchange) {
		val msg = exchange.getRequestText()
		//println("resource $name  | PUT: $msg")
		when( msg ){
			"0" -> resetDetector()
 			else -> {
				var splitted = msg.split(".")
				pos = splitted[0]
				direction = splitted[1]
				moving = splitted[2]
			}
		}
		changed()	// notify all CoAp observers
 		exchange.respond(CHANGED)
	}
	
	fun resetDetector(){
		pos = "(0,0)"
		direction = "SUD"
		moving = "false"
	}
}