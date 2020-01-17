/* Generated by AN DISI Unibo */ 
package it.unibo.plasticbox

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Plasticbox ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		var Result:HashMap<Int,Int> = HashMap<Int,Int>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("plasticBox started: initializing resource value...")
						kotlincode.coapSupport.init( "coap://localhost:5683"  )
						kotlincode.coapSupport.updateResource(myself ,"wroom/plasticBox", "NPB=10" )
						kotlincode.coapSupport.readPlasticBox( "wroom/plasticBox", Result  )
						val Bottles = Result.get(1)
								  val NPB = Result.get(2)
						println("Resource correctly initialized: bottles=$Bottles, NPB=$NPB")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
					}
					 transition(edgeName="t00",targetState="doCollect",cond=whenDispatch("collect"))
				}	 
				state("doCollect") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("collect(X)"), Term.createTerm("collect(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								var Bottles = payloadArg(0)
								kotlincode.coapSupport.updateResource(myself ,"wroom/plasticBox", "$Bottles" )
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}
