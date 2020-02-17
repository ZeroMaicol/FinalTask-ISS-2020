package adapter
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ActorBasicFsm
 
object sensorSupport{
	lateinit var sensorKind  :  String
	
	fun create( type:String ){
		sensorKind = type
		println( "		--- sensorSupport | CREATED for $sensorKind" )
	}
	
	fun getSensorValue( ):Int{
		//println("robotSupport move cmd=$cmd robotKind=$robotKind" )
		when( sensorKind ){
			"virtual"  -> { return sensors.airqualitysensor.getData() }	
			"realmbot" -> { return 10 }
			"realnano" -> { return 10 }
			else       -> println( "		--- sensorSupport | sensor unknown" )
		}
		return 0
	}
}