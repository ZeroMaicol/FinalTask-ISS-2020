package adapter
import it.unibo.kactor.*
import alice.tuprolog.*
import adapter.sensorSupport

class sensorAdapter( name : String ) : ActorBasic( name ){  
    init{
		println("	--- sensorAdapter | started")
		val sol1 = pengine.solve( "consult('sensorConfig.pl')." )
		if( ! sol1.isSuccess() ){
			println("	--- sensorAdapter | ERROR in sensorConfig.pl")
		}else{
	 		val sol2 = pengine.solve( "sensor(TYPE)." )
	 		if( sol2.isSuccess() ){
				val TYPE =  sol2.getVarValue("TYPE").toString()
				println("	--- sensorAdapter | FOR TYPE: $TYPE")
				sensor.sensorSupport.create( TYPE )   
			}
		}		  		      
    }
 
    override suspend fun actorBody(msg : ApplMessage){
        //println("	--- robotAdapterQaStream | received  msg= $msg "  ) //msg.msgContent()=cmd(X)
		sysUtil.traceprintln(" $tt $name | received  $msg "  ) //msg.msgContent()=cmd(X)
    }
}