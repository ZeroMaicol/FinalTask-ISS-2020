package kotlincode

import org.eclipse.californium.core.CoapClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import kotlinx.coroutines.delay
import it.unibo.kactor.MqttUtils
import it.unibo.kactor.MsgUtil

class handler: CoapHandler{
var mqtt   : MqttUtils
	
	init{
		mqtt = MqttUtils()
		val b = mqtt.connect("coaphandler", "tcp://localhost:1883")
		println("RESOURCE OBSERVER | connected mqtt $b"  )
	}
	
	override fun onLoad( response : CoapResponse ) {
		val content = response.getResponseText()
		println("RESOURCE OBSERVER HANDLER | value=" + content)  //content=pos(1, 0),dir(SUD)
	}
	override fun onError(){ println("RESOURCE OBSERVER HANDLER | FAILED ")} 
}

object resourceObserver{
lateinit var client : CoapClient
lateinit var path   : String
			
	fun init( host: String, mypath : String ){  //"coap://localhost:5683/robot/pos"
		path    = mypath
		val url = "$host/$path"
		client =  CoapClient( url )

		observe( )
		GlobalScope.launch{
 			System.`in`.read()
 		}
 	} 
	
	fun observe( ){
		println("RESOURCE OBSERVER | STARTS path=$path"  )
		//GlobalScope.launch{
			client.observe( handler() )
			//System.`in`.read()
 		//}
	}
}

fun main(){
	resourceObserver.init("coap://localhost:5683", "robot/pos")
	System.`in`.read()
}