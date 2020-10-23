package ledcontroller

import chisel3._
import chisel3.util._

class LedController(clkPerSecond: Int = 10) extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(2.W))
    val out = Output(UInt(8.W))
  })
  val ONE_SECOND = (clkPerSecond - 2).S(32.W)
  val cntReg = RegInit(-1.S(32.W))
  val swReg = RegInit(0.U(2.W))
  val ledReg = RegInit(0.U(8.W))
  
  when (swReg =/= io.in) {
    swReg := io.in
    cntReg := ONE_SECOND
    ledReg := 0.U
  } .otherwise {
    cntReg := cntReg - 1.S
    when (cntReg(31)) {
      cntReg := ONE_SECOND
      switch (swReg) {
        is ("b00".U) {
          when (ledReg === "b0000_0000".U) {
            ledReg := "b0000_0001".U
          } .otherwise {
            ledReg := ledReg << 1.U
          }
        }
        is ("b01".U) {
          when (ledReg === "b0000_0000".U) {
            ledReg := "b1000_0000".U
          } .otherwise {
            ledReg := ledReg >> 1.U
          }
        }
        is ("b10".U) {
          when (ledReg === "b1111_1111".U) {
            ledReg := 0.U
          } .otherwise {
            ledReg := (ledReg << 1.U) | "b0000_0001".U
          }
        }
        is ("b11".U) {
          when (ledReg === "b1111_1111".U) {
            ledReg := 0.U
          } .otherwise {
            ledReg := (ledReg >> 1.U) | "b1000_0000".U
          }
        }
      }
    }
  }

  io.out := ledReg
}

object LedController extends App {
  chisel3.Driver.execute(args, () => new LedController(50000000))
}
