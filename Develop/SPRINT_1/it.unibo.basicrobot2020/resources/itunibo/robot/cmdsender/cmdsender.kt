package it.unibo.robot.cmdsender

import it.unibo.kactor.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch

class cmdsender( name : String, scope: CoroutineScope) : ActorBasic(name,scope){
var robot : ActorBasic? = null
	
	init{
        scope.launch{
			QakContext.createContexts("localhost", this, "basicrobot.pl", "sysRules.pl")
			robot = sysUtil.getActor("basicrobot")		//assumes a context
			doJob()
		}
    }
	
	suspend fun doJob(){
		delay(1000)
		forward("cmd", "cmd(w)", robot!! )
		delay(500)
		forward("cmd", "cmd(h)", robot!! )
	}
	
	override suspend fun actorBody(msg: ApplMessage){}
}

fun main() = runBlocking {
    println("START   ")
    //val a =
		cmdsender("cmdsender", this)   
    println("END   ")
}