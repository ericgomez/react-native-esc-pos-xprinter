# react-native-esc-pos-xprinter

This is an android xprinter driver.

## Installation

```sh
npm install react-native-esc-pos-xprinter
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
// Push Text
// text: string            # The string you want to print
// size: number            # 0 ~ 7 Level
RNXprinter.pushText("Hello World!!!", 0);

// Push Cut Paper
RNXprinter.pushCutPaper();
```

### Push image to buffer
Thermal printer is a kind of high speed printer, so we need push all things to the buffer first. To add the image to the printer use:
[Printer TEST V3.0C](https://www.youtube.com/watch?v=rbho0L0VqMQ&list=WL)
```js
// Push Image
// size: index             # The FLASH index of image
// Currently only supported without download image, you need use your computer to help
RNXprinter.pushFlashImage(0);

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

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## Roadmap

- [x] Android support
- [ ] Save default printer
- [ ] Test coverage
- [x] Printer select panel
- [x] Download image to printer
- [ ] USB support

## License

MIT
