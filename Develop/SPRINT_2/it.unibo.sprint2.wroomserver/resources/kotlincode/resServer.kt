package kotlincode

import org.eclipse.californium.core.CoapServer
import it.unibo.kactor.ActorBasic
import org.eclipse.californium.core.network.config.NetworkConfig

object resServer{
		fun init(owner: ActorBasic){
			val config: NetworkConfig = NetworkConfig.getStandard()
			config.setInt(NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_COUNT, 1);

			val server = CoapServer(config);
			
			server.add( 
				 resWroom(owner,  "wroom")
					 .add(resDetectorBox(owner, "detectorBox"))
					 .add(resPlasticBox(owner, "plasticBox"))
					 .add(resRobotCommand("robotCommand"))
			)
			server.start();			
		}
}
