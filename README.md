# react-native-esc-pos-xprinter

This is an Android Xprinter SDK driver.

## Installation

```sh
npm install react-native-esc-pos-xprinter
```
or
```sh
yarn add react-native-esc-pos-xprinter
```

## Usage

```js
import RNXprinter from "react-native-esc-pos-xprinter";

// Select a printer to use
const printerList = await RNXprinter.getDeviceList();
await RNXprinter.connectPrinter(printerList[0].address);

```

### Print text
After you connected to your printer, try this to make sure everything except yourself is worked perfectly.

```js
const msg =`COMPANY NAME
Folio: 1234-674
Date: 2022-02-04, 16:15:00
Name: JUAN PEREZ
\nPayment to Loan\n
Amortization No.: 24
Equity: $150.00
Interest: $35.00
Moratorium: $0.00
VAT: $20.00
Total Due: $205.00
\n ___________ ____________
     Promoter     Client
\n        Attended:
         DIEGO TORRES
   THIS PROOF WILL NOT BE VALID
WITHOUT SIGNATURE OF THE REPRESENTATIVE
P. SHERMAN, 42 WALLABY STREET, SYDNEY
  
       SPECIALIZED UNIT:
   SUGGESTIONS@EXAMPLE.COM
           PHONES:
987654321 AND 987654321 EXT 608A
          CUSTOMER\n\n`;

// Push Text
// text: string            # The string you want to print
// size: number            # 0 ~ 7 Level
RNXprinter.pushText(msg, 0);

// Push Cut Paper
RNXprinter.pushCutPaper();
```

### Print image from buffer
Thermal printer is a kind of high speed printer, so we need push all things to the buffer first. To add the image to the printer use:
[Printer TEST V3.0C](https://www.youtube.com/watch?v=rbho0L0VqMQ&list=WL)
```js
// Push Image
// Image in buffer: index           # The FLASH index of image
// Currently only supported without download image, you need use your computer to help
RNXprinter.pushFlashImage(1);

// Push Cut Paper
RNXprinter.pushCutPaper();
```

### Print image in base64
After connecting to your printer, send your image in base64 format and add a size.

```js
const base64Image = "iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAMAAABg3..........."
// Push Image
// size: size image
RNXprinter.pushImage(base64Image, 200);

// Push Cut Paper
RNXprinter.pushCutPaper();
```

### Disconnect printer
After connecting to your printer, you can disconnect the printer if it is not in use.

```js
// Disconnect printer
await RNXprinter.disconnectPrinter();
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## Roadmap

- [x] Android support
- [ ] Save default printer
- [ ] Test coverage
- [x] Printer select panel
- [x] Download image to printer
- [x] Bluetooth support
- [ ] USB support

## License

MIT
