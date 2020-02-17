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


class resDetectorBox( val owner: ActorBasic, name : String) : CoapResource( name ){
 	var bottles = 0
	var NDB = 0
	
	init{
		setObservable(true)
		println("resource $name  | created  " );		
	}
	override fun handleGET( exchange : CoapExchange ) {
		exchange.respond( "$bottles,$NDB" )  // moving=$moving" , $pos dir($direction)
	}
	override fun handlePUT( exchange : CoapExchange) {
		val msg = exchange.getRequestText()
		when( msg ){
			"0" ->  { resetBottles() }
	 		else -> {
				if (msg.contains("NDB")) {
					val NDBValue = msg.substring(4)
					setNDB(NDBValue)
				} else {
					updateBottles()
				}
	 		} 
		}
		changed()	// notify all CoAp observers
 		exchange.respond(CHANGED)
	}
	
	fun updateBottles(){
		bottles = bottles+1
	}
	
	fun resetBottles(){
		bottles = 0
	}
	
	fun setNDB(NDBValue:String){
		var value = NDBValue.toIntOrNull()
		if (value != null){
			NDB = value
		}
	}
	
}