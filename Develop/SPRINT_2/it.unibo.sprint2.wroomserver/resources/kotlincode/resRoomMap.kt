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


class resRoomMap( val owner: ActorBasic, name : String) : CoapResource( name ){
 	var roomMap:String = ""
	
	init{
		setObservable(true)
		println("resource $name  | created  " );		
	}
	override fun handleGET( exchange : CoapExchange ) {
		exchange.respond( "map($roomMap)")  // moving=$moving" , $pos dir($direction)
	}
	override fun handlePUT( exchange : CoapExchange) {
		val msg = exchange.getRequestText()
		when( msg ){
			"0" ->  { resetMap() }
	 		else -> {
				roomMap = msg
	 		} 
		}
		changed()	// notify all CoAp observers
 		exchange.respond(CHANGED)
		println("-----------------------------------------")
		println("RESOURCE ROOM MAP UPDATED WITH:")
		println(roomMap)
		println("-----------------------------------------")
	}
	
	fun resetMap(){
		roomMap = ""
	}

}