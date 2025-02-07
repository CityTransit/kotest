package io.kotest.engine

import io.kotest.common.ExperimentalKotest
import io.kotest.engine.listener.NoopTestEngineListener
import io.kotest.engine.listener.TestEngineListener
import io.kotest.engine.spec.SpecExecutor
import io.kotest.mpp.log

/**
 * A [TestSuiteScheduler] is responsible for launching each spec from a [TestSuite] into a coroutine.
 */
@ExperimentalKotest
interface TestSuiteScheduler {
   suspend fun schedule(suite: TestSuite, listener: TestEngineListener): EngineResult
}

/**
 * A [TestSuiteScheduler] that launches specs sequentially in a loop.
 */
object LoopingTestSuiteScheduler : TestSuiteScheduler {

   override suspend fun schedule(suite: TestSuite, listener: TestEngineListener): EngineResult {
      log { "LoopingTestSuiteScheduler: Executing ${suite.specs} specs" }
      suite.specs.forEach {
         val executor = SpecExecutor(NoopTestEngineListener)
         executor.execute(it)
      }
      return EngineResult(emptyList())
   }
}

/**
 * Returns the appropriate TestSuiteScheduler for the platform.
 */
expect fun createTestSuiteScheduler(): TestSuiteScheduler
