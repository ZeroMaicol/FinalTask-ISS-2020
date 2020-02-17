package sensors

import kotlin.random.Random

object airqualitysensor{
	fun getData():Int {
		return Random.nextInt(0, 100)
	}
}