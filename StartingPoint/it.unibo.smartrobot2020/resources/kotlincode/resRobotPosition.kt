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


enum class Direction( ){
	SUD, EAST, NORTH, WEST
}

class resRobotPosition( val owner: ActorBasic, name : String) : CoapResource( name ){
 	var pos        = Pair<Int,Int>(0,0)
	var direction  = Direction.SUD
	var moving     = false
	val stepTime   = 370    //370 for virtual  //600 for real
	
	init{
		setObservable(true)
		plannerUtil.initAI()
		plannerUtil.showMap() 
		println("resource $name  | created  " );		
	}
	override fun handleGET( exchange : CoapExchange ) {
		//println("resource $name  | GET: ${exchange.getRequestText()} pos=$pos moving=$moving" )
		exchange.respond( "pos$pos,dir($direction)" )  // moving=$moving" , $pos dir($direction)
	}
	override fun handlePUT( exchange : CoapExchange) {
		val msg = exchange.getRequestText()
		//println("resource $name  | PUT: $msg")
		when( msg ){
			"0" ->  { moving = false;  resetPos()       }
			"a" ->  { moving = false;  cmdToOwner("a")  }
			"d" ->  { moving = false;  cmdToOwner("d")  }
			"h" ->  { moving = false;  cmdToOwner("h")  }
			"w" ->  { moving = true;   cmdToOwner("w")  }
			"s" ->  { moving = true;   cmdToOwner("s")  }
			"l" ->  { moving = false;  cmdToOwner("l")  }
			"r" ->  { moving = false;  cmdToOwner("r")  }
			"b" ->  { moving = true;   emit("boundary","boundary(do)")  }
			"p" ->  { moving = true;   stepTheOwner( )  }
			"k" ->  { moving = true;   stopTheOwner( )  }
			"up" -> { updatePos()                       }
			"ul" -> { rotateLeft( )                     }
			"ur" -> { rotateRight()                     }
			"ustop" -> {stepStopped() }
 			//else -> println("")
		}
		changed()	// notify all CoAp observers
 		exchange.respond(CHANGED)
	}
	
	fun resetPos(){
		pos = Pair<Int,Int>(0,0)
		direction  = Direction.SUD
	}
	
	fun cmdToOwner(msg: String){
		owner.scope.launch{ MsgUtil.sendMsg("cmd","cmd($msg)",owner) }
	}
	
	fun emit(evId: String, payload : String ){
		owner.scope.launch{  owner.emit(evId,payload) }
	}
	 
	fun stepTheOwner( ){
		//val msg = MsgUtil.buildDispatch(owner.name,"step","step($stepTime)",owner.name )
		val msg = MsgUtil.buildRequest(owner.name,"onestep","onestep($stepTime)",owner.name )
		owner.scope.launch{ MsgUtil.sendMsg(msg,owner) }
		//The result of the move is given by the position of the robot
	}
	
	fun stopTheOwner(){
		val msg = MsgUtil.buildDispatch(owner.name,"stop","stop(user)",owner.name )
		owner.scope.launch{ MsgUtil.sendMsg(msg,owner) }		
	}
	
	fun updatePos( ){
		val X = pos.first.toInt()
		val Y = pos.second.toInt()
		when( direction ){
			Direction.SUD   -> pos = Pair(X,Y+1)
			Direction.EAST  -> pos = Pair(X+1,Y)
			Direction.NORTH -> pos = Pair(X,Y-1)
			Direction.WEST  -> pos = Pair(X-1,Y)
		}
		
		moveUtils.updateMapAfterAheadOk(owner); plannerUtil.showMap()
	}
	
	fun rotateLeft( ){
 		when( direction ){
			Direction.SUD    -> direction  = Direction.EAST
			Direction.EAST   -> direction =  Direction.NORTH
			Direction.NORTH  -> direction =  Direction.WEST
			Direction.WEST   -> direction =  Direction.SUD 
		}
		
		moveUtils.rotateLeft90(owner); plannerUtil.showMap()
	}
	
	fun rotateRight( ){
 		when( direction ){
			Direction.SUD    -> direction  = Direction.WEST
			Direction.EAST   -> direction =  Direction.SUD
			Direction.NORTH  -> direction =  Direction.EAST
			Direction.WEST   -> direction =  Direction.NORTH 
		}
		
		moveUtils.rotateRight90(owner); plannerUtil.showMap()
	}
	
	fun stepStopped(){
		println("resource $name  | stepStopped")
		val ev = MsgUtil.buildEvent(
		  "resource","modelContent","content(robot(state(pos(stepstopped))))" )
		emit( ev.msgId(), ev.msgContent() )
	}
	
}