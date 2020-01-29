package plasticBox
import it.unibo.kactor.*
import alice.tuprolog.*
import plasticBox.plasticBoxSupport

class plasticBoxAdapter( name : String ) : ActorBasic( name ){  
    init{
		println("	--- plasticBoxAdapter | started")
		val sol1 = pengine.solve( "consult('plasticBoxConfig.pl')." )
		if( ! sol1.isSuccess() ){
			println("	--- plasticBoxAdapter | ERROR in plasticBoxConfig.pl")
		}else{
	 		val sol2 = pengine.solve( "plasticBox(NPB)." )
	 		if( sol2.isSuccess() ){
				val NPB =  sol2.getVarValue("NPB").toString()
				println("	--- plasticBoxAdapter | USING NPB: $NPB")
				val NPB_Int = NPB.toInt()
				plasticBox.plasticBoxSupport.create( NPB_Int )   
			}
		}		  		      
    }
 
    override suspend fun actorBody(msg : ApplMessage){
        //println("	--- robotAdapterQaStream | received  msg= $msg "  ) //msg.msgContent()=cmd(X)
		sysUtil.traceprintln(" $tt $name | received  $msg "  ) //msg.msgContent()=cmd(X)
    }
}