// See README.md for license details.

package switchledcontroller

import java.io.File

import chisel3.iotesters
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class SwitchLedControllerUnitTester(c: SwitchLedController) extends PeekPokeTester(c) {
  val clkPerSecond: Int = 10

  def toBinaryString(value: Int, bits: Int) : String = {
    var binary = Integer.toString(value, 2)
    var padding = ""
    var paddingZeros = bits - binary.length()
    while (paddingZeros > 0) {
      padding += "0"
      paddingZeros -= 1;
    }
    padding + binary
  }

  for (i <- 1 to 20 by 1) {
    val sw = i % 16
    poke(c.io.in, sw)
    step(1)
    printf("Switch: %s, LED: %s\n", toBinaryString(sw, 4), toBinaryString(peek(c.io.out).toInt, 4))
  }
}

/**
  * This is a trivial example of how to run this Specification
  * From within sbt use:
  * {{{
  * testOnly switchledcontroller.SwitchLedController
  * }}}
  * From a terminal shell use:
  * {{{
  * sbt 'testOnly switchledcontroller.SwitchLedController'
  * }}}
  */
class SwitchLedControllerTester extends ChiselFlatSpec {
  private val backendNames = if(firrtl.FileUtils.isCommandAvailable(Seq("verilator", "--version"))) {
    Array("firrtl", "verilator")
  }
  else {
    Array("firrtl")
  }
  for ( backendName <- backendNames ) {
    "SwitchLedController" should s"calculate proper greatest common denominator (with $backendName)" in {
      Driver(() => new SwitchLedController, backendName) {
        c => new SwitchLedControllerUnitTester(c)
      } should be (true)
    }
  }

  "Basic test using Driver.execute" should "be used as an alternative way to run specification" in {
    iotesters.Driver.execute(Array(), () => new SwitchLedController) {
      c => new SwitchLedControllerUnitTester(c)
    } should be (true)
  }

  if(backendNames.contains("verilator")) {
    "using --backend-name verilator" should "be an alternative way to run using verilator" in {
      iotesters.Driver.execute(Array("--backend-name", "verilator"), () => new SwitchLedController) {
        c => new SwitchLedControllerUnitTester(c)
      } should be(true)
    }
  }

  "running with --is-verbose" should "show more about what's going on in your tester" in {
    iotesters.Driver.execute(Array("--is-verbose"), () => new SwitchLedController) {
      c => new SwitchLedControllerUnitTester(c)
    } should be(true)
  }

  /**
    * By default verilator backend produces vcd file, and firrtl and treadle backends do not.
    * Following examples show you how to turn on vcd for firrtl and treadle and how to turn it off for verilator
    */

  "running with --generate-vcd-output on" should "create a vcd file from your test" in {
    iotesters.Driver.execute(
      Array("--generate-vcd-output", "on", "--target-dir", "test_run_dir/make_a_vcd", "--top-name", "make_a_vcd"),
      () => new SwitchLedController
    ) {

      c => new SwitchLedControllerUnitTester(c)
    } should be(true)

    new File("test_run_dir/make_a_vcd/SwitchLedController.vcd").exists should be (true)
  }

  "running with --generate-vcd-output off" should "not create a vcd file from your test" in {
    iotesters.Driver.execute(
      Array("--generate-vcd-output", "off", "--target-dir", "test_run_dir/make_no_vcd", "--top-name", "make_no_vcd",
      "--backend-name", "verilator"),
      () => new SwitchLedController
    ) {

      c => new SwitchLedControllerUnitTester(c)
    } should be(true)

    new File("test_run_dir/make_no_vcd/make_a_vcd.vcd").exists should be (false)

  }

}
