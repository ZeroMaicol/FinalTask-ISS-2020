package kotlincode

import org.eclipse.californium.core.CoapServer
import it.unibo.kactor.ActorBasic

object resServer{
		fun init(owner: ActorBasic){
			val server = CoapServer();
			server.add( 
				 resRobot(owner,  "robot").add(	//robot
					 resRobotPosition(owner, "pos"),
					 resRobotSonar(owner, "sonar") )  //robot/pos, robot/sonar
			)
			server.start();			
		}
}
