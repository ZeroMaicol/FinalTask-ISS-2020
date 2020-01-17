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


enum class State( ){
	EXPLORE, SUSPEND
}

class resDetectorState( val owner: ActorBasic, name : String) : CoapResource( name ){
	var state  = State.SUSPEND
	
	init{
		setObservable(true)
		println("resource $name  | created  " );		
	}
	override fun handleGET( exchange : CoapExchange ) {
		//println("resource $name  | GET: ${exchange.getRequestText()} pos=$pos moving=$moving" )
		exchange.respond( "$state" )  // moving=$moving" , $pos dir($direction)
	}
	override fun handlePUT( exchange : CoapExchange) {
		val msg = exchange.getRequestText()
		//println("resource $name  | PUT: $msg")
		when( msg ){
			"e" ->  { state = State.EXPLORE;  sendExplore("explore")}
 			//else -> println("")
		}
		changed()	// notify all CoAp observers
 		exchange.respond(CHANGED)
	}
	
	fun sendExplore(msg: String){
		owner.scope.launch{ MsgUtil.sendMsg("cmd","cmd($msg)",owner) }
	}
	
}