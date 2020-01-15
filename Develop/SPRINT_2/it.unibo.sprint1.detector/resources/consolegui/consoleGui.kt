package consolegui
 
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import it.unibo.kactor.ActorBasic
import it.unibo.`is`.interfaces.IObserver
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import org.eclipse.californium.core.CoapResponse
 
class consoleGui : IObserver {
	
	companion object{
		val buttonLabels = arrayOf("f","w", "s", "l", "r", "z", "x", "b", "p", "h")
 		lateinit var msgId    : String
		lateinit var client   : CoapClient
		
		fun create(   ){
  			val concreteButton = ButtonAsGui.createButton( buttonLabels )
            concreteButton.addObserver( consoleGui() )
			val url = "coap://localhost:5683/robot/pos"
			client = CoapClient( url )
			client.setTimeout( 1000L )
		}
	  }
	
      override fun update(o: Observable, arg: Any) {	   
   		  var cmd = arg as String
  		  println("guiSupport update $cmd  ")
		  val resp : CoapResponse = client.put(cmd, MediaTypeRegistry.TEXT_PLAIN)
		  println("response=${resp.getCode()}")
      }//update
}
fun main(){
	consoleGui.create(   )
}
 