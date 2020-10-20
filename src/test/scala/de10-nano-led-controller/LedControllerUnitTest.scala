// See README.md for license details.

package ledcontroller

import java.io.File

import chisel3.iotesters
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class LedControllerUnitTester(c: LedController) extends PeekPokeTester(c) {
  for (i <- 0 to 10 by 1) {
    printf("Switch: %d\n", i % 4)
    poke(c.io.in, i)
    step(1)
    for (j <- 1 to 10 by 1) {
      step(10)
      printf("Step %02d. LED: %s\n", j, Integer.toString(peek(c.io.out).toInt, 2))
    }
  }
}

/**
  * This is a trivial example of how to run this Specification
  * From within sbt use:
  * {{{
  * testOnly ledcontroller.LedControllerTester
  * }}}
  * From a terminal shell use:
  * {{{
  * sbt 'testOnly ledcontroller.LedControllerTester'
  * }}}
  */
class LedControllerTester extends ChiselFlatSpec {
  private val backendNames = if(firrtl.FileUtils.isCommandAvailable(Seq("verilator", "--version"))) {
    Array("firrtl", "verilator")
  }
  else {
    Array("firrtl")
  }
  for ( backendName <- backendNames ) {
    "LedController" should s"calculate proper greatest common denominator (with $backendName)" in {
      Driver(() => new LedController, backendName) {
        c => new LedControllerUnitTester(c)
      } should be (true)
    }
  }

  "Basic test using Driver.execute" should "be used as an alternative way to run specification" in {
    iotesters.Driver.execute(Array(), () => new LedController) {
      c => new LedControllerUnitTester(c)
    } should be (true)
  }

  if(backendNames.contains("verilator")) {
    "using --backend-name verilator" should "be an alternative way to run using verilator" in {
      iotesters.Driver.execute(Array("--backend-name", "verilator"), () => new LedController) {
        c => new LedControllerUnitTester(c)
      } should be(true)
    }
  }

  "running with --is-verbose" should "show more about what's going on in your tester" in {
    iotesters.Driver.execute(Array("--is-verbose"), () => new LedController) {
      c => new LedControllerUnitTester(c)
    } should be(true)
  }

  /**
    * By default verilator backend produces vcd file, and firrtl and treadle backends do not.
    * Following examples show you how to turn on vcd for firrtl and treadle and how to turn it off for verilator
    */

  "running with --generate-vcd-output on" should "create a vcd file from your test" in {
    iotesters.Driver.execute(
      Array("--generate-vcd-output", "on", "--target-dir", "test_run_dir/make_a_vcd", "--top-name", "make_a_vcd"),
      () => new LedController
    ) {

      c => new LedControllerUnitTester(c)
    } should be(true)

    new File("test_run_dir/make_a_vcd/LedController.vcd").exists should be (true)
  }

  "running with --generate-vcd-output off" should "not create a vcd file from your test" in {
    iotesters.Driver.execute(
      Array("--generate-vcd-output", "off", "--target-dir", "test_run_dir/make_no_vcd", "--top-name", "make_no_vcd",
      "--backend-name", "verilator"),
      () => new LedController
    ) {

      c => new LedControllerUnitTester(c)
    } should be(true)

    new File("test_run_dir/make_no_vcd/make_a_vcd.vcd").exists should be (false)

  }

}
