module LedController(
  input        clock,
  input        reset,
  input  [1:0] io_in,
  output [7:0] io_out
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
`endif // RANDOMIZE_REG_INIT
  reg [31:0] cntReg; // @[LedController.scala 12:23]
  reg [1:0] swReg; // @[LedController.scala 13:22]
  reg [7:0] ledReg; // @[LedController.scala 14:23]
  wire  _T = swReg != io_in; // @[LedController.scala 16:15]
  wire [31:0] _T_3 = $signed(cntReg) - 32'sh1; // @[LedController.scala 21:22]
  wire  _T_5 = 2'h0 == swReg; // @[Conditional.scala 37:30]
  wire  _T_6 = ledReg == 8'h0; // @[LedController.scala 26:24]
  wire [8:0] _T_7 = {ledReg, 1'h0}; // @[LedController.scala 29:30]
  wire [8:0] _GEN_0 = _T_6 ? 9'h1 : _T_7; // @[LedController.scala 26:44]
  wire  _T_8 = 2'h1 == swReg; // @[Conditional.scala 37:30]
  wire [7:0] _T_10 = {{1'd0}, ledReg[7:1]}; // @[LedController.scala 36:30]
  wire [7:0] _GEN_1 = _T_6 ? 8'h80 : _T_10; // @[LedController.scala 33:44]
  wire  _T_11 = 2'h2 == swReg; // @[Conditional.scala 37:30]
  wire  _T_12 = ledReg == 8'hff; // @[LedController.scala 40:24]
  wire [8:0] _T_14 = _T_7 | 9'h1; // @[LedController.scala 43:39]
  wire [8:0] _GEN_2 = _T_12 ? 9'h0 : _T_14; // @[LedController.scala 40:44]
  wire  _T_15 = 2'h3 == swReg; // @[Conditional.scala 37:30]
  wire [7:0] _T_18 = _T_10 | 8'h80; // @[LedController.scala 50:39]
  wire [7:0] _GEN_3 = _T_12 ? 8'h0 : _T_18; // @[LedController.scala 47:44]
  wire [7:0] _GEN_4 = _T_15 ? _GEN_3 : ledReg; // @[Conditional.scala 39:67]
  wire [8:0] _GEN_5 = _T_11 ? _GEN_2 : {{1'd0}, _GEN_4}; // @[Conditional.scala 39:67]
  wire [8:0] _GEN_6 = _T_8 ? {{1'd0}, _GEN_1} : _GEN_5; // @[Conditional.scala 39:67]
  wire [8:0] _GEN_7 = _T_5 ? _GEN_0 : _GEN_6; // @[Conditional.scala 40:58]
  wire [8:0] _GEN_9 = cntReg[31] ? _GEN_7 : {{1'd0}, ledReg}; // @[LedController.scala 22:23]
  wire [8:0] _GEN_12 = _T ? 9'h0 : _GEN_9; // @[LedController.scala 16:26]
  assign io_out = ledReg; // @[LedController.scala 57:10]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  cntReg = _RAND_0[31:0];
  _RAND_1 = {1{`RANDOM}};
  swReg = _RAND_1[1:0];
  _RAND_2 = {1{`RANDOM}};
  ledReg = _RAND_2[7:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      cntReg <= -32'sh1;
    end else if (_T) begin
      cntReg <= 32'sh2faf07e;
    end else if (cntReg[31]) begin
      cntReg <= 32'sh2faf07e;
    end else begin
      cntReg <= _T_3;
    end
    if (reset) begin
      swReg <= 2'h0;
    end else if (_T) begin
      swReg <= io_in;
    end
    if (reset) begin
      ledReg <= 8'h0;
    end else begin
      ledReg <= _GEN_12[7:0];
    end
  end
endmodule
