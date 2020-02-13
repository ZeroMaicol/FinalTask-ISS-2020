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
		
		val StepDuration = 400L
		var IsBottle     = false
		var radius = 0
		var exploreX = 0
		var exploreY = 0
		var radiusFinished = false
		var hasPlannedMoves = false
		var exploringHorizontal = true
		var CurrentCommand = ""
		var goToHome = false
		var unexploredPosition: Pair<Int,Int>? = null
		var Result:HashMap<Int,Int> = HashMap<Int,Int>()
		var emptyTheDetectorBox = false
		var robotWasExploring = false;
		var beforeEmptyPos_x = -1
		var beforeEmptyPos_y = -1
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("detector starting...")
						delay(2000) 
						println("detector started!")
						kotlincode.coapSupport.observeCommands(myself)
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						println("")
						println("")
						println("")
						println("work::: CurrentMap is:")
						goToHome = false
						itunibo.planner.plannerUtil.showMap(  )
					}
					 transition(edgeName="t00",targetState="startExplore",cond=whenDispatch("explore"))
					transition(edgeName="t01",targetState="goHome",cond=whenDispatch("suspend"))
					transition(edgeName="t02",targetState="executeEmptyTheDetectorBox",cond=whenDispatch("terminate"))
				}	 
				state("startExplore") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						itunibo.planner.plannerUtil.initAI(  )
						radius = 0
					}
					 transition( edgeName="goto",targetState="exploreNextRadius", cond=doswitch() )
				}	 
				state("gotoUnexplored") { //this:State
					action { //it:State
						println("gotoUnexplored")
							radiusFinished = true
									unexploredPosition = itunibo.planner.plannerUtil.getFirstNonExploredPosition()
						if(unexploredPosition != null && goToHome){ itunibo.planner.plannerUtil.setGoal( "${unexploredPosition!!.first}", "${unexploredPosition!!.second}"  )
						itunibo.planner.plannerUtil.doPlan(  )
						 }
						else
						 { goToHome = true
						  }
					}
					 transition( edgeName="goto",targetState="doPlannedMoves", cond=doswitchGuarded({(unexploredPosition != null)}) )
					transition( edgeName="goto",targetState="executeEmptyTheDetectorBox", cond=doswitchGuarded({! (unexploredPosition != null)}) )
				}	 
				state("executeEmptyTheDetectorBox") { //this:State
					action { //it:State
						goToHome = false
						if( checkMsgContent( Term.createTerm("terminate(X)"), Term.createTerm("terminate(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								goToHome = true
						}
						kotlincode.coapSupport.readDetectorBox( "wroom/detectorBox", Result  )
						val BottlesInDetector = Result.get(1)
						if(BottlesInDetector!!.compareTo(0) == 0){ goToHome = true
						 }
						if(BottlesInDetector!!.compareTo(0) > 0){ kotlincode.coapSupport.readPlasticBox( "wroom/plasticBox", Result  )
						val bottlesInPlasticBox = Result.get(1) !!
									  val NPB = Result.get(2)
									  val totalBottles = bottlesInPlasticBox.plus(BottlesInDetector !!)
						if(compareValues(NPB, totalBottles) >= 0){ beforeEmptyPos_x = 
						itunibo.planner.plannerUtil.getPosX(  )
						beforeEmptyPos_y = 
						itunibo.planner.plannerUtil.getPosY(  )
						goToHome = true
										  emptyTheDetectorBox = true
										  robotWasExploring = true
						forward("detectorMustEmpty", "detectorMustEmpty(OK)" ,"detector" ) 
						 }
						else
						 {  }
						 }
					}
					 transition( edgeName="goto",targetState="goHome", cond=doswitchGuarded({goToHome}) )
					transition( edgeName="goto",targetState="work", cond=doswitchGuarded({! goToHome}) )
				}	 
				state("goHome") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.setGoal( "0", "0"  )
						itunibo.planner.plannerUtil.doPlan(  )
					}
					 transition( edgeName="goto",targetState="executeHomeMove", cond=doswitch() )
				}	 
				state("executeHomeMove") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
							var PosX = 
						itunibo.planner.plannerUtil.getPosX(  )
							var PosY = 
						itunibo.planner.plannerUtil.getPosY(  )
							var Dir = 
						itunibo.planner.plannerUtil.getDirection(  )
						kotlincode.coapSupport.updateDetectorPosition( "($PosX, $PosY)", "$Dir", "Moving"  )
						var Map = 
						itunibo.planner.plannerUtil.getMap(  )
						kotlincode.coapSupport.updateResource(myself ,"wroom/roomMap", "$Map" )
						if((itunibo.planner.plannerUtil.isRobotHome())){ if(emptyTheDetectorBox){ forward("detectorMustEmpty", "detectorMustEmpty(OK)" ,"detector" ) 
						 }
						else
						 { forward("backHome", "backHome(OK)" ,"detector" ) 
						  }
						 }
						else
						 { delay(500) 
						 var CmdPayload = itunibo.planner.plannerUtil.getNextPlannedMove()
						 itunibo.planner.plannerUtil.doMove( CmdPayload  )
						 if(CmdPayload == "w"){ request("onestep", "onestep($StepDuration)" ,"steprobot" )  
						  }
						 if(CmdPayload == "a"){ forward("cmd", "cmd(l)" ,"basicrobot" ) 
						 forward("moveOk", "moveOk(OK)" ,"detector" ) 
						  }
						 if(CmdPayload == "d"){ forward("cmd", "cmd(r)" ,"basicrobot" ) 
						 forward("moveOk", "moveOk(OK)" ,"detector" ) 
						  }
						  }
					}
					 transition(edgeName="t13",targetState="work",cond=whenDispatch("backHome"))
					transition(edgeName="t14",targetState="executeHomeMove",cond=whenDispatch("moveOk"))
					transition(edgeName="t15",targetState="executeHomeMove",cond=whenReply("stepdone"))
					transition(edgeName="t16",targetState="executeBottlesTransfer",cond=whenDispatch("detectorMustEmpty"))
				}	 
				state("executeBottlesTransfer") { //this:State
					action { //it:State
						println("executeEmptyDetectorBox")
						kotlincode.coapSupport.readDetectorBox( "wroom/detectorBox", Result  )
						val BottlesInDetector = Result.get(1) !!
						kotlincode.coapSupport.readPlasticBox( "wroom/plasticBox", Result  )
						val bottlesInPlasticBox = Result.get(1) !!
								  val NPB = Result.get(2)
								  val totalBottles = bottlesInPlasticBox.plus(BottlesInDetector !!)
						if(compareValues(NPB, totalBottles) >= 0){ forward("emptyBottleResource", "emptyBottleResource(0)" ,"detectorbox" ) 
						forward("collect", "collect($BottlesInDetector)" ,"plasticbox" ) 
						emptyTheDetectorBox = false
						goToHome = false
						if(robotWasExploring){ robotWasExploring = false
						itunibo.planner.plannerUtil.setGoal( "$beforeEmptyPos_x", "$beforeEmptyPos_y"  )
						itunibo.planner.plannerUtil.doPlan(  )
						 }
						 }
					}
					 transition( edgeName="goto",targetState="doPlannedMoves", cond=doswitch() )
				}	 
				state("exploreNextRadius") { //this:State
					action { //it:State
						println("")
						println("------------")
						println("exploreNextRadius: $radius")
						
									radius = radius + 1
									radiusFinished = false
									exploreY = radius
									exploreX = 0
									exploringHorizontal = true
					}
					 transition( edgeName="goto",targetState="doExplore", cond=doswitchGuarded({(!itunibo.planner.plannerUtil.isFullyExplored() && !goToHome)}) )
					transition( edgeName="goto",targetState="gotoUnexplored", cond=doswitchGuarded({! (!itunibo.planner.plannerUtil.isFullyExplored() && !goToHome)}) )
				}	 
				state("doExplore") { //this:State
					action { //it:State
						if (exploreY < 0) { radiusFinished = true }
						if((!radiusFinished)){ println("")
						println("doExplore: currentMap:")
						itunibo.planner.plannerUtil.showMap(  )
						var Map = 
						itunibo.planner.plannerUtil.getMap(  )
						kotlincode.coapSupport.updateResource(myself ,"wroom/roomMap", "$Map" )
						println("doExplore goal: ($exploreX,$exploreY)")
						itunibo.planner.plannerUtil.setGoal( "$exploreX", "$exploreY"  )
						itunibo.planner.plannerUtil.doPlan(  )
						
										if (exploringHorizontal) {
											if (exploreX < radius) { exploreX = exploreX + 1 }
											else { 
												exploringHorizontal = false 
												exploreY = exploreY - 1
											}
										} else {
											exploreY = exploreY - 1
										}
						 }
					}
					 transition( edgeName="goto",targetState="doPlannedMoves", cond=doswitchGuarded({(!radiusFinished && !itunibo.planner.plannerUtil.isFullyExplored())}) )
					transition( edgeName="goto",targetState="exploreNextRadius", cond=doswitchGuarded({! (!radiusFinished && !itunibo.planner.plannerUtil.isFullyExplored())}) )
				}	 
				state("doPlannedMoves") { //this:State
					action { //it:State
							var PosX = 
						itunibo.planner.plannerUtil.getPosX(  )
							var PosY = 
						itunibo.planner.plannerUtil.getPosY(  )
							var Dir = 
						itunibo.planner.plannerUtil.getDirection(  )
						kotlincode.coapSupport.updateDetectorPosition( "($PosX, $PosY)", "$Dir", "Moving"  )
						var Map = 
						itunibo.planner.plannerUtil.getMap(  )
						kotlincode.coapSupport.updateResource(myself ,"wroom/roomMap", "$Map" )
					}
					 transition( edgeName="goto",targetState="doMove", cond=doswitchGuarded({(itunibo.planner.plannerUtil.hasPlannedMoves())}) )
					transition( edgeName="goto",targetState="doExplore", cond=doswitchGuarded({! (itunibo.planner.plannerUtil.hasPlannedMoves())}) )
				}	 
				state("doMove") { //this:State
					action { //it:State
						delay(500) 
						var CmdPayload = itunibo.planner.plannerUtil.getNextPlannedMove()
						println("-----")
						println("doMove: move is $CmdPayload")
						println("-----")
						itunibo.planner.plannerUtil.doMove( CmdPayload  )
						if(CmdPayload == "w"){ request("onestep", "onestep($StepDuration)" ,"steprobot" )  
						 }
						if(CmdPayload == "a"){ forward("cmd", "cmd(l)" ,"basicrobot" ) 
						forward("moveOk", "moveOk(OK)" ,"detector" ) 
						 }
						if(CmdPayload == "d"){ forward("cmd", "cmd(r)" ,"basicrobot" ) 
						forward("moveOk", "moveOk(OK)" ,"detector" ) 
						 }
					}
					 transition(edgeName="t17",targetState="doPlannedMoves",cond=whenDispatch("moveOk"))
					transition(edgeName="t18",targetState="doPlannedMoves",cond=whenReply("stepdone"))
					transition(edgeName="t19",targetState="onStepFail",cond=whenReply("stepfail"))
				}	 
				state("onStepFail") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("stepfail(DURATION,CAUSE)"), Term.createTerm("stepfail(DURATION,CAUSE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								IsBottle = payloadArg(1).startsWith("bottle")
								if(IsBottle){ val StepTimeLeft = StepDuration - payloadArg(0).toLong()
								forward("cmd", "cmd(${payloadArg(1)})" ,"basicrobot" ) 
								delay(100) 
								forward("cmd", "cmd(h)" ,"basicrobot" ) 
								forward("updateBottle", "updateBottle(1)" ,"detectorbox" ) 
								request("onestep", "onestep($StepTimeLeft)" ,"steprobot" )  
								println("onStepFail: bottle, step of duration $StepTimeLeft")
								 }
								else
								 { forward("cmd", "cmd(h)" ,"basicrobot" ) 
								 itunibo.planner.plannerUtil.setRobotPositionAsObstacle(  )
								 itunibo.planner.plannerUtil.stepBack(  )
								 println("onStepFail: stepbackwards of duration ${payloadArg(0)}")
								 request("onestepbackwards", "onestepbackwards(${payloadArg(0)})" ,"steprobot" )  
								  }
						}
					}
					 transition(edgeName="t110",targetState="afterStepFail",cond=whenReply("stepdone"))
				}	 
				state("afterStepFail") { //this:State
					action { //it:State
						if(IsBottle){ kotlincode.coapSupport.readDetectorBox( "wroom/detectorBox", Result  )
						val BottlesInDetector = Result.get(1) !!
									  val NDB = Result.get(2)
						if((compareValues(NDB, BottlesInDetector) == 0)){ kotlincode.coapSupport.readPlasticBox( "wroom/plasticBox", Result  )
						val bottlesInPlasticBox = Result.get(1) !!
										  val NPB = Result.get(2)
										  val totalBottles = bottlesInPlasticBox.plus(BottlesInDetector !!)
						if(compareValues(NPB, totalBottles) >= 0){ beforeEmptyPos_x = 
						itunibo.planner.plannerUtil.getPosX(  )
						beforeEmptyPos_y = 
						itunibo.planner.plannerUtil.getPosY(  )
						goToHome = true
											  emptyTheDetectorBox = true
											  robotWasExploring = true
						forward("detectorMustEmpty", "detectorMustEmpty(OK)" ,"detector" ) 
						 }
						else
						 { forward("detectorCantEmpty", "detectorCantEmpt(detectorCantEmpty)" ,"detector" ) 
						  }
						 }
						else
						 { forward("moveOk", "moveOk(OK)" ,"detector" ) 
						  }
						 }
						else
						 { forward("obstacleFound", "obstacleFound(X)" ,"detector" ) 
						  }
					}
					 transition(edgeName="t111",targetState="doPlannedMoves",cond=whenDispatch("moveOk"))
					transition(edgeName="t112",targetState="doExplore",cond=whenDispatch("obstacleFound"))
					transition(edgeName="t113",targetState="goHome",cond=whenDispatch("detectorMustEmpty"))
					transition(edgeName="t114",targetState="work",cond=whenDispatch("detectorCantEmpty"))
				}	 
			}
		}
}