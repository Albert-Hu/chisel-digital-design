module Top(
	input        clk,
  input        rst,
  input  [1:0] sw,
  output [7:0] led
);
	LedController m(
		.clock(clk),
		.reset(~rst),
		.io_in(sw),
		.io_out(led)
	);
endmodule

