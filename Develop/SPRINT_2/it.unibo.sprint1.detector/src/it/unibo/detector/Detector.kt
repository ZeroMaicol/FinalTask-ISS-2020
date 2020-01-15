/* Generated by AN DISI Unibo */ 
package it.unibo.detector

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Detector ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 
		var IsBottle     = false
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("detector starting...")
						delay(2000) 
						println("detector started!")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
					}
					 transition(edgeName="t00",targetState="doExplore",cond=whenRequest("explore"))
				}	 
				state("doExplore") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						answer("explore", "exploreAck", "exploreAck(OK)"   )  
						forward("cmd", "cmd(w)" ,"basicrobot" ) 
					}
					 transition(edgeName="t11",targetState="checkObstacle",cond=whenEvent("virtualobstacle"))
				}	 
				state("checkObstacle") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("virtualobstacle(OBJNAME)"), Term.createTerm("virtualobstacle(OBJNAME)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  
											  IsBottle = payloadArg(0).startsWith("bottle")
								if(IsBottle){ forward("cmd", "cmd(${payloadArg(0)})" ,"basicrobot" ) 
								delay(100) 
								forward("cmd", "cmd(h)" ,"basicrobot" ) 
								forward("updateBottle", "updateBottle(1)" ,"detectorbox" ) 
								println("Bottle removed!")
								 }
								else
								 { forward("cmd", "cmd(h)" ,"basicrobot" ) 
								 println("Stop for safety, obstacle not bottle!")
								  }
						}
						IsBottle = false
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}
