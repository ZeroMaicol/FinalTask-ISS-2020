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
import itunibo.planner.plannerUtil
import itunibo.planner.moveUtils


class resPlasticBox( val owner: ActorBasic, name : String) : CoapResource( name ){
 	var bottles = 0
	
	init{
		setObservable(true)
		println("resource $name  | created  " );		
	}
	override fun handleGET( exchange : CoapExchange ) {
		exchange.respond( "$bottles" )  // moving=$moving" , $pos dir($direction)
	}
	override fun handlePUT( exchange : CoapExchange) {
		val msg = exchange.getRequestText()
		when( msg ){
			"0" ->  { resetBottles() }
 			else -> updateBottles(msg)
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
	
}