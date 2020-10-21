package switchledcontroller

import chisel3._

/**
  * This provides an alternate way to run tests, by executing then as a main
  * From sbt (Note: the test: prefix is because this main is under the test package hierarchy):
  * {{{
  * test:runMain ledcontroller.LedControllerMain
  * }}}
  * To see all command line options use:
  * {{{
  * test:runMain ledcontroller.LedControllerMain --help
  * }}}
  * To run with verilator:
  * {{{
  * test:runMain ledcontroller.LedControllerMain --backend-name verilator
  * }}}
  * To run with verilator from your terminal shell use:
  * {{{
  * sbt 'test:runMain ledcontroller.LedControllerMain --backend-name verilator'
  * }}}
  */
object SwitchLedControllerMain extends App {
  iotesters.Driver.execute(args, () => new SwitchLedController) {
    c => new SwitchLedControllerUnitTester(c)
  }
}

/**
  * This provides a way to run the firrtl-interpreter REPL (or shell)
  * on the lowered firrtl generated by your circuit. You will be placed
  * in an interactive shell. This can be very helpful as a debugging
  * technique. Type help to see a list of commands.
  *
  * To run from sbt
  * {{{
  * test:runMain ledcontroller.LedControllerRepl
  * }}}
  * To run from sbt and see the half a zillion options try
  * {{{
  * test:runMain ledcontroller.LedControllerRepl --help
  * }}}
  */
object SwitchLedControllerRepl extends App {
  iotesters.Driver.executeFirrtlRepl(args, () => new SwitchLedController)
}