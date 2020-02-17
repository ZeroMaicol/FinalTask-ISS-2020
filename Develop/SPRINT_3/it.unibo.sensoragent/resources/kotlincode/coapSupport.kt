package kotlincode

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import org.eclipse.californium.core.CoapResponse
import it.unibo.kactor.ActorBasic
import alice.tuprolog.Term
import alice.tuprolog.Struct

object coapSupport{
lateinit var client : CoapClient
lateinit var host   : String
	
	fun init( address : String ){
		host = address
 	}
	
	private fun setClientForPath( path : String ){
		val url = host + "/" + path
		//println("coapSupport | setClientForPath url=$url")
		client = CoapClient( url )
		client.setTimeout( 1000L )
	}
		
	fun setWroomAlarm(){
		setClientForPath( "wroom" )
		val resp : CoapResponse = client.put("alarm", MediaTypeRegistry.TEXT_PLAIN)
	}
	
	fun setWroomIdle(){
		setClientForPath( "wroom" )
		val resp : CoapResponse = client.put("idle", MediaTypeRegistry.TEXT_PLAIN)
	}
	
	fun readWroomState(  ):WroomState{
		setClientForPath( "wroom" )
		val respGet : CoapResponse = client.get( )
		val v = respGet.getResponseText()
		if (v == "alarm"){
			return kotlincode.WroomState.ALARM
		} else {
			return kotlincode.WroomState.IDLE
		}
	}
	
}
