import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-esc-pos-xprinter' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const RNXprinter = NativeModules.RNXprinter
  ? NativeModules.RNXprinter
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function getDeviceList(): Promise<any> {
  return RNXprinter.getDeviceList();
}

export function connectPrinter(bleAddress: string): Promise<any> {
  return RNXprinter.connectPrinter(bleAddress);
}

export function disconnectPrinter(): Promise<any> {
  return RNXprinter.disconnectPrinter();
}

export function pushText(text: string, size: number): void {
  return RNXprinter.pushText(text, size);
}

export function pushFlashImage(index: number): void {
  return RNXprinter.pushFlashImage(index);
}

export function pushImage(base64img: string, width: number): void {
  return RNXprinter.pushImage(base64img, width);
}

export function pushCutPaper(): void {
  return RNXprinter.pushCutPaper();
}
