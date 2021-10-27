package com.reactnativeescposxprinter;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Matrix;
import android.os.IBinder;
import android.util.Log;
import java.util.Set;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import android.graphics.BitmapFactory;
import android.util.Base64;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import net.posprinter.posprinterface.IMyBinder;

import com.reactnativeescposxprinter.utils.PrinterCommands;
import com.reactnativeescposxprinter.utils.StringUtils;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.service.PosprinterService;
import net.posprinter.utils.BitmapToByteData;
import net.posprinter.utils.DataForSendToPrinterPos58;
import net.posprinter.utils.DataForSendToPrinterPos80;
import net.posprinter.utils.PosPrinterDev;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

public class EscPosXprinterModule extends ReactContextBaseJavaModule {
  public static final String NAME = "EscPosXprinter";

  private String LOG_TAG = "RNXNetprinter";
  private ReactApplicationContext context;

  private byte[] mBuffer = new byte[0];

  // Net
  public static IMyBinder binder;
  public static boolean ISCONNECT;

  //bindService connection
  ServiceConnection conn= new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
      //Bind successfully
      binder= (IMyBinder) iBinder;
      Log.e("binder","connected");
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
      Log.e("disbinder","disconnected");
    }
  };

  public EscPosXprinterModule(ReactApplicationContext reactContext) {
    super(reactContext);

    this.context = reactContext;

    Intent intent=new Intent(this.context, PosprinterService.class);
    this.context.bindService(intent, conn, BIND_AUTO_CREATE);
    Log.v(LOG_TAG, "RNXNetprinter alloc");
  }

  @Override
  public String getName() {
    return "RNXNetprinter";
  }

  @ReactMethod
  public void connectPrinter(String address, final Promise promise){
    binder.connectNetPort(address,9100, new UiExecute() {
      @Override
      public void onsucess() {

        ISCONNECT=true;
        //in this ,you could call acceptdatafromprinter(),when disconnect ,will execute onfailed();
        binder.acceptdatafromprinter(new UiExecute() {
          @Override
          public void onsucess() {
            promise.resolve(true);
          }

          @Override
          public void onfailed() {
            ISCONNECT=false;
            promise.reject("-105", "Device address not exist.");
          }
        });
      }

      @Override
      public void onfailed() {
        ISCONNECT=false;
        promise.reject("-105", "Device address not exist.");
      }
    });
  }

  @ReactMethod
  public void pushText(String text, final int size){

    final String tempText = text;
    binder.writeDataByYouself(
      new UiExecute() {
        @Override
        public void onsucess() {
          Log.v(LOG_TAG, "pushText onsucess");
        }

        @Override
        public void onfailed() {
          Log.v(LOG_TAG, "pushText onfailed");
        }
      }, new ProcessData() {
        @Override
        public List<byte[]> processDataBeforeSend() {

          List<byte[]> list=new ArrayList<byte[]>();
          //creat a text ,and make it to byte[],
          String str=tempText;
          if (str.equals(null)||str.equals("")){
          }else {
            //initialize the printer
//                            list.add( DataForSendToPrinterPos58.initializePrinter());
            list.add(DataForSendToPrinterPos80.initializePrinter());
            byte[] data1= StringUtils.strTobytes(str);
            list.add(PrinterCommands.ESC_ALIGN_CENTER);
            list.add(DataForSendToPrinterPos80.selectCharacterSize(size));
            list.add(data1);
            //should add the command of print and feed line,because print only when one line is complete, not one line, no print
            list.add(DataForSendToPrinterPos80.printAndFeedLine());
            return list;
          }
          return null;
        }
      }
    );
  }


  private Bitmap b1;//grey-scale bitmap
  private  Bitmap b2;//compress bitmap

  /*@ReactMethod
  private void printImage(String base64img, final int width){
    byte [] bytes = Base64.decode(base64img, Base64.DEFAULT);
    Bitmap b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    if (b != null) {
      b1=convertGreyImg(b);
      Message message=new Message();
      message.what=1;
      handler.handleMessage(message);

      //compress the bitmap
      Tiny.BitmapCompressOptions options = new Tiny.BitmapCompressOptions();
      Tiny.getInstance().source(b1).asBitmap().withOptions(options).compress(new BitmapCallback() {
        @Override
        public void callback(boolean isSuccess, Bitmap bitmap) {
          if (isSuccess){
            //                            Toast.makeText(PosActivity.this,"bitmap: "+bitmap.getByteCount(),Toast.LENGTH_LONG).show();
            b2=bitmap;
            b2=resizeImage(b2,width,false);
            Message message=new Message();
            message.what=2;
            handler.handleMessage(message);
          }


        }
      });
    }
  }*/

  public static Bitmap resizeImage(Bitmap bitmap, int w,boolean ischecked)
  {

    Bitmap BitmapOrg = bitmap;
    Bitmap resizedBitmap = null;
    int width = BitmapOrg.getWidth();
    int height = BitmapOrg.getHeight();
    if (width<=w) {
      return bitmap;
    }
    if (!ischecked) {
      int newWidth = w;
      int newHeight = height*w/width;

      float scaleWidth = ((float) newWidth) / width;
      float scaleHeight = ((float) newHeight) / height;

      Matrix matrix = new Matrix();
      matrix.postScale(scaleWidth, scaleHeight);
      // if you want to rotate the Bitmap
      // matrix.postRotate(45);
      resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
        height, matrix, true);
    }else {
      resizedBitmap=Bitmap.createBitmap(BitmapOrg, 0, 0, w, height);
    }

    return resizedBitmap;
  }

  private void printpicCode(final Bitmap printBmp){
    binder.writeDataByYouself(new UiExecute() {
      @Override
      public void onsucess() {
        Log.v(LOG_TAG, "printpicCode onsucess");
      }

      @Override
      public void onfailed() {
        Log.v(LOG_TAG, "printpicCode onfailed");
      }
    }, new ProcessData() {
      @Override
      public List<byte[]> processDataBeforeSend() {
        List<byte[]> list=new ArrayList<byte[]>();
        list.add(DataForSendToPrinterPos80.initializePrinter());
        list.add(PrinterCommands.ESC_ALIGN_CENTER);
        list.add(DataForSendToPrinterPos80.printRasterBmp(
          0,printBmp, BitmapToByteData.BmpType.Threshold, BitmapToByteData.AlignType.Left,576));
        //                list.add(DataForSendToPrinterPos80.printAndFeedForward(3));
        return list;
      }
    });
  }

  public Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what){
        case 2:
          printpicCode(b2);
          break;
      }

    }
  };

  public Bitmap convertGreyImg(Bitmap img) {
    int width = img.getWidth();
    int height = img.getHeight();

    int[] pixels = new int[width * height];

    img.getPixels(pixels, 0, width, 0, 0, width, height);


    //The arithmetic average of a grayscale image; a threshold
    double redSum=0,greenSum=0,blueSun=0;
    double total=width*height;

    for(int i = 0; i < height; i++)  {
      for(int j = 0; j < width; j++) {
        int grey = pixels[width * i + j];

        int red = ((grey  & 0x00FF0000 ) >> 16);
        int green = ((grey & 0x0000FF00) >> 8);
        int blue = (grey & 0x000000FF);



        redSum+=red;
        greenSum+=green;
        blueSun+=blue;


      }
    }
    int m=(int) (redSum/total);

    //Conversion monochrome diagram
    for(int i = 0; i < height; i++)  {
      for(int j = 0; j < width; j++) {
        int grey = pixels[width * i + j];

        int alpha1 = 0xFF << 24;
        int red = ((grey  & 0x00FF0000 ) >> 16);
        int green = ((grey & 0x0000FF00) >> 8);
        int blue = (grey & 0x000000FF);


        if (red>=m) {
          red=green=blue=255;
        }else{
          red=green=blue=0;
        }
        grey = alpha1 | (red << 16) | (green << 8) | blue;
        pixels[width*i+j]=grey;


      }
    }
    Bitmap mBitmap=Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    mBitmap.setPixels(pixels, 0, width, 0, 0, width, height);



    return mBitmap;
  }

  @ReactMethod
  public void pushCutPaper(){
    binder.writeDataByYouself(new UiExecute() {
      @Override
      public void onsucess() {
        Log.v(LOG_TAG, "pushCutPaper onsucess");
      }

      @Override
      public void onfailed() {
        Log.v(LOG_TAG, "pushCutPaper onfailed");
      }
    }, new ProcessData() {
      @Override
      public List<byte[]> processDataBeforeSend() {
        List<byte[]> list=new ArrayList<byte[]>();
        list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66,1));
        return list;
      }
    });
  }
}
