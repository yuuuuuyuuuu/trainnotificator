package com.dev.android.yuu.bluetoothtest;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.util.UUID;

/**
 * Created by Chieko on 9/18/14.
 */
public class BluetoothServerThread extends Thread
{
    private Context mContext = null;
    private BluetoothAdapter mBtAdapter = null;
    private BluetoothServerSocket mBtServerSocket = null;

    private InputStream mInputStream = null;
    private OutputStream mOutputStream = null;

    private static final String SERIAL_PORT_SERVICE_ID = "00001101-0000-1000-8000-00805F9B34FB";
    private static final UUID SERVICE_ID = UUID.fromString(SERIAL_PORT_SERVICE_ID);

    public BluetoothServerThread(Context context)
    {
        Log.d(this.getClass().toString(), "BluetoothServerThread");

        this.mContext = context;

        this.mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        if(null == this.mBtAdapter)
        {
            Log.d(this.getClass().toString(), "Failed to get BtAdapter");
        }
        else
        {
            Log.d(this.getClass().toString(), "Got BtAdapter");

            try
            {
                this.mBtServerSocket = this.mBtAdapter.listenUsingRfcommWithServiceRecord("btserverthred", SERVICE_ID);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void run()
    {
        Log.d(this.getClass().toString(), "run");
        Log.d(this.getClass().toString(), "waiting connection...");

        BluetoothSocket serverSocket = null;

        while(true)
        {
            try
            {
                serverSocket = this.mBtServerSocket.accept();
            }
            catch (IOException e)
            {
                Log.d(this.getClass().toString(), "Failed to accept.");

                e.printStackTrace();
            }

            Log.d(this.getClass().toString(), "Acception success !!");

            if(null != serverSocket)
            {
                this.connect(serverSocket);
            }
        }
    }

    private void connect(BluetoothSocket socket)
    {
        Log.d(this.getClass().toString(), "connect");

        try
        {
            this.mInputStream = socket.getInputStream();
            this.mOutputStream = socket.getOutputStream();

            Log.d(this.getClass().toString(), "Connection established!! ");

            this.mOutputStream.write("Hello bluetooth".getBytes());

            while(true)
            {
                this.sleep(1000);

                this.mOutputStream.write("hello again".getBytes());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }

    public void cancel()
    {
        Log.d(this.getClass().toString(), "cancel");

        try
        {
            this.mBtServerSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }





}
