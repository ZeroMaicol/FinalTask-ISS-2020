package kotlincode

import org.eclipse.californium.core.CoapServer
import it.unibo.kactor.ActorBasic

object resServer{
		fun init(owner: ActorBasic){
			val server = CoapServer();
			server.add( 
				 resWroom(owner,  "wroom").add(	//robot
					 resDetectorBox(owner, "detectorBox"))
			)
			server.start();			
		}
}
