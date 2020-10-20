package ledcontroller

import chisel3._
import chisel3.util._

class LedController extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(2.W))
    val out = Output(UInt(8.W))
  })
  //val ONE_SECOND = (100000000 - 1).U(32.W)
  val ONE_SECOND = (10 - 1).U(32.W)
  val cntReg = RegInit(ONE_SECOND)
  val swReg = RegInit(0.U(2.W))
  val ledReg = RegInit(0.U(8.W))
  val update = Wire(Bool())
  val fn1 = Wire(UInt(8.W))
  val fn2 = Wire(UInt(8.W))
  val fn3 = Wire(UInt(8.W))
  val fn4 = Wire(UInt(8.W))

  swReg := io.in

  cntReg := cntReg - 1.U
  update := cntReg === 0.U

  when (update) {
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
/*
  val MAX = (50000000 - 2).S(32.W)
  val cntReg = RegInit(MAX) // 50MHz Counter
  val swReg = RegInit(0.U(2.W)) // switch buttons
  val ledReg = RegInit(0.U(8.W)) // led
  val defVal = Wire(UInt(2.W))
  val outVal = Wire(UInt(8.W))

  swReg := io.in

  switch (swReg) {
    is ("b00".U) { defVal := "h00".U }
    is ("b01".U) { defVal := "h00".U }
    is ("b10".U) { defVal := "h01".U }
    is ("b11".U) { defVal := "h80".U }
  }

  switch (swReg) {
    is ("b00".U) {
      when (ledReg === "hff".U) {
        outVal := 0.U
      } .otherwise {
        outVal := (ledReg << 1.U) | 1.U
      }
    }
    is ("b01".U) {
      when (ledReg === "hff".U) {
        outVal := 0.U
      } .otherwise {
        outVal := (ledReg >> 1.U) | "h80".U
      }
    }
    is ("b10".U) {
      when (ledReg === 0.U) {
        outVal := 1.U
      } .otherwise {
        outVal := ledReg << 1.U
      }
    }
    is ("b11".U) {
      when (ledReg === 0.U) {
        outVal := "h80".U
      } .otherwise {
        outVal := ledReg >> 1.U
      }
    }
  }

  when (ledReg =/= io.in) {
    ledReg := defVal
  } .otherwise {
    ledReg := outVal
  }

  io.out := ledReg
*/
}

