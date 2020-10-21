// See README.md for license details.

package ledcontroller

import java.io.File

import chisel3.iotesters
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class LedControllerUnitTester(c: LedController) extends PeekPokeTester(c) {
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

  for (i <- 1 to 10 by 1) {
    val sw = i % 4
    var led = 0
    printf("Switch: %s\n", toBinaryString(sw, 2))
    poke(c.io.in, sw)
    step(1)
    for (j <- 1 to 10 by 1) {
      step(clkPerSecond)
      led = sw match {
        case 0 => if (led == 0 || led == 0x80) 1 else (led << 1)
        case 1 => if (led == 0) 0x80 else (led >> 1)
        case 2 => if (led == 0xff) 0 else (led << 1) + 1
        case 3 => if (led == 0xff) 0 else (led >> 1) + 0x80
      }
      printf("Step %02d. LED: %s : %s\n",
        j,
        toBinaryString(peek(c.io.out).toInt, 8),
        toBinaryString(led, 8))
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
