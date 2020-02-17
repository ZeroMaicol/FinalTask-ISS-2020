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
			var NDB:Int = 0
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("detectorBox started: initializing resource value...")
						kotlincode.coapSupport.init( "coap://localhost:5683"  )
						NDB = detector.detectorSupport.NDB
						kotlincode.coapSupport.updateResource(myself ,"wroom/detectorBox", "NDB=$NDB" )
						kotlincode.coapSupport.readDetectorBox( "wroom/detectorBox", Result  )
						val Bottles = Result.get(1)
								  val ndb = Result.get(2)
						println("Resource correctly initialized: bottles=$Bottles, NDB=$ndb")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
					}
					 transition(edgeName="t025",targetState="updateBottleResource",cond=whenDispatch("updateBottle"))
					transition(edgeName="t026",targetState="emptyBottleResource",cond=whenDispatch("emptyBottleResource"))
				}	 
				state("updateBottleResource") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("updateBottle(X)"), Term.createTerm("updateBottle(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								kotlincode.coapSupport.updateResource(myself ,"wroom/detectorBox", "1" )
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("emptyBottleResource") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("emptyBottleResource(X)"), Term.createTerm("emptyBottleResource(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								kotlincode.coapSupport.updateResource(myself ,"wroom/detectorBox", "0" )
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}