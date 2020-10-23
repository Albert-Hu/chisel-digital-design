# DE10 Nano Led Controller

Chisel soruce codes:
- src/main/scala/de10-nano-led-controller
- src/test/scala/de10-nano-led-controller

FPGA demo:
- fpga/de10-nano-led-controller/fpga

This example uses two switches to control the led lighting in 4 modes.

## Mode 1, Switch(Binary Format): 00

The status starts at step 1 and ends at step 9 then repeating from step 1.

```
- o: led on.
- x: led off.

1. LED Status: x x x x x x x x
2. LED Status: x x x x x x x o
3. LED Status: x x x x x x o x
4. LED Status: x x x x x o x x
5. LED Status: x x x x o x x x
6. LED Status: x x x o x x x x
7. LED Status: x x o x x x x x
8. LED Status: x o x x x x x x
9. LED Status: o x x x x x x x
```

## Mode 2, Switch(Binary Format): 01

The status starts at step 1 and ends at step 9 then repeating from step 1.

```
- o: led on.
- x: led off.

1. LED Status: x x x x x x x x
2. LED Status: o x x x x x x x
3. LED Status: x o x x x x x x
4. LED Status: x x o x x x x x
5. LED Status: x x x o x x x x
6. LED Status: x x x x o x x x
7. LED Status: x x x x x o x x
8. LED Status: x x x x x x o x
9. LED Status: x x x x x x x o
```

## Mode 3, Switch(Binary Format): 10

The status starts at step 1 and ends at step 9 then repeating from step 1.

```
- o: led on.
- x: led off.

1. LED Status: x x x x x x x x
2. LED Status: x x x x x x x o
3. LED Status: x x x x x x o o
4. LED Status: x x x x x o o o
5. LED Status: x x x x o o o o
6. LED Status: x x x o o o o o
7. LED Status: x x o o o o o o
8. LED Status: x o o o o o o o
9. LED Status: o o o o o o o o
```

## Mode 4, Switch(Binary Format): 11

The status starts at step 1 and ends at step 9 then repeating from step 1.

```
- o: led on.
- x: led off.

1. LED Status: x x x x x x x x
2. LED Status: o x x x x x x x
3. LED Status: o o x x x x x x
4. LED Status: o o o x x x x x
5. LED Status: o o o o x x x x
6. LED Status: o o o o o x x x
7. LED Status: o o o o o o x x
8. LED Status: o o o o o o o x
9. LED Status: o o o o o o o o
```
