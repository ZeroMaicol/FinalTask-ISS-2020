package plasticBox
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ActorBasicFsm
 
object plasticBoxSupport{
	var NPB  :  Int = 0
	
	fun create( npb:Int ){
		NPB = npb
	}
}