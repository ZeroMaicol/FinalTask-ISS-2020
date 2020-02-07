package detector
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ActorBasicFsm
 
object detectorSupport{
	var NDB  :  Int = 0
	
	fun create( ndb:Int ){
		NDB = ndb
	}
}