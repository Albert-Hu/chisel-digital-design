package ledcontroller

import chisel3._
import chisel3.util._

class LedController(clkPerSecond: Int = 10) extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(2.W))
    val out = Output(UInt(8.W))
  })
  val ONE_SECOND = (clkPerSecond - 2).S(32.W)
  val cntReg = RegInit(ONE_SECOND)
  val swReg = RegInit(0.U(2.W))
  val ledReg = RegInit(0.U(8.W))
  val fn1 = Wire(UInt(8.W))
  val fn2 = Wire(UInt(8.W))
  val fn3 = Wire(UInt(8.W))
  val fn4 = Wire(UInt(8.W))

  swReg := io.in

  cntReg := cntReg - 1.S

  when (cntReg(31)) {
    when (ledReg === 0.U) {
      fn1 := "b0000_0001".U
      fn2 := "b1000_0000".U
    } .otherwise {
      fn1 := ledReg << 1.U
      fn2 := ledReg >> 1.U
    }

    when (ledReg === "hff".U) {
      fn3 := 0.U
      fn4 := 0.U
    } .otherwise {
      fn3 := (ledReg << 1.U) | "b0000_0001".U
      fn4 := (ledReg >> 1.U) | "b1000_0000".U
    }

    cntReg := ONE_SECOND
  } .otherwise {
    fn1 := ledReg
    fn2 := ledReg
    fn3 := ledReg
    fn4 := ledReg
  }

  when (swReg =/= io.in) {
    ledReg := 0.U
  } .otherwise {
    switch (io.in) {
      is ("b00".U) { ledReg := fn1 }
      is ("b01".U) { ledReg := fn2 }
      is ("b10".U) { ledReg := fn3 }
      is ("b11".U) { ledReg := fn4 }
    }
  }

  io.out := ledReg
}

object LedController extends App {
  chisel3.Driver.execute(args, () => new LedController(50000000))
}
