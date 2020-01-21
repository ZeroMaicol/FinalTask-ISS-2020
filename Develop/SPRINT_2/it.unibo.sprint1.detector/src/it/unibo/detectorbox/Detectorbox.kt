/* Generated by AN DISI Unibo */ 
package it.unibo.detectorbox

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Detectorbox ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		var Result:HashMap<Int,Int> = HashMap<Int,Int>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("detectorBox started: initializing resource value...")
						kotlincode.coapSupport.init( "coap://localhost:5683"  )
						kotlincode.coapSupport.updateResource(myself ,"wroom/detectorBox", "NDB=5" )
						kotlincode.coapSupport.readDetectorBox( "wroom/detectorBox", Result  )
						val Bottles = Result.get(1)
								  val NDB = Result.get(2)
						println("Resource correctly initialized: bottles=$Bottles, NDB=$NDB")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
					}
					 transition(edgeName="t02",targetState="updateBottleResource",cond=whenDispatch("updateBottle"))
				}	 
				state("updateBottleResource") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("updateBottle(X)"), Term.createTerm("updateBottle(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								kotlincode.coapSupport.updateResource(myself ,"wroom/detectorBox", "1" )
						}
					}
					 transition( edgeName="goto",targetState="tryThrowBottle", cond=doswitch() )
				}	 
				state("tryThrowBottle") { //this:State
					action { //it:State
						println("detector ask to the plasticbox the permission to throw the bottle...")
						request("tryPutBottle", "tryPutBottle(1)" ,"plasticbox" )  
					}
					 transition(edgeName="t03",targetState="receptionAllowed",cond=whenReply("receptionAllowed"))
					transition(edgeName="t04",targetState="plasticBoxIsFull",cond=whenReply("plasticBoxFull"))
				}	 
				state("receptionAllowed") { //this:State
					action { //it:State
						println("detector throws the bottle into the plasticBox")
						kotlincode.coapSupport.readDetectorBox( "wroom/detectorBox", Result  )
						val bottles = Result.get(1)
						forward("collect", "collect(1)" ,"plasticbox" ) 
						kotlincode.coapSupport.updateResource(myself ,"wroom/detectorBox", "0" )
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("plasticBoxIsFull") { //this:State
					action { //it:State
						println("detector has found the plasticBox full of bottles, ew what to do ...")
						if( checkMsgContent( Term.createTerm("plasticBoxFull(X)"), Term.createTerm("plasticBoxFull(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}
