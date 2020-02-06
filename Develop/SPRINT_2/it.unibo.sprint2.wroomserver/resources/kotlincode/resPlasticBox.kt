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


class resPlasticBox( val owner: ActorBasic, name : String) : CoapResource( name ){
 	var bottles = 0
	var NPB = 0 
	
	init{
		setObservable(true)
		println("resource $name  | created  " );		
	}
	override fun handleGET( exchange : CoapExchange ) {
		exchange.respond( "$bottles,$NPB" )  // moving=$moving" , $pos dir($direction)
	}
	override fun handlePUT( exchange : CoapExchange) {
		val msg = exchange.getRequestText()
		when( msg ){
			"0" ->  { resetBottles() }
	 		else -> {
				if (msg.contains("NPB")) {
					val NPBValue = msg.substring(4)
					setNPB(NPBValue)
				} else {
					updateBottles(msg)
				}
	 		} 
		}
		changed()	// notify all CoAp observers
 		exchange.respond(CHANGED)
	}
	
	fun updateBottles(msg:String){
		var value = msg.toIntOrNull()
		if (value != null) {
			bottles = bottles+value
		}
	}
	
	fun resetBottles(){
		bottles = 0
	}
	
	fun setNPB(NPBValue:String){
		var value = NPBValue.toIntOrNull()
		if (value != null){
			NPB = value
		}
	}
}