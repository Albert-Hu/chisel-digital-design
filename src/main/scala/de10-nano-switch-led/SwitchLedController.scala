package switchledcontroller

import chisel3._
import chisel3.util._

class SwitchLedController() extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(4.W))
    val out = Output(UInt(4.W))
  })
  io.out := io.in
}

object SwitchLedController extends App {
  chisel3.Driver.execute(args, () => new SwitchLedController)
}
