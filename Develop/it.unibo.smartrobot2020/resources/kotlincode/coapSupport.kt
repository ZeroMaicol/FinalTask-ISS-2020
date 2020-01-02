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
	
	fun updateResource( owner : ActorBasic, path: String, msg : String ){
		setClientForPath( path )
		//println("coapSupport | updateResource $msg $client")
		val resp : CoapResponse = client.put(msg, MediaTypeRegistry.TEXT_PLAIN)
		//println("coapSupport | updateResource respCode=${resp.getCode()}")
	}
	
	fun readResource(  owner : ActorBasic, path : String ){
		setClientForPath( path )
		val respGet : CoapResponse = client.get( )
		val v = respGet.getResponseText()
		//println("coapSupport | readResource v=$v")
	}
	
	fun readPos(  path : String, result : HashMap<Int,String> ){
		setClientForPath( path )
		val respGet : CoapResponse = client.get( )
		val v = "state("+respGet.getResponseText()+")"
		val pos = ( Term.createTerm(v) as Struct ).getArg(0).toString()
		val dir = ( Term.createTerm(v) as Struct ).getArg(1).toString()
		//println("coapSupport | readPos v=$v")
		result.put(1, pos)
		result.put(2, dir)
	}
	
}
