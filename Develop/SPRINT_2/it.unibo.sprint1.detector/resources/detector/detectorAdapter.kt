package detector
import it.unibo.kactor.*
import alice.tuprolog.*
import detector.detectorSupport

class detectorAdapter( name : String ) : ActorBasic( name ){  
    init{
		println("	--- detectorAdapter | started")
		val sol1 = pengine.solve( "consult('detectorConfig.pl')." )
		if( ! sol1.isSuccess() ){
			println("	--- detectorAdapter | ERROR in detectorConfig.pl")
		}else{
	 		val sol2 = pengine.solve( "detectorBox(NDB)." )
	 		if( sol2.isSuccess() ){
				val NDB =  sol2.getVarValue("NDB").toString()
				println("	--- detectorAdapter | USING NDB: $NDB")
				val NDB_Int = NDB.toInt()
				detector.detectorSupport.create( NDB_Int )   
			}
		}		  		      
    }
 
    override suspend fun actorBody(msg : ApplMessage){
        //println("	--- robotAdapterQaStream | received  msg= $msg "  ) //msg.msgContent()=cmd(X)
		sysUtil.traceprintln(" $tt $name | received  $msg "  ) //msg.msgContent()=cmd(X)
    }
}