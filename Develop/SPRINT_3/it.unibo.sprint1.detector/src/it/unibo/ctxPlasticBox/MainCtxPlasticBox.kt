/* Generated by AN DISI Unibo */ 
package it.unibo.ctxPlasticBox
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "::1", this, "wroom.pl", "sysRules.pl"
	)
}

