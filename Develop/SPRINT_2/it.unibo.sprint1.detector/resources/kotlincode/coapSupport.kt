package kotlincode

import org.eclipse.californium.core.coap.CoAP
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.CoapHandler
import it.unibo.kactor.ActorBasic
import alice.tuprolog.Term
import alice.tuprolog.Struct
import kotlinx.coroutines.launch
import it.unibo.kactor.MsgUtil
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.MessageObserverAdapter
import org.eclipse.californium.core.coap.Response

object coapSupport{
lateinit var client : CoapClient
lateinit var host   : String
	
	fun init( address : String ){
		host = address
 	}
	
	private fun setClientForPath( path : String ){
		println("setClientForPath $path")
		val url = host + "/" + path
		client = CoapClient( url )
		client.setTimeout( 1000L )
		client.useNONs()
	}
	
	fun updateResource( owner : ActorBasic, path: String, msg : String ){
		setClientForPath( path )
		val resp : CoapResponse = client.put(msg, MediaTypeRegistry.TEXT_PLAIN)
	}
	
	fun updateDetectorPosition(pos:String, direction:String, moving:String){
		setClientForPath( "wroom/detectorPosition" )
		val resp : CoapResponse = client.put(pos+"."+direction+"."+moving, MediaTypeRegistry.TEXT_PLAIN)
	}

	fun observeCommands(actor: ActorBasic) {
		setClientForPath("wroom/robotCommand")
		val relation = client.observe(ForwardCommandToActor(actor))
	}
	
	fun readResource(  owner : ActorBasic, path : String ){
		setClientForPath( path )
		val respGet : CoapResponse = client.get( )
		val v = respGet.getResponseText()
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
	
	fun readPlasticBox(  path : String, result : HashMap<Int,Int> ){
		setClientForPath( path )
		val respGet : CoapResponse = client.get( )
		val v = "state("+respGet.getResponseText()+")"
		val bottles = ( Term.createTerm(v) as Struct ).getArg(0).toString()
		val npb = ( Term.createTerm(v) as Struct ).getArg(1).toString()
		//println("coapSupport | readPos v=$v")
		result.put(1, bottles.toInt())
		result.put(2, npb.toInt())
	}
	
	fun readDetectorBox(  path : String, result : HashMap<Int,Int> ){
		setClientForPath( path )
		val respGet : CoapResponse = client.get( )
		val v = "state("+respGet.getResponseText()+")"
		val bottles = ( Term.createTerm(v) as Struct ).getArg(0).toString()
		val ndb = ( Term.createTerm(v) as Struct ).getArg(1).toString()
		result.put(1, bottles.toInt())
		result.put(2, ndb.toInt())
	}
	
	
}

class ForwardCommandToActor(actor: ActorBasic): CoapHandler {
	
	var actor: ActorBasic
	var previousResponse: CoapResponse? = null
	var previous: String? = null
	
	init {
		this.actor = actor
	}
	
	override fun onLoad(response: CoapResponse) {
		val content = response.getResponseText()
		if (content != previous){
			println("NOTIFICATION $content, code: ${response.getCode().value}")
			response.advanced().setCanceled(true)
			actor.scope.launch { MsgUtil.sendMsg("cmd", "cmd($content)", actor) }
			previous = content
		} else {
			println("Same as before, no send")
		}
	}
	
	override fun onError() {
		println("OBSERVING FAILURE")
	}
	
}

class MyMessageObserverAdapter: MessageObserverAdapter() {
	override fun onResponse(response: Response) {
		println("response received")
		println(response)
	}
}
