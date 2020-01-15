package consolegui

import java.io.Console
import java.io.Reader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.runBlocking
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import org.eclipse.californium.core.CoapResponse

object console{
	fun start(){
		GlobalScope.launch{ work() } 
	}	
	fun work(  ){
		val url = "coap://localhost:5683/robot/pos"
		val client = CoapClient( url )
		client.setTimeout( 1000L )
  			while (true) {
				print(">")
				val t = readLine()!! 
				//if( t == 10 || t == 13 ) continue;
				//val cmd = t.toString()
				println("EXCECUTE  $t $client")
				//if( owner is ActorBasic){
					val resp : CoapResponse = client.put(t, MediaTypeRegistry.TEXT_PLAIN)
				//}
  			}		
	}
}
	
fun main() = runBlocking {
 		println("START  ")
		val job = GlobalScope.launch{ console.work() }
		job.join()
//		val v = System.`in`.read() 
 		println("BYE  ")
}
